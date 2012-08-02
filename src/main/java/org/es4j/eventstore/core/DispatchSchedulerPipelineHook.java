package org.es4j.eventstore.core;

import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.api.dispatcher.IScheduleDispatches;
import org.es4j.eventstore.core.dispatcher.NullDispatcher;
//import org.es4j.eventstore.core.dotnet.GC;
//using Dispatcher;


public class DispatchSchedulerPipelineHook implements IPipelineHook {

    private final IScheduleDispatches scheduler;

    public DispatchSchedulerPipelineHook() {
        this(null);
    }

    public DispatchSchedulerPipelineHook(IScheduleDispatches scheduler) {
        this.scheduler = (scheduler != null)? scheduler : new NullDispatcher(); // serves as a scheduler also
    }

    @Override
    public void close() throws Exception {
        this.dispose();
    }

    @Override
    public void dispose() throws Exception {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virtual
    protected void dispose(boolean disposing) throws Exception {
        this.scheduler.close();  //.dispose();
    }

    @Override
    public Commit select(Commit committed) {
        return committed;
    }

    
    @Override // virtual
    public boolean preCommit(Commit attempt) {
        return true;
    }

    @Override
    public void postCommit(Commit committed) {
        if (committed != null) {
            this.scheduler.scheduleDispatch(committed);
        }
    }
}