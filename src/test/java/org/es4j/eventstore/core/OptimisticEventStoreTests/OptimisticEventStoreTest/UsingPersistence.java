package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.OptimisticEventStore;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;


public abstract class UsingPersistence {

    protected static final UUID streamId = UUID.randomUUID();

    public List<IPipelineHook>  pipelineHooks;

    @Mock
    public  IPersistStreams      persistence;

    @UnderTest
    protected OptimisticEventStore store;

    public void establishContext() {
        persistence   = mock(IPersistStreams.class);
        pipelineHooks = Arrays.asList(mock(IPipelineHook.class));
        store         = new OptimisticEventStore(persistence, pipelineHooks);
    };

    public void cleanupEverything() {
        //streamId = UUID.randomUUID();
    }

    protected static Commit buildCommitStub(UUID commitId) {
        return new Commit(streamId,
                          1, commitId, 1, SystemTime.utcNow(), null, null);
    }

    protected static Commit buildCommitStub(int streamRevision, int commitSequence) {
        List<EventMessage> events = Arrays.asList(new EventMessage(""));
        return new Commit(streamId,
                          streamRevision, UUID.randomUUID(), commitSequence, SystemTime.utcNow(), null, events);
    }

    protected static Commit buildCommitStub(UUID commitId, int streamRevision, int commitSequence) {
        List<EventMessage> events = Arrays.asList(new EventMessage(""));
        return new Commit(streamId,
                          streamRevision, commitId, commitSequence, SystemTime.utcNow(), null, events);
    }
}



/*
public abstract class using_persistence
{
    protected static Guid streamId = Guid.NewGuid();
    protected static Mock<IPersistStreams> persistence;
    protected static OptimisticEventStore store;
    protected static List<Mock<IPipelineHook>> pipelineHooks;

    Establish context = () =>
    {
        persistence = new Mock<IPersistStreams>();
        pipelineHooks = new List<Mock<IPipelineHook>>();

        store = new OptimisticEventStore(persistence.Object, pipelineHooks.Select(x => x.Object));
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
        return new Commit(streamId, streamRevision, Guid.NewGuid(), commitSequence, SystemTime.UtcNow,null,events);
    }
    protected static Commit BuildCommitStub(Guid commitId, int streamRevision, int commitSequence)
    {
        var events = new[] { new EventMessage() }.ToList();
        return new Commit(streamId, streamRevision, commitId, commitSequence, SystemTime.UtcNow, null, events);
    }
}
 */

