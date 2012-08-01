package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.messaging.api.EventMessage;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_adding_a_fully_populated_event_message {

    @Test
    public void should_add_the_event_to_the_set_of_uncommitted_events() {

        // Given
        UUID streamId    = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);
        EventMessage event = new EventMessage("populated");

        // When
        stream.add(event);

        // Then
        assertThat(stream.getUncommittedEvents().size(), is(1));
    }

    OptimisticEventStream stream;
}





/*
[Subject("OptimisticEventStream")]
public class when_adding_a_fully_populated_event_message : on_the_event_stream
{
    Because of = () =>
        stream.Add(new EventMessage { Body = "populated" });

    It should_add_the_event_to_the_set_of_uncommitted_events = () =>
        stream.UncommittedEvents.Count.ShouldEqual(1);
}
*/