package org.es4j.eventstore.core;

import java.util.*;
import java.util.Map.Entry;
import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.*;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using System.Diagnostics.CodeAnalysis;
//using System.Linq;
//using Logging;
//[SuppressMessage("Microsoft.Naming", "CA1711:IdentifiersShouldNotHaveIncorrectSuffix",
//	Justification = "This behaves like a stream--not a .NET 'Stream' object, but a stream nonetheless.")]
public class OptimisticEventStream implements IEventStream {

    private static final ILog logger = LogFactory.buildLogger(OptimisticEventStream.class);
    private final List<EventMessage> committed = new LinkedList<EventMessage>();
    private final List<EventMessage> events = new LinkedList<EventMessage>();
    private final Map<String, Object>      uncommittedHeaders = new HashMap<String, Object>();
    private final Map<String, Object>      committedHeaders = new HashMap<String, Object>();
    private final Collection<UUID>         identifiers = new HashSet<UUID>();
    private final ICommitEvents            persistence;
    private boolean disposed;

    public OptimisticEventStream(UUID streamId, ICommitEvents persistence) {
        this.streamId = streamId;
        this.persistence = persistence;
    }

    public OptimisticEventStream(UUID streamId, ICommitEvents persistence, int minRevision, int maxRevision) {
        this(streamId, persistence);
        Iterable<Commit> commits = persistence.getFrom(streamId, minRevision, maxRevision);
        this.populateStream(minRevision, maxRevision, commits);

        if (minRevision > 0 && this.committed.isEmpty()) {
            throw new StreamNotFoundException();
        }
    }

    public OptimisticEventStream(Snapshot snapshot, ICommitEvents persistence, int maxRevision) {
        this(snapshot.getStreamId(), persistence);
        Iterable<Commit> commits = persistence.getFrom(snapshot.getStreamId(), snapshot.getStreamRevision(), maxRevision);
        this.populateStream(snapshot.getStreamRevision() + 1, maxRevision, commits);
        this.streamRevision = snapshot.getStreamRevision() + this.committed.size();
    }

    protected void populateStream(int minRevision, int maxRevision, Iterable<Commit> commits) {
        for (Commit commit : (commits != null) ? commits : new LinkedList<Commit>()) {
            logger.verbose(Resources.AddingCommitsToStream(), commit.getCommitId(), commit.getEvents().size(), this.streamId);
            this.identifiers.add(commit.getCommitId());

            this.commitSequence = commit.getCommitSequence();
            int currentRevision = commit.getStreamRevision() - commit.getEvents().size() + 1;
            if (currentRevision > maxRevision) {
                return;
            }

            this.copyToCommittedHeaders(commit);
            this.copyToEvents(minRevision, maxRevision, currentRevision, commit);
        }
    }

    private void copyToCommittedHeaders(Commit commit) {
        for (Entry<String, Object> entry : commit.getHeaders().entrySet()) {
            String key = entry.getKey();
            this.committedHeaders.put(key, commit.getHeaders().get(key));
        }
    }

    private void copyToEvents(int minRevision, int maxRevision, int currentRevision, Commit commit) {
        for (EventMessage event : commit.getEvents()) {
            if (currentRevision > maxRevision) {
                logger.debug(Resources.IgnoringBeyondRevision(), commit.getCommitId(), this.streamId, maxRevision);
                break;
            }

            if (currentRevision++ < minRevision) {
                logger.debug(Resources.IgnoringBeforeRevision(), commit.getCommitId(), this.streamId, maxRevision);
                continue;
            }

            this.committed.add(event);
            this.streamRevision = currentRevision - 1;
        }
    }

    @Override
    public void close() {
        dispose();
    }
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    //virtual
    protected void dispose(boolean disposing) {
        this.disposed = true;
    }


    private UUID streamId;       // { get; private set; } // virtual
    private int  streamRevision; // { get; private set; } // virtual
    private int  commitSequence; // { get; private set; } // virtual

    // virtual
    @Override
    public Collection<EventMessage> getCommittedEvents() {
        return Collections.unmodifiableList(this.committed);
    }

// virtual
    @Override
    public Map<String, Object> getCommittedHeaders() {
        return this.committedHeaders;
    }

    // virtual
    @Override
    public Collection<EventMessage> getUncommittedEvents() {
        return Collections.unmodifiableList(this.events);
    }

    // virtual
    @Override
    public Map<String, Object> getUncommittedHeaders() {
        return this.uncommittedHeaders;
    }

    // virtual
    @Override
    public void add(EventMessage uncommittedEvent) {
        if (uncommittedEvent == null || uncommittedEvent.getBody() == null) {
            return;
        }
        logger.debug(Resources.AppendingUncommittedToStream(), this.streamId);
        this.events.add(uncommittedEvent);
    }

    // virtual
    @Override
    public void commitChanges(UUID commitId) {
        logger.debug(Resources.AttemptingToCommitChanges(), this.streamId);

        if (this.identifiers.contains(commitId)) {
            throw new DuplicateCommitException();
        }

        if (!this.hasChanges()) {
            return;
        }

        try {
            this.persistChanges(commitId);
        } catch (ConcurrencyException ex) {
            logger.info(Resources.UnderlyingStreamHasChanged(), this.streamId);
            Iterable<Commit> commits = this.persistence.getFrom(this.streamId, this.streamRevision + 1, Integer.MAX_VALUE);
            this.populateStream(this.streamRevision + 1, Integer.MAX_VALUE, commits);
            throw ex;
        }
    }

    // virtual
    protected boolean hasChanges() {
        if (this.disposed) {
            throw new ObjectDisposedException(Resources.AlreadyDisposed());
        }

        if (this.events.size() > 0) {
            return true;
        }

        logger.warn(Resources.NoChangesToCommit(), this.streamId);
        return false;
    }

    // virtual
    protected void persistChanges(UUID commitId) {
        Commit attempt = this.buildCommitAttempt(commitId);

        logger.debug(Resources.PersistingCommit(), commitId, this.streamId);
        this.persistence.commit(attempt);

        this.populateStream(this.streamRevision + 1, attempt.getStreamRevision(), Arrays.asList(attempt));
        this.clearChanges();
    }

    //virtual
    protected Commit buildCommitAttempt(UUID commitId) {
        logger.debug(Resources.BuildingCommitAttempt(), commitId, this.streamId);
        return new Commit(this.streamId,
                          this.streamRevision + this.events.size(),
                          commitId,
                          this.commitSequence + 1,
                          SystemTime.utcNow(),
                          this.uncommittedHeaders,
                          this.events);
    }

    //virtual
    @Override
    public void clearChanges() {
        logger.debug(Resources.ClearingUncommittedChanges(), this.streamId);
        this.events.clear();
        this.uncommittedHeaders.clear();
    }


    @Override
    public int getCommitSequence() {
        return commitSequence;
    }

    @Override
    public UUID getStreamId() {
        return streamId;
    }

    @Override
    public int getStreamRevision() {
        return streamRevision;
    }

}