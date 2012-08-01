package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static junit.framework.Assert.assertEquals;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.api.IEventStream;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_after_another_thread_or_process_has_moved_the_stream_head extends OnTheEventStream {

    @UnderTest
    IEventStream stream; // = new OptimisticEventStream(streamId, persistence);

    @Rule
    public static final ExpectedException thrown = ExpectedException.none();

    @Mock
    ICommitEvents persistence;

    static final UUID         streamId           = UUID.randomUUID();
    static final int          streamRevision     = 1;
    static final List<Commit> committed          = Arrays.asList(buildCommitStub(streamId, 1, 1, 1));
    static final List<Commit> discoveredOnCommit = Arrays.asList(buildCommitStub(streamId, 3, 2, 2));
    static final EventMessage uncommitted        = new EventMessage("");
    static       Commit       constructed;

    @Before
    public void setUp() throws Exception {

        // Given
        persistence = mock(ICommitEvents.class);
        willThrow(new ConcurrencyException(/*anyString()*/)).given(persistence).commit(any(Commit.class));
        given(persistence.getFrom(streamId, streamRevision, Integer.MAX_VALUE)).willReturn(committed);
        given(persistence.getFrom(streamId, streamRevision+1, Integer.MAX_VALUE)).willReturn(discoveredOnCommit);

        stream = new OptimisticEventStream(streamId, persistence, streamRevision, Integer.MAX_VALUE);
        stream.add(uncommitted);

        thrown.expect(ConcurrencyException.class);

        // When
        //thrown = Catch.Exception(() =>
        stream.commitChanges(UUID.randomUUID());
    }

    @Test
    public void should_throw_a_ConcurrencyException() {
        // Then
        //thrown.ShouldBeOfType<ConcurrencyException>();
    }

    @Test
    public void should_query_the_underlying_storage_to_discover_the_new_commits() {
        // Then
        verify(persistence, times(1)).getFrom(streamId, streamRevision+1, Integer.MAX_VALUE);
    }

    @Test
    public void should_update_the_stream_revision_accordingly() {
        // Then
        assertEquals(stream.getStreamRevision(),
                     theFirst(discoveredOnCommit).getStreamRevision());
    }

    @Test
    public void should_update_the_commit_sequence_accordingly() {
        // Then
        assertEquals(stream.getCommitSequence(),
                     theFirst(discoveredOnCommit).getCommitSequence());
    }

    @Test
    public void should_add_the_newly_discovered_committed_events_to_the_set_of_committed_events_accordingly() {
        // Then
        assertEquals(stream.getCommittedEvents().size(),
                     theFirst(discoveredOnCommit).getEvents().size() + 1);
    }
}





/*
[Subject("OptimisticEventStream")]
public class when_committing_after_another_thread_or_process_has_moved_the_stream_head : on_the_event_stream
{
    const int StreamRevision = 1;
    private static readonly Commit[] Committed = new[] { BuildCommitStub(1, 1, 1) };
    static readonly EventMessage uncommitted = new EventMessage { Body = string.Empty };
    static readonly Commit[] DiscoveredOnCommit = new[] { BuildCommitStub(3, 2, 2) };
    static Commit constructed;
    static Exception thrown;

    Establish context = () =>
    {
        persistence
	    .Setup(x => x.Commit(Moq.It.IsAny<Commit>()))
	    .Throws(new ConcurrencyException());
        persistence
            .Setup(x => x.GetFrom(streamId, StreamRevision, int.MaxValue))
            .Returns(Committed);
        persistence
            .Setup(x => x.GetFrom(streamId, StreamRevision + 1, int.MaxValue))
            .Returns(DiscoveredOnCommit);

        stream = new OptimisticEventStream(streamId, persistence.Object, StreamRevision, int.MaxValue);
        stream.Add(uncommitted);
    };

    Because of = () =>
        thrown = Catch.Exception(() => stream.CommitChanges(Guid.NewGuid()));

    It should_throw_a_ConcurrencyException = () =>
        thrown.ShouldBeOfType<ConcurrencyException>();

    It should_query_the_underlying_storage_to_discover_the_new_commits = () =>
        persistence.Verify(x => x.GetFrom(streamId, StreamRevision + 1, int.MaxValue), Times.Once());

    It should_update_the_stream_revision_accordingly = () =>
        stream.StreamRevision.ShouldEqual(DiscoveredOnCommit[0].StreamRevision);

    It should_update_the_commit_sequence_accordingly = () =>
        stream.CommitSequence.ShouldEqual(DiscoveredOnCommit[0].CommitSequence);

    It should_add_the_newly_discovered_committed_events_to_the_set_of_committed_events_accordingly = () =>
        stream.CommittedEvents.Count.ShouldEqual(DiscoveredOnCommit[0].Events.Count + 1);
}
 */

