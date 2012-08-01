package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.api.IEventStream;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_any_uncommitted_changes { // extends OnTheEventStream {

    @UnderTest
    private IEventStream/*OptimisticEventStream*/ stream;

    static final UUID streamId = UUID.randomUUID();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    ICommitEvents persistence;

    static final int defaultStreamRevision = 1;
    static final int defaultCommitSequence = 1;
    static final UUID                commitId    = UUID.randomUUID();
    static final EventMessage        uncommitted = new EventMessage("");
    static final Map<String, Object> headers     = new HashMap<String, Object>();
    static { headers.put("key", "value");}

    Commit                   constructed;
    //CommitCallback           commitCallback = new CommitCallback();

    @Before
    public void given() throws Exception {

        // Given
         persistence = BDDMockito.mock(ICommitEvents.class);
        BDDMockito.doAnswer(new CommitCallback()).when(persistence).commit(BDDMockito.any(Commit.class));

        stream = new OptimisticEventStream(streamId, persistence);
	stream.add(uncommitted);
        for (Entry<String,Object> item : headers.entrySet()) {
            stream.getUncommittedHeaders().put(item.getKey(), item.getValue());
        }

        // When
        stream.commitChanges(commitId);
    }


    @Test
    public void should_provide_a_commit_to_the_underlying_infrastructure() {

        // Then
        BDDMockito.verify(persistence, BDDMockito.times(1)).commit(BDDMockito.any(Commit.class));
    }


    @Test
    public void should_build_the_commit_with_the_correct_stream_identifier() {

        // Then
        assertThat(constructed.getStreamId(), is(streamId));
    }

    @Test
    public void should_build_the_commit_with_the_correct_stream_revision() {

        // Then
        assertThat(constructed.getStreamRevision(), is(defaultStreamRevision));
    }

    @Test
    public void should_build_the_commit_with_the_correct_commit_identifier() {

        // Then
        assertThat(constructed.getCommitId(), is(commitId));
    }

    @Test
    public void should_build_the_commit_with_an_incremented_commit_sequence() {

        // Then
        assertEquals(constructed.getCommitSequence(), defaultCommitSequence);
    }

    @Test
    public void should_build_the_commit_with_the_correct_commit_stamp() {

        // Then
        assertTrue(SystemTime.utcNow().getMillis() - constructed.getCommitStamp().getMillis() < 150);
    }

    @Test
    public void should_build_the_commit_with_the_headers_provided() {
        //constructed.Headers[headers.First().Key].ShouldEqual(headers.First().Value);
        assertFalse(headers.isEmpty());
        //Assertions.assertThat(headers).hasSize(1);
        assertEquals(headers.entrySet().iterator().next().getValue(),
                     constructed.getHeaders().get(theFirst(headers.entrySet()).getKey()));
    }

    @Test
    public void should_build_the_commit_containing_all_uncommitted_events() {
        assertEquals(headers.size(), constructed.getEvents().size());
    }

    @Test
    public void should_build_the_commit_using_the_event_messages_provided() {

        // Then
        assertThat(constructed.getEvents().get(0), is(uncommitted));
    }



    @Test
    public void should_contain_a_copy_of_the_headers_provided() {
        assertFalse(constructed.getHeaders().isEmpty());
    }

    @Test
    public void should_update_the_stream_revision() {

        // Then
        assertEquals(stream.getStreamRevision(), constructed.getStreamRevision());
    }

    @Test
    public void should_update_the_commit_sequence() {

        // Then
        assertEquals(stream.getCommitSequence(), constructed.getCommitSequence());
    }

    @Test
    public void should_add_the_uncommitted_events_the_committed_events() {

        // Then
        //stream.CommittedEvents.Last().ShouldEqual(uncommitted);
        assertFalse(stream.getCommittedEvents().isEmpty());
        assertEquals(theLast(stream.getCommittedEvents()), uncommitted);
    }

    @Test
    public void should_clear_the_uncommitted_events_on_the_stream() {
        assertTrue(stream.getUncommittedEvents().isEmpty());
    }

    @Test
    public void should_clear_the_uncommitted_headers_on_the_stream() {
        assertTrue(stream.getUncommittedHeaders().isEmpty());
    }

    @Test
    public void should_copy_the_uncommitted_headers_to_the_committed_stream_headers() {

        // Then
        assertEquals(stream.getCommittedHeaders().size(), headers.size());
    }

    protected <T> T theFirst(Collection<T> list) {
        return ((list.size() > 0)? list.iterator().next() : null);
     }

    protected <T> T theLast(Collection<T> list) {
        return (T)((list.size() > 0)? list.toArray()[list.size() - 1] : null);
    }

    private class CommitCallback implements Answer {
        //public Commit             constructed = null;
        //public List<EventMessage> events      = null;

        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            constructed = (Commit)((Commit)invocation.getArguments()[0]); //.clone();
            //events = new ArrayList<EventMessage>(constructed.getEvents());
            return null;
        }
    }
}










/*
[Subject("OptimisticEventStream")]
public class when_committing_any_uncommitted_changes : on_the_event_stream
{
    static readonly Guid commitId = Guid.NewGuid();
    static readonly EventMessage uncommitted = new EventMessage { Body = string.Empty };
    static readonly Dictionary<string, object> headers = new Dictionary<string, object> { { "key", "value" } };
    static Commit constructed;

    Establish context = () =>
    {
        persistence.Setup(x => x.Commit(Moq.It.IsAny<Commit>())).Callback<Commit>(x => constructed = x);
        stream.Add(uncommitted);
        foreach (var item in headers)
            stream.UncommittedHeaders[item.Key] = item.Value;
    };

    Because of = () =>
        stream.CommitChanges(commitId);

		It should_provide_a_commit_to_the_underlying_infrastructure = () =>
			persistence.Verify(x => x.Commit(Moq.It.IsAny<Commit>()), Times.Once());

		It should_build_the_commit_with_the_correct_stream_identifier = () =>
			constructed.StreamId.ShouldEqual(streamId);

		It should_build_the_commit_with_the_correct_stream_revision = () =>
			constructed.StreamRevision.ShouldEqual(DefaultStreamRevision);

		It should_build_the_commit_with_the_correct_commit_identifier = () =>
			constructed.CommitId.ShouldEqual(commitId);

		It should_build_the_commit_with_an_incremented_commit_sequence = () =>
			constructed.CommitSequence.ShouldEqual(DefaultCommitSequence);

		It should_build_the_commit_with_the_correct_commit_stamp = () =>
			SystemTime.UtcNow.Subtract(constructed.CommitStamp).ShouldBeLessThan(TimeSpan.FromMilliseconds(150));

		It should_build_the_commit_with_the_headers_provided = () =>
			constructed.Headers[headers.First().Key].ShouldEqual(headers.First().Value);

		It should_build_the_commit_containing_all_uncommitted_events = () =>
			constructed.Events.Count.ShouldEqual(headers.Count);

		It should_build_the_commit_using_the_event_messages_provided = () =>
			constructed.Events.First().ShouldEqual(uncommitted);

		It should_contain_a_copy_of_the_headers_provided = () =>
			constructed.Headers.ShouldNotBeEmpty();

		It should_update_the_stream_revision = () =>
			stream.StreamRevision.ShouldEqual(constructed.StreamRevision);

		It should_update_the_commit_sequence = () =>
			stream.CommitSequence.ShouldEqual(constructed.CommitSequence);

		It should_add_the_uncommitted_events_the_committed_events = () =>
			stream.CommittedEvents.Last().ShouldEqual(uncommitted);

		It should_clear_the_uncommitted_events_on_the_stream = () =>
			stream.UncommittedEvents.ShouldBeEmpty();

		It should_clear_the_uncommitted_headers_on_the_stream = () =>
			stream.UncommittedHeaders.ShouldBeEmpty();

		It should_copy_the_uncommitted_headers_to_the_committed_stream_headers = () =>
			stream.CommittedHeaders.Count.ShouldEqual(headers.Count);
}
*/