package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IEventStream;
import java.util.ArrayList;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_an_empty_stream_starting_at_revision_zero extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(persistence.getFrom(streamId, 0, Integer.MAX_VALUE)).willReturn(new ArrayList<Commit>());

        // When
        stream = store.openStream(streamId, 0, 0);
    }

    @Test 
    public void should_return_a_new_stream() {
   
        // Then
        assertThat(stream, is(notNullValue()));
        assertNotNull(stream);
    }

    @Test 
    public void should_return_a_stream_with_the_correct_stream_identifier() {
        assertThat(stream.getStreamId(), is(streamId));
    }

    @Test 
    public void should_return_a_stream_with_a_zero_stream_revision() {
        assertThat(stream.getStreamRevision(), is(0));
    }

    @Test 
    public void should_return_a_stream_with_a_zero_commit_sequence() {
        assertThat(stream.getCommitSequence(), is(0));
    }

    @Test 
    public void should_return_a_stream_with_no_uncommitted_events() {
        assertThat(stream.getUncommittedEvents().size(), is(0));
    }

    @Test 
    public void should_return_a_stream_with_no_committed_events() {
        assertThat(stream.getCommittedEvents().size(), is(0));
    }

    @Test 
    public void should_return_a_stream_with_empty_headers() {
        assertThat(stream, is(notNullValue()));
        assertThat(stream.getUncommittedHeaders().size(), is(0));
    }
    

    IEventStream stream;
}




/*
[Subject("OptimisticEventStore")]
public class when_opening_an_empty_stream_starting_at_revision_zero : using_persistence
{
    static IEventStream stream;

    Establish context = () =>
        persistence.Setup(x => x.GetFrom(streamId, 0, 0)).Returns(new Commit[0]);

    Because of = () =>
        stream = store.OpenStream(streamId, 0, 0);

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