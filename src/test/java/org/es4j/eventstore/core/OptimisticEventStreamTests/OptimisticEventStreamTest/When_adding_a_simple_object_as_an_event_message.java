package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.messaging.api.EventMessage;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_adding_a_simple_object_as_an_event_message {

    @Before
    public void setUpClass() throws Exception {

        // Given
        UUID streamId    = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);

        // When
        stream.add(new EventMessage(myEvent));
    }

    @Test
    public void should_add_the_uncommited_event_to_the_set_of_uncommitted_events() {

        // Then
        assertThat(stream.getUncommittedEvents().size(), is(1));
    }

    @Test
    public void should_wrap_the_uncommited_event_in_an_EventMessage_object() {

        // Then
        assertEquals(stream.getUncommittedEvents().iterator().next().getBody(), myEvent);
    }

    OptimisticEventStream stream;

    static final String myEvent = "some event data";
}



/*
[Subject("OptimisticEventStream")]
public class when_adding_a_simple_object_as_an_event_message : on_the_event_stream
{
    const string MyEvent = "some event data";

    Because of = () =>
        stream.Add(new EventMessage { Body = MyEvent });

    It should_add_the_uncommited_event_to_the_set_of_uncommitted_events = () =>
        stream.UncommittedEvents.Count.ShouldEqual(1);

    It should_wrap_the_uncommited_event_in_an_EventMessage_object = () =>
        stream.UncommittedEvents.First().Body.ShouldEqual(MyEvent);
}
*/