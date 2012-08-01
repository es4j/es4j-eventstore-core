package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_attempting_to_modify_the_event_collections {

    @UnderTest
    private OptimisticEventStream stream;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        // Given
        UUID streamId = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);
        thrown.expect(UnsupportedOperationException.class);
    }

    @Test
    public void should_throw_an_exception_when_adding_to_the_committed_collection() {

        // When
        stream.getCommittedEvents().add(null);

        // Then
        // no action
    }


    @Test
    public void should_throw_an_exception_when_adding_to_the_uncommitted_collection() {

        // When
        stream.getUncommittedEvents().add(null); //.ShouldBeOfType<NotSupportedException>();
    }

    @Test
    public void should_throw_an_exception_when_clearing_the_committed_collection() {
        // When
        stream.getCommittedEvents().clear(); //).ShouldBeOfType<NotSupportedException>();

        // Then
        // no action
    }

    @Test
    public void should_throw_an_exception_when_clearing_the_uncommitted_collection() {
        // When
        stream.getUncommittedEvents().clear(); //).ShouldBeOfType<NotSupportedException>();

        // Then
        // no action
    }

    @Test
    public void should_throw_an_exception_when_removing_from_the_committed_collection() {
       // Wheb
        stream.getCommittedEvents().remove(null); //).ShouldBeOfType<NotSupportedException>();

        // Then
        // no action
    }

    @Test
    public void should_throw_an_exception_when_removing_from_the_uncommitted_collection() {
        //Catch.Exception(() =>
        stream.getUncommittedEvents().remove(null); //).ShouldBeOfType<NotSupportedException>();

        // Then
        // no action
    }
}






/*
[Subject("OptimisticEventStream")]
public class when_attempting_to_modify_the_event_collections : on_the_event_stream
{
    It should_throw_an_exception_when_adding_to_the_committed_collection = () =>
        Catch.Exception(() => stream.CommittedEvents.Add(null)).ShouldBeOfType<NotSupportedException>();

    It should_throw_an_exception_when_adding_to_the_uncommitted_collection = () =>
        Catch.Exception(() => stream.UncommittedEvents.Add(null)).ShouldBeOfType<NotSupportedException>();

    It should_throw_an_exception_when_clearing_the_committed_collection = () =>
        Catch.Exception(() => stream.CommittedEvents.Clear()).ShouldBeOfType<NotSupportedException>();

    It should_throw_an_exception_when_clearing_the_uncommitted_collection = () =>
        Catch.Exception(() => stream.UncommittedEvents.Clear()).ShouldBeOfType<NotSupportedException>();

    It should_throw_an_exception_when_removing_from_the_committed_collection = () =>
        Catch.Exception(() => stream.CommittedEvents.Remove(null)).ShouldBeOfType<NotSupportedException>();

    It should_throw_an_exception_when_removing_from_the_uncommitted_collection = () =>
        Catch.Exception(() => stream.UncommittedEvents.Remove(null)).ShouldBeOfType<NotSupportedException>();
}
 */
