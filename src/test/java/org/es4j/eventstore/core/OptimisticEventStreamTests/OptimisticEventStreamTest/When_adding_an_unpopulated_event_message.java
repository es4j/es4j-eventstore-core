package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.messaging.api.EventMessage;
import org.hamcrest.core.Is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


//[Subject("OptimisticEventStream")]
@RunWith(MockitoJUnitRunner.class)
public class When_adding_an_unpopulated_event_message {

    @Before
    public void setUp() throws Exception {

        // Given
        UUID          streamId    = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);

        // When
        thrown.expect(IllegalArgumentException.class);
        EventMessage event = new EventMessage(null);
        //stream.add(event);
    }

    @Test
    public void should_be_ignored() {
        // Then
        assertThat(stream.getUncommittedEvents().size(), Is.is(0));
    }

    protected static OptimisticEventStream stream;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}

/*
[Subject("OptimisticEventStream")]
public class when_adding_an_unpopulated_event_message : on_the_event_stream
{
    Because of = () =>
        stream.Add(new EventMessage { Body = null });

    It should_be_ignored = () =>
        stream.UncommittedEvents.ShouldBeEmpty();
}
*/