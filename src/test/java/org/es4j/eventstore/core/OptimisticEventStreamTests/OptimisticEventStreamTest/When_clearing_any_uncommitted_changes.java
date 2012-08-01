package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.UUID;
import static junit.framework.Assert.assertTrue;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_clearing_any_uncommitted_changes {

    @UnderTest
    private OptimisticEventStream stream;

    @Before
    public void setUp() throws Exception {

        // Given
        UUID streamId = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);
        stream.add(new EventMessage(""));

        // When
        stream.clearChanges();
    }

    @Test
    public void should_clear_all_uncommitted_events() {
        // Then
        assertTrue(stream.getUncommittedEvents().isEmpty()); //.ShouldEqual(0);
    }
}






/*
[Subject("OptimisticEventStream")]
public class when_clearing_any_uncommitted_changes : on_the_event_stream
{
    Establish context = () =>
        stream.Add(new EventMessage { Body = string.Empty });

    Because of = () =>
	stream.ClearChanges();

    It should_clear_all_uncommitted_events = () =>
        stream.UncommittedEvents.Count.ShouldEqual(0);
}
*/

