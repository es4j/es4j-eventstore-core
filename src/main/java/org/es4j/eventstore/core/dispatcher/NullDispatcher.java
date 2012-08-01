package org.es4j.eventstore.core.dispatcher;

import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.dispatcher.IScheduleDispatches;
import org.es4j.eventstore.core.Resources;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;


public final class NullDispatcher implements IScheduleDispatches, IDispatchCommits {

    private static final ILog logger = LogFactory.buildLogger(NullDispatcher.class);

    @Override
    public void close() {
        dispose_FORNOW();
    }
    @Override
    public void dispose_FORNOW() {
        logger.debug(Resources.ShuttingDownDispatcher());
        GC.suppressFinalize(this);
    }

    @Override
    public void scheduleDispatch(Commit commit) {
        logger.info(Resources.SchedulingDispatch(), commit.getCommitId());
        this.dispatch(commit);
    }

    @Override
    public void dispatch(Commit commit) {
        logger.info(Resources.DispatchingToDevNull());
    }
}
