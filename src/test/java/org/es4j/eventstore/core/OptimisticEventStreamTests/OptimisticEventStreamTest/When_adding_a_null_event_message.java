package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.UUID;
import static junit.framework.Assert.assertTrue;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_adding_a_null_event_message /*extends OnTheEventStream*/ {

    protected static final UUID streamId = UUID.randomUUID();

    protected OptimisticEventStream stream;

    @Before
    public void given_when() {

        // Given
        ICommitEvents persistence = mock(ICommitEvents.class);
        stream = new OptimisticEventStream(streamId, persistence);

        // When
        stream.add(null);
    }

    @Test
    public void should_be_ignored() {
        // Given
        ICommitEvents persistence = mock(ICommitEvents.class);
        stream = new OptimisticEventStream(streamId, persistence);

        // When
        stream.add(null);

        // Then
        assertTrue(stream.getUncommittedEvents().isEmpty());
    }
}


/*
[Subject("OptimisticEventStream")]
public class when_adding_a_null_event_message : on_the_event_stream
{
    Because of = () =>
        stream.Add(null);

    It should_be_ignored = () =>
        stream.UncommittedEvents.ShouldBeEmpty();
}
*/