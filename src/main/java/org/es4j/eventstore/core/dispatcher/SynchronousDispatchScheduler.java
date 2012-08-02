package org.es4j.eventstore.core.dispatcher;

import org.es4j.dotnet.GC;
//import org.es4j.dotnet.exceptions.ObjectDisposedException;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.dispatcher.IScheduleDispatches;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.Resources;
import org.es4j.exceptions.ObjectDisposedException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;

//using Logging;
//using Persistence;


public class SynchronousDispatchScheduler implements IScheduleDispatches {

    private static final ILog      logger = LogFactory.buildLogger(SynchronousDispatchScheduler.class);
    private final IDispatchCommits dispatcher;
    private final IPersistStreams  persistence;
    private boolean                disposed;

    protected SynchronousDispatchScheduler(IDispatchCommits dispatcher, IPersistStreams persistence, boolean noStart) {
        this.dispatcher  = dispatcher;
        this.persistence = persistence;

        logger.info(Resources.StartingDispatchScheduler());
        //this.start();
    }

    public SynchronousDispatchScheduler(IDispatchCommits dispatcher, IPersistStreams persistence) {
        this.dispatcher  = dispatcher;
        this.persistence = persistence;

        logger.info(Resources.StartingDispatchScheduler());
        this.start();
    }

    @Override
    public void close() throws Exception {
        dispose();
    }
    @Override
    public void dispose() throws Exception {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    //virtual
    protected void dispose(boolean disposing) throws Exception {
        if (!disposing || this.disposed) {
            return;
        }

        logger.debug(Resources.ShuttingDownDispatchScheduler());
        this.disposed = true;
        this.dispatcher .close(); // .dispose();
        this.persistence.close(); //.dispose();
    }

    protected void start() {
        logger.debug(Resources.InitializingPersistence());
        this.persistence.initialize();

        logger.debug(Resources.GettingUndispatchedCommits());
        for (Commit commit : this.persistence.getUndispatchedCommits()) {
            this.scheduleDispatch(commit);
        }
    }

    // virtual
    @Override
    public void scheduleDispatch(Commit commit) {
        this.dispatchImmediately(commit);
        this.markAsDispatched(commit);
    }

    private void dispatchImmediately(Commit commit) {
        try {
            logger.info(Resources.SchedulingDispatch(), commit.getCommitId());
            this.dispatcher.dispatch(commit);
        }
        catch (Exception ex) {
            logger.error(Resources.UnableToDispatch(), this.dispatcher.getClass().getName(), commit.getCommitId());
            throw ex;
        }
    }

    private void markAsDispatched(Commit commit) {
        try {
            logger.info(Resources.MarkingCommitAsDispatched(), commit.getCommitId());
            this.persistence.markCommitAsDispatched(commit);
        }
        catch (ObjectDisposedException ex) {
            logger.warn(Resources.UnableToMarkDispatched(), commit.getCommitId());
        }
    }
}
