package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_adding_multiple_populated_event_messages {

    @UnderTest
    protected static OptimisticEventStream stream;

    @BeforeClass
    public static void setUpClass() throws Exception {

        // Given
        UUID streamId    = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);

        // When
        stream.add(new EventMessage("populated"));
        stream.add(new EventMessage("also populated"));
    }

    @Test
    public void should_add_the_event_to_the_set_of_uncommitted_events() {

        // Then
        Assert.assertThat(stream.getUncommittedEvents().size(), is(2));
    }

}





/*
[Subject("OptimisticEventStream")]
public class when_adding_multiple_populated_event_messages : on_the_event_stream
{
    Because of = () =>
    {
        stream.Add(new EventMessage { Body = "populated" });
        stream.Add(new EventMessage { Body = "also populated" });
    };

    It should_add_all_of_the_events_provided_to_the_set_of_uncommitted_events = () =>
        stream.UncommittedEvents.Count.ShouldEqual(2);
}
*/