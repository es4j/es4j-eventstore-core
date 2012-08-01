package org.es4j.eventstore.core.OptimisticCommitHookTests.OptimisticCommitHookTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.es4j.eventstore.api.Commit;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime_DELETE;
//import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;


public abstract class UsingCommitHooks {

    protected static UUID streamId = UUID.randomUUID();
    //protected static OptimisticPipelineHook hook;

    public void establishContext() {
        streamId = UUID.randomUUID();
        //hook     = new OptimisticPipelineHook();
    };

    public void cleanupEverything () {
        streamId = UUID.randomUUID();
    }

    protected static Commit buildCommitStub(UUID commitId) {
        return new Commit(streamId, 1, commitId, 1, SystemTime.utcNow(), null, null);
    }

    protected static Commit buildCommitStub(int streamRevision, int commitSequence) {
        List<EventMessage> events = Arrays.asList(new EventMessage(""));
        return new Commit(streamId, streamRevision, UUID.randomUUID(), commitSequence, SystemTime.utcNow(), null, events);
    }

    protected static Commit buildCommitStub(UUID commitId, int streamRevision, int commitSequence) {
        List<EventMessage> events = Arrays.asList(new EventMessage(""));
        return new Commit(streamId, streamRevision, commitId, commitSequence, SystemTime.utcNow(), null, events);
    }

}


/*
public abstract class using_commit_hooks
{
    protected static Guid streamId = Guid.NewGuid();
    protected static OptimisticPipelineHook hook;

    Establish context = () =>
    {
			streamId = Guid.NewGuid();
			hook = new OptimisticPipelineHook();
    };

    Cleanup everything = () =>
        streamId = Guid.NewGuid();

    protected static Commit BuildCommitStub(Guid commitId)
    {
			return new Commit(streamId, 1, commitId, 1, SystemTime.UtcNow, null, null);
    }
    protected static Commit BuildCommitStub(int streamRevision, int commitSequence)
    {
			var events = new[] { new EventMessage() }.ToList();
			return new Commit(streamId, streamRevision, Guid.NewGuid(), commitSequence, SystemTime.UtcNow, null, events);
    }
    protected static Commit BuildCommitStub(Guid commitId, int streamRevision, int commitSequence)
    {
			var events = new[] { new EventMessage() }.ToList();
			return new Commit(streamId, streamRevision, commitId, commitSequence, SystemTime.UtcNow, null, events);
    }
}
*/
