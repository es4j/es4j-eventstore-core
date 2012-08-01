package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_the_head_event_revision_is_less_than_the_max_desired_revision extends OnTheEventStream {

    @UnderTest
    OptimisticEventStream stream;

    static final UUID     streamId = UUID.randomUUID();

    static final int          eventsPerCommit = 2;
    static final List<Commit> committed = Arrays.asList(
        buildCommitStub(streamId, 2, 1, eventsPerCommit),  // 1-2
        buildCommitStub(streamId, 4, 2, eventsPerCommit),  // 3-4
        buildCommitStub(streamId, 6, 3, eventsPerCommit),  // 5-6
        buildCommitStub(streamId, 8, 3, eventsPerCommit)); // 7-8

    @Before
    public void given_when() {

        // Given
        ICommitEvents persistence = mock(ICommitEvents.class);
        given(persistence.getFrom(streamId, 0, Integer.MAX_VALUE)).willReturn(committed);

        // When
        stream = new OptimisticEventStream(streamId, persistence, 0, Integer.MAX_VALUE);
    }

    @Test
    public void should_set_the_stream_revision_to_the_revision_of_the_most_recent_event() {
        // Then
        assertThat(stream.getStreamRevision(), is(theLast(committed).getStreamRevision()));
    }
}



/*
[Subject("OptimisticEventStream")]
public class when_the_head_event_revision_is_less_than_the_max_desired_revision : on_the_event_stream
{
    static readonly int EventsPerCommit = 2.Events();
    static readonly Commit[] Committed = new[]
    {
	BuildCommitStub(2, 1, EventsPerCommit), // 1-2
	BuildCommitStub(4, 2, EventsPerCommit), // 3-4
	BuildCommitStub(6, 3, EventsPerCommit), // 5-6
	BuildCommitStub(8, 3, EventsPerCommit)  // 7-8
    };

    Establish context = () =>
        persistence.Setup(x => x.GetFrom(streamId, 0, int.MaxValue)).Returns(Committed);

    Because of = () =>
        stream = new OptimisticEventStream(streamId, persistence.Object, 0, int.MaxValue);

    It should_set_the_stream_revision_to_the_revision_of_the_most_recent_event = () =>
        stream.StreamRevision.ShouldEqual(Committed.Last().StreamRevision);
}
*/