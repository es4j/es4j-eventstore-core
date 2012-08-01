package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IEventStream;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.IterableCounter;
import org.es4j.eventstore.core.OptimisticEventStore;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_a_stream_from_a_snapshot_that_is_at_the_revision_of_the_stream_head { // extends UsingPersistence {

    @Before
    public void setUp() {

        Snapshot snapshot;
        Commit               commit;
        List<EventMessage>   events;
        IPipelineHook        pipelineHook;
        IPersistStreams      persistence;
        List<IPipelineHook>  pipelineHooks;

        // Given
        headStreamRevision = 42;
        headCommitSequence = 15;
        streamId           = UUID.randomUUID();

        events    = Arrays.asList(new EventMessage("Test Event"));
        commit    = new Commit(streamId, headStreamRevision, UUID.randomUUID(), headCommitSequence, SystemTime.utcNow(), null, events);
        committed = new IterableCounter(Arrays.asList(commit));
        snapshot  = new Snapshot(streamId, headStreamRevision, "snapshot");

        persistence = mock(IPersistStreams.class);
        given(persistence.getFrom(streamId, headStreamRevision, Integer.MAX_VALUE)).willReturn(committed);

        pipelineHook  = mock(IPipelineHook.class);
        pipelineHooks = Arrays.asList(pipelineHook);

        // Under Test
        OptimisticEventStore store = new OptimisticEventStore(persistence, pipelineHooks);


        // When
        stream = store.openStream(snapshot, Integer.MAX_VALUE);
    }

    @Test
    public void should_return_a_stream_with_the_correct_stream_identifier() {

        // Then
        assertThat(stream.getStreamId(), is(streamId));
    }

    @Test
    public void should_return_a_stream_with_revision_of_the_stream_head() {

        // Then
        assertThat(stream.getStreamRevision(), is(headStreamRevision));
    }

    @Test
    public void should_return_a_stream_with_a_commit_sequence_of_the_stream_head() {

        // Then
        assertThat(stream.getCommitSequence(), is(headCommitSequence));
    }

    @Test
    public void should_return_a_stream_with_no_committed_events() {

        // Then
        assertThat(stream.getCommittedEvents().size(), is(0));
    }

    @Test
    public void should_return_a_stream_with_no_uncommitted_events() {

        // Then
        assertThat(stream.getUncommittedEvents().size(), is(0));
    }

    @Test
    public void should_only_enumerate_the_set_of_commits_once() {

        // Then
        assertThat(committed.getIteratorCallCount(), is(1));
    }

/*
    Guid streamId; // = Guid.newGuid();

    IterableCounter committed; // = new IterableCounter(Arrays.asList(buildCommitStub(headStreamRevision, headCommitSequence)));

    int headStreamRevision;
    int headCommitSequence;
    Snapshot snapshot; // = new Snapshot(streamId, headStreamRevision, "snapshot");
    Commit             commit;
    IEventStream stream;
    List<EventMessage> events;
    IPipelineHook pipelineHook;

    @Mock
    public  IPersistStreams      persistence;

    //@Mock
    public List<IPipelineHook>  pipelineHooks;

    @UnderTest
    protected OptimisticEventStore store;
*/

    int  headStreamRevision;
    int  headCommitSequence;
    UUID streamId;

    IterableCounter committed;

    @Mock
    IEventStream stream;
}



/*
[Subject("OptimisticEventStore")]
public class when_opening_a_stream_from_a_snapshot_that_is_at_the_revision_of_the_stream_head : using_persistence
{
    const int HeadStreamRevision = 42;
    const int HeadCommitSequence = 15;
    static readonly Snapshot snapshot = new Snapshot(streamId, HeadStreamRevision, "snapshot");
    static readonly EnumerableCounter Committed = new EnumerableCounter(
			new[] { BuildCommitStub(HeadStreamRevision, HeadCommitSequence) });
    static IEventStream stream;

		Establish context = () =>
			persistence.Setup(x => x.GetFrom(streamId, HeadStreamRevision, int.MaxValue)).Returns(Committed);

		Because of = () =>
			stream = store.OpenStream(snapshot, int.MaxValue);

		It should_return_a_stream_with_the_correct_stream_identifier = () =>
			stream.StreamId.ShouldEqual(streamId);

		It should_return_a_stream_with_revision_of_the_stream_head = () =>
			stream.StreamRevision.ShouldEqual(HeadStreamRevision);

		It should_return_a_stream_with_a_commit_sequence_of_the_stream_head = () =>
			stream.CommitSequence.ShouldEqual(HeadCommitSequence);

		It should_return_a_stream_with_no_committed_events = () =>
			stream.CommittedEvents.Count.ShouldEqual(0);

		It should_return_a_stream_with_no_uncommitted_events = () =>
			stream.UncommittedEvents.Count.ShouldEqual(0);

		It should_only_enumerate_the_set_of_commits_once = () =>
			Committed.GetEnumeratorCallCount.ShouldEqual(1);
}
 */