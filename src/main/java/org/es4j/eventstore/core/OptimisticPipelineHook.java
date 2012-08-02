package org.es4j.eventstore.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.api.persistence.StorageException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using Persistence;


/// <summary>
/// Tracks the heads of streams to reduce latency by avoiding roundtrips to storage.
/// </summary>
public class OptimisticPipelineHook implements IPipelineHook {

    private static final ILog logger = LogFactory.buildLogger(OptimisticPipelineHook.class);
    private static final int MAXSTREAMSToTOTRACK = 100;
    private final LinkedList<UUID> maxItemsToTrack = new LinkedList<UUID>();
    private final Map<UUID, Commit> heads = new HashMap<UUID, Commit>();
    private final int maxStreamsToTrack;

    public OptimisticPipelineHook() {
        this(MAXSTREAMSToTOTRACK);
    }

    public OptimisticPipelineHook(int maxStreamsToTrack) {
        logger.debug(Resources.TrackingStreams(), maxStreamsToTrack);
        this.maxStreamsToTrack = maxStreamsToTrack;
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

    // virtual
    protected void dispose(boolean disposing) {
        this.heads.clear();
        this.maxItemsToTrack.clear();
    }

    // virtual
    @Override
    public Commit select(Commit committed) {
        this.track(committed);
        return committed;
    }

    // virtual
    @Override
    public boolean preCommit(Commit attempt) {
        logger.debug(Resources.OptimisticConcurrencyCheck(), attempt.getStreamId());

        Commit head = this.getStreamHead(attempt.getStreamId());
        if (head == null) {
            return true;
        }

        if (head.getCommitSequence() >= attempt.getCommitSequence()) {
            throw new ConcurrencyException();
        }

        if (head.getStreamRevision() >= attempt.getStreamRevision()) {
            throw new ConcurrencyException();
        }

        if (head.getCommitSequence() < attempt.getCommitSequence() - 1) {
            throw new StorageException(); // beyond the end of the stream
        }
        if (head.getStreamRevision() < attempt.getStreamRevision() - attempt.getEvents().size()) {
            throw new StorageException(); // beyond the end of the stream
        }
        logger.debug(Resources.NoConflicts(), attempt.getStreamId());
        return true;
    }

    // virtual
    @Override
    public void postCommit(Commit committed) {
        this.track(committed);
    }

    // virtual
    public void track(Commit committed) {
        if (committed == null) {
            return;
        }

        synchronized (this.maxItemsToTrack) {
            this.updateStreamHead(committed);
            this.trackUpToCapacity(committed);
        }
    }

    private void updateStreamHead(Commit committed) {
        Commit head = this.getStreamHead(committed.getStreamId());
        if (alreadyTracked(head)) {
            this.maxItemsToTrack.remove(committed.getStreamId());
        }

        head = (head != null) ? head : committed;
        head = head.getStreamRevision() > committed.getStreamRevision() ? head : committed;

        this.heads.put(committed.getStreamId(), head);
    }

    private static boolean alreadyTracked(Commit head) {
        return head != null;
    }

    private void trackUpToCapacity(Commit committed) {
        logger.verbose(Resources.TrackingCommit(), committed.getCommitSequence(), committed.getStreamId());
        this.maxItemsToTrack.addFirst(committed.getStreamId());
        if (this.maxItemsToTrack.size() <= this.maxStreamsToTrack) {
            return;
        }

        UUID expired = this.maxItemsToTrack.getLast();
        logger.verbose(Resources.NoLongerTrackingStream(), expired);

        this.heads.remove(expired);
        this.maxItemsToTrack.removeLast();
    }

    // virtual
    public boolean contains(Commit attempt) {
        return this.getStreamHead(attempt.getStreamId()) != null;
    }

    private Commit getStreamHead(UUID streamId) {
        Commit head;
        synchronized (this.maxItemsToTrack) {
            head = (heads.containsKey(streamId))? heads.get(streamId) : null;
        }
        return head;
    }
}