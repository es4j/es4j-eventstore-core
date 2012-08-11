package org.es4j.eventstore.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.*;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using Persistence;


public class OptimisticEventStore implements IStoreEvents, ICommitEvents {

    private static final ILog logger = LogFactory.buildLogger(OptimisticEventStore.class);
    private final IPersistStreams persistence;
    private final Iterable<IPipelineHook> pipelineHooks;

    public OptimisticEventStore(IPersistStreams persistence, Iterable<IPipelineHook> pipelineHooks) {
        if (persistence == null) {
            throw new ArgumentNullException("persistence");
        }
        this.persistence = persistence;
        this.pipelineHooks = (pipelineHooks != null) ? pipelineHooks : new ArrayList<IPipelineHook>();
    }

    @Override
    public void close() throws Exception {
        dispose();
    }

    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    //virtual
    protected void dispose(boolean disposing) {
        if (!disposing) {
            return;
        }

        logger.info(Resources.ShuttingDownStore());
        try {
            this.persistence.close(); //.dispose();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        for (IPipelineHook hook : this.pipelineHooks) {
            try {
                hook.close(); //.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override // virtual
    public IEventStream createStream(UUID streamId) {
        logger.info(Resources.CreatingStream(), streamId);
        return new OptimisticEventStream(streamId, this);
    }

    @Override //virtual
    public IEventStream openStream(UUID streamId, int minRevision, int maxRevision) {
        maxRevision = maxRevision <= 0 ? Integer.MAX_VALUE : maxRevision;

        logger.debug(Resources.OpeningStreamAtRevision(), streamId, minRevision, maxRevision);
        return new OptimisticEventStream(streamId, this, minRevision, maxRevision);
    }

    @Override //virtual
    public IEventStream openStream(Snapshot snapshot, int maxRevision) {
        if (snapshot == null) {
            throw new ArgumentNullException("snapshot");
        }

        logger.debug(Resources.OpeningStreamWithSnapshot(), snapshot.getStreamId(), snapshot.getStreamRevision(), maxRevision);
        maxRevision = maxRevision <= 0 ? Integer.MAX_VALUE : maxRevision;
        return new OptimisticEventStream(snapshot, this, maxRevision);
    }

    /*
     * public virtual IEnumerable<Commit> GetFrom(Guid streamId, int minRevision, int maxRevision) { foreach (var commit
     * in this.persistence.GetFrom(streamId, minRevision, maxRevision)) { var filtered = commit; foreach (var hook in
     * this.pipelineHooks.Where(x => (filtered = x.Select(filtered)) == null)) {
     * Logger.Info(Resources.PipelineHookSkippedCommit, hook.GetType(), commit.CommitId); break; } if (filtered == null)
     * Logger.Info(Resources.PipelineHookFilteredCommit); else yield return filtered; } }
     */
    @Override  // virtual
    public Iterable<Commit> getFrom(UUID streamId, int minRevision, int maxRevision) {
        List<Commit> commits = new LinkedList<Commit>();

        for (Commit commit : this.persistence.getFrom(streamId, minRevision, maxRevision)) {
            Commit filtered = commit;
            for (IPipelineHook hook : this.pipelineHooks) { //.Where(x => (filtered = x.Select(filtered)) == null)) {
                Commit saved = hook.select(filtered);
                if (saved == null) {
                    logger.info(Resources.PipelineHookSkippedCommit(), hook.getClass().getName(), commit.getCommitId());
                    break;
                }
                filtered = saved;
            }

            if (filtered == null) {
                logger.info(Resources.PipelineHookFilteredCommit());
            } else {
                commits.add(filtered); //yield return filtered;
            }
        }
        return commits;
    }

    @Override // virutal
    public void commit(Commit attempt) {
        if (!(CommitExtensionMethods.isValid(attempt)) || CommitExtensionMethods.isEmpty(attempt)) {
            logger.debug(Resources.CommitAttemptFailedIntegrityChecks());
            return;
        }

        for (IPipelineHook hook : this.pipelineHooks) {
            logger.debug(Resources.InvokingPreCommitHooks(), attempt.getCommitId(), hook.getClass().getName());
            if (hook.preCommit(attempt)) {
                continue;
            }

            logger.info(Resources.CommitRejectedByPipelineHook(), hook.getClass().getName(), attempt.getCommitId());
            return;
        }

        logger.info(Resources.CommittingAttempt(), attempt.getCommitId(), attempt.getEvents().size());
        this.persistence.commit(attempt);

        for (IPipelineHook hook : this.pipelineHooks) {
            logger.debug(Resources.InvokingPostCommitPipelineHooks(), attempt.getCommitId(), hook.getClass().getName());
            hook.postCommit(attempt);
        }
    }

    @Override // virtual
    public IPersistStreams advanced() {
        return this.persistence;
    }
}
