package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.IEventStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_creating_a_new_stream extends UsingPersistence {

    @Before
    public void setUp() throws Exception {

        // Given
        establishContext();

        //When
        stream = store.createStream(streamId);
    }

    @Test
    public void should_return_a_new_stream() {

        // Then
        assertNotNull(stream); //.ShouldNotBeNull();
    }

    @Test
    public void should_return_a_stream_with_the_correct_stream_identifier() {
        assertThat(stream.getStreamId(), is(streamId)); //.ShouldEqual(streamId);
    }

    @Test
    public void should_return_a_stream_with_a_zero_stream_revision() {
        assertThat(stream.getStreamRevision(), is(0)); //.ShouldEqual(0);
    }

    @Test
    public void should_return_a_stream_with_a_zero_commit_sequence() {
        assertThat(stream.getCommitSequence(), is(0)); //.ShouldEqual(0);
    }

    @Test
    public void should_return_a_stream_with_no_uncommitted_events() {
        assertThat(stream.getUncommittedEvents().size(), is(0)); //.ShouldBeEmpty();
    }

    @Test
    public void should_return_a_stream_with_no_committed_events() {
        assertThat(stream.getCommittedEvents().size(), is(0)); //.ShouldBeEmpty();
    }

    @Test
    public void should_return_a_stream_with_empty_headers() {
        assertThat(stream.getUncommittedHeaders().size(), is(0)); //.ShouldBeEmpty();
    }


    static IEventStream stream;
}



/*
[Subject("OptimisticEventStore")]
public class when_creating_a_new_stream : using_persistence
{
    static IEventStream stream;

        Because of = () =>
        stream = store.CreateStream(streamId);

		It should_return_a_new_stream = () =>
			stream.ShouldNotBeNull();

		It should_return_a_stream_with_the_correct_stream_identifier = () =>
			stream.StreamId.ShouldEqual(streamId);

		It should_return_a_stream_with_a_zero_stream_revision = () =>
			stream.StreamRevision.ShouldEqual(0);

		It should_return_a_stream_with_a_zero_commit_sequence = () =>
			stream.CommitSequence.ShouldEqual(0);

		It should_return_a_stream_with_no_uncommitted_events = () =>
			stream.UncommittedEvents.ShouldBeEmpty();

		It should_return_a_stream_with_no_committed_events = () =>
			stream.CommittedEvents.ShouldBeEmpty();

		It should_return_a_stream_with_empty_headers = () =>
			stream.UncommittedHeaders.ShouldBeEmpty();
}
*/

