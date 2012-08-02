package org.es4j.eventstore.core.persistence.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.DuplicateCommitException;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.api.persistence.StreamHead;
import org.es4j.eventstore.core.Resources;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.util.DateTime;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;


public class InMemoryPersistenceEngine implements IPersistStreams {

    private static final ILog logger = LogFactory.buildLogger(InMemoryPersistenceEngine.class);

    private final ArrayList<Commit>     commits      = new ArrayList<Commit>();
    private final ArrayList<StreamHead> heads        = new ArrayList<StreamHead>();
    private final ArrayList<Commit>     undispatched = new ArrayList<Commit>();
    private final ArrayList<Snapshot>   snapshots    = new ArrayList<Snapshot>();
    private final Map<UUID, DateTime>   stamps       = new HashMap<UUID, DateTime>();

    private boolean disposed;

    @Override
    public void close() {
        dispose();
    }
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virtual
    protected void dispose(boolean disposing) {
        this.disposed = true;
        logger.info(Resources.DisposingEngine());
    }

    private void ThrowWhenDisposed() {
        if (!this.disposed) {
            return;
        }

        logger.warn(Resources.AlreadyDisposed());
        throw new ObjectDisposedException(Resources.AlreadyDisposed());
    }

    @Override
    public void initialize() {
        logger.info(Resources.InitializingEngine());
    }

    @Override // virtual
    public Iterable<Commit> getFrom(UUID streamId, int minRevision, int maxRevision) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.GettingAllCommitsFromRevision(), streamId, minRevision, maxRevision);

        synchronized (this.commits) {
            List<Commit> list = new ArrayList<Commit>();
            for (Commit commit : this.commits) {
                if ( commit.getStreamId() == streamId
                 &&  commit.getStreamRevision() >= minRevision
                 &&  commit.getStreamRevision() - commit.getEvents().size() + 1 <= maxRevision) {
                    list.add(commit);
                }
            }
            return list;
        }
    }

    @Override  // virtual
    public Iterable<Commit> getFrom(DateTime start) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.GettingAllCommitsFromTime(), start);

        for (Entry<UUID, DateTime> x : this.stamps.entrySet()) {
            if ( ! x.getValue().before(start)) {
                return this.commits.subList(this.commits.indexOf(x.getKey()), this.commits.size());
            }
        }
        return new ArrayList<Commit>();
    }

    @Override //virtual
    public void commit(Commit attempt) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.AttemptingToCommit(), attempt.getCommitId(), attempt.getStreamId(), attempt.getCommitSequence());

        synchronized (this.commits) {
            if (this.commits.contains(attempt)) {
                throw new DuplicateCommitException();
            }

            for (Commit c : this.commits) {
                if (c.getStreamId() == attempt.getStreamId() && c.getStreamRevision() == attempt.getStreamRevision()) {
                    throw new ConcurrencyException();
                }
            }

            this.stamps.put(attempt.getCommitId(), attempt.getCommitStamp());
            this.commits.add(attempt);

            this.undispatched.add(attempt);

            StreamHead head = null; //this.heads.FirstOrDefault(x => x.StreamId == attempt.StreamId);
            for(StreamHead x : this.heads) {
                if(x.getStreamId() == attempt.getStreamId()) {
                    this.heads.remove(head);
                }
            }

            logger.debug(Resources.UpdatingStreamHead(), attempt.getStreamId());
            int snapshotRevision = head == null ? 0 : head.getSnapshotRevision();
            this.heads.add(new StreamHead(attempt.getStreamId(), attempt.getStreamRevision(), snapshotRevision));
        }
    }

    @Override //virtual
    public Iterable<Commit> getUndispatchedCommits() {

        synchronized (this.commits) {
            this.ThrowWhenDisposed();
            logger.debug(Resources.RetrievingUndispatchedCommits(), this.commits.size());

            ArrayList<Commit> list = new ArrayList<Commit>();
            for(Commit commit : this.commits) {
                if(this.undispatched.contains(commit)) {
                    list.add(commit);
                }
            }
            return list; //this.commits.Where(c => this.undispatched.Contains(c));
        }
    }

    @Override // virtual
    public void markCommitAsDispatched(Commit commit) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.MarkingAsDispatched(), commit.getCommitId());

        synchronized (this.commits) {
            this.undispatched.remove(commit);
        }
    }

    @Override // virtual
    public Iterable<StreamHead> getStreamsToSnapshot(int maxThreshold) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.GettingStreamsToSnapshot(), maxThreshold);

        synchronized (this.commits) {
            ArrayList<StreamHead> list = new ArrayList<StreamHead>();
            for(StreamHead x : this.heads) {
                if(x.getHeadRevision() >= x.getSnapshotRevision() + maxThreshold) {
                    list.add(new StreamHead(x.getStreamId(), x.getHeadRevision(), x.getSnapshotRevision()));
                }
            }
            return list;
        }
    }


    @Override // virtual
    public Snapshot getSnapshot(UUID streamId, int maxRevision) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.GettingSnapshotForStream(), streamId, maxRevision);

        synchronized (this.commits) {
            //	return this.snapshots
            //		.Where(x => x.StreamId == streamId && x.StreamRevision <= maxRevision)
            //		.OrderByDescending(x => x.StreamRevision)
            //		.FirstOrDefault();

            int latestRevision = Integer.MIN_VALUE;
            Snapshot snapshot = null;
            for (Snapshot x : this.snapshots) {
                if (x.getStreamId() == streamId && x.getStreamRevision() <= maxRevision) {
                    if(latestRevision < x.getStreamRevision()) {
                        latestRevision = x.getStreamRevision();
                        snapshot = x;
                    }
                }
            }
            return snapshot;
        }
    }


    @Override // virtual
    public boolean addSnapshot(Snapshot snapshot) {
        this.ThrowWhenDisposed();
        logger.debug(Resources.AddingSnapshot(), snapshot.getStreamId(), snapshot.getStreamRevision());

        synchronized (this.commits) {
            //StreamHead currentHead = null; // this.heads.FirstOrDefault(h => h.StreamId == snapshot.StreamId);
            for (StreamHead h : this.heads) {
                if (h.getStreamId() == snapshot.getStreamId()) {
                    this.snapshots.add(snapshot);
                    this.heads.remove(h);
                    this.heads.add(new StreamHead(h.getStreamId(), h.getHeadRevision(), snapshot.getStreamRevision()));
                    return true;
                }
            }
        }
        return false;
    }

    @Override // virtual
    public void purge() {
        this.ThrowWhenDisposed();
        logger.warn(Resources.PurgingStore());

        synchronized (this.commits) {
            this.commits.clear();
            this.snapshots.clear();
            this.heads.clear();
        }
    }

}
