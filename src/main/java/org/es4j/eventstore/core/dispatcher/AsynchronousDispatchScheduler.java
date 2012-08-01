package org.es4j.eventstore.core.dispatcher;

import java.util.concurrent.ExecutorService;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.Resources;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;
//using System.Threading;
//using Persistence;


public class AsynchronousDispatchScheduler extends SynchronousDispatchScheduler {

    private static final ILog logger = LogFactory.buildLogger(AsynchronousDispatchScheduler.class);
    private final ExecutorService threadPool;

    //public AsynchronousDispatchScheduler(IDispatchCommits dispatcher, IPersistStreams persistence) {
    //    super(dispatcher, persistence);
    //}
    public AsynchronousDispatchScheduler(IDispatchCommits dispatcher,
                                         IPersistStreams  persistence,
                                         ExecutorService  threadPool) {
        super(dispatcher, persistence, true);
        this.threadPool = threadPool;

        this.start();
    }

    @Override // override
    public void scheduleDispatch(final Commit commit) {
        logger.info(Resources.SchedulingDelivery(), commit.getCommitId());

        //ThreadPool.queueUserWorkItem(this/*x => this.callback(commit)*/);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                callback(commit);
            }
        });
    }

    private void callback(Commit commit) {
        super.scheduleDispatch(commit);
    }
}