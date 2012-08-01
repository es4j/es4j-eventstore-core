package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;

/**
 *
 */
public abstract class OnTheEventStream {

    protected static final int defaultStreamRevision = 1;
    protected static final int defaultCommitSequence = 1;

    //protected static UUID streamId = UUID.randomUUID();

    //@UnderTest
    //protected static OptimisticEventStream stream;

    //@Mock
    //protected static ICommitEvents persistence;

    //void establishContext() {
    //    persistence = mock(ICommitEvents.class);
    //    stream      = new OptimisticEventStream(streamId, persistence);
    //};

    //void cleanupContext() {
    //    streamId = UUID.randomUUID();
    //}

    protected static Commit buildCommitStub(UUID streamId, int revision, int sequence, int eventCount) {
        List<EventMessage> events = new ArrayList<EventMessage>(eventCount);
	for (int i = 0; i < eventCount; i++) {
	    events.add(new EventMessage(""));
        }
	return new Commit(streamId, revision, UUID.randomUUID(), sequence, SystemTime.utcNow(), null, events);
    }

    protected <T> T theFirst(Collection<T> list) {
        return ((list.size() > 0)? list.iterator().next() : null);
     }

    protected <T> T theLast(Collection<T> list) {
        return (T)((list.size() > 0)? list.toArray()[list.size() - 1] : null);
    }
}




/*
public abstract class on_the_event_stream
{
    protected const int DefaultStreamRevision = 1;
    protected const int DefaultCommitSequence = 1;
    protected static Guid streamId = Guid.NewGuid();
    protected static OptimisticEventStream stream;
    protected static Mock<ICommitEvents> persistence;

    Establish context = () =>
    {
        persistence = new Mock<ICommitEvents>();
	stream = new OptimisticEventStream(streamId, persistence.Object);
    };

    Cleanup cleanup = () =>
	streamId = Guid.NewGuid();

    protected static Commit BuildCommitStub(int revision, int sequence, int eventCount)
    {
        var events = new List<EventMessage>(eventCount);
	for (var i = 0; i < eventCount; i++)
	    events.Add(new EventMessage());

	return new Commit(streamId, revision, Guid.NewGuid(), sequence, SystemTime.UtcNow, null, events);
    }
}
*/