package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.api.IEventStream;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_an_empty_changeset {

    @UnderTest
    IEventStream stream; // = new OptimisticEventStream(streamId, persistence);

    @Mock
    static ICommitEvents persistence;

    @Before
    public void given_when() {

        // Given
        UUID streamId = UUID.randomUUID();
        persistence = mock(ICommitEvents.class);
        stream      = new OptimisticEventStream(streamId, persistence);

        // When
        stream.commitChanges(UUID.randomUUID());
    }

    @Test
    public void should_not_call_the_underlying_infrastructure() {

        // Then
        verify(persistence, times(0)).commit(any(Commit.class));
    }

    @Test
    public void should_not_increment_the_current_stream_revision() {

        // Then
        assertThat(stream.getStreamRevision(), is(0));
    }

    @Test
    public void should_not_increment_the_current_commit_sequence() {

        // Then
        assertThat(stream.getCommitSequence(), is(0));
    }
}



/*
[Subject("OptimisticEventStream")]
public class when_committing_an_empty_changeset : on_the_event_stream
{
    Because of = () =>
        stream.CommitChanges(Guid.NewGuid());

    It should_not_call_the_underlying_infrastructure = () =>
        persistence.Verify(x => x.Commit(Moq.It.IsAny<Commit>()), Times.Never());

    It should_not_increment_the_current_stream_revision = () =>
        stream.StreamRevision.ShouldEqual(0);

    It should_not_increment_the_current_commit_sequence = () =>
        stream.CommitSequence.ShouldEqual(0);
}
*/

