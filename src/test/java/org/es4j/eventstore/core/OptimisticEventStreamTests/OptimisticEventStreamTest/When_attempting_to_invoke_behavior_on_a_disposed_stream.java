package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
//import org.es4j.dotnet.exceptions.ObjectDisposedException;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.exceptions.ObjectDisposedException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_attempting_to_invoke_behavior_on_a_disposed_stream {

    @UnderTest
    protected static OptimisticEventStream stream;

    @Rule
    public static ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {

        // Given
        UUID streamId    = UUID.randomUUID();
        ICommitEvents persistence = mock(ICommitEvents.class);
	stream = new OptimisticEventStream(streamId, persistence);
        stream.close();
        thrown.expect(ObjectDisposedException.class);
    }

    @Test
    public void should_throw_a_ObjectDisposedException() {

        // When
        stream.commitChanges(UUID.randomUUID());

        // Then
        //thrown.ShouldBeOfType<ObjectDisposedException>();
    }
}


/*
[Subject("OptimisticEventStream")]
public class when_attempting_to_invoke_behavior_on_a_disposed_stream : on_the_event_stream
{
    static Exception thrown;

    Establish context = () =>
        stream.Dispose();

    Because of = () =>
        thrown = Catch.Exception(() => stream.CommitChanges(Guid.NewGuid()));

    It should_throw_a_ObjectDisposedException = () =>
        thrown.ShouldBeOfType<ObjectDisposedException>();
}
 */