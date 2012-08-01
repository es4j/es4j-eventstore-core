package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_building_a_stream extends OnTheEventStream {

    @UnderTest
    OptimisticEventStream stream;

    static final UUID     streamId    = UUID.randomUUID();

    static final int          minRevision = 2;
    static final int          maxRevision = 7;
    static final int          eachCommitHas = 2;//.Events();
    static final List<Commit> committed = Arrays.asList(
        buildCommitStub(streamId, 2, 1, eachCommitHas),  // 1-2
        buildCommitStub(streamId, 4, 2, eachCommitHas),  // 3-4
        buildCommitStub(streamId, 6, 3, eachCommitHas),  // 5-6
        buildCommitStub(streamId, 8, 3, eachCommitHas)); // 7-8

    @BeforeClass
    public static void establishClassContext() {
        Commit[] commits = committed.toArray(new Commit[eachCommitHas]);
        Commit commit;

        commit = commits[0];
        Map<String, Object> headers = commit.getHeaders();
        headers.put("Common", "");

        commits[0].getHeaders().put("Common", "");
        commits[1].getHeaders().put("Common", "");
        commits[2].getHeaders().put("Common", "");
        commits[3].getHeaders().put("Common", "");
        commits[0].getHeaders().put("Unique", "");

        //persistence.Setup(x => x.GetFrom(streamId, MinRevision, MaxRevision)).Returns(Committed);
        //stub(persistence.getFrom(streamId, minRevision, maxRevision)).toReturn(committed);
    };


    @Before
    public void givne_when() {

        // Given
        ICommitEvents persistence = mock(ICommitEvents.class);
        stub(persistence.getFrom(streamId, minRevision, maxRevision)).toReturn(committed);

        // when
	stream = new OptimisticEventStream(streamId, persistence, minRevision, maxRevision);
    }

    @Test
    public void  should_have_the_correct_stream_identifier() {
        assertThat(stream.getStreamId(), is(streamId));
    }

    @Test
    public void should_have_the_correct_head_stream_revision() {
        assertThat(stream.getStreamRevision(), is(maxRevision));
    }

    @Test
    public void should_have_the_correct_head_commit_sequence() {
        assertThat(stream.getCommitSequence(), is(theLast(committed).getCommitSequence()));
    }

    @Test
    public void should_not_include_events_below_the_minimum_revision_indicated() {
        assertEquals(theFirst(stream.getCommittedEvents()), theLast(theFirst(committed).getEvents()));
    }

    @Test
    public void should_not_include_events_above_the_maximum_revision_indicated() {
        assertEquals(theLast(stream.getCommittedEvents()), theFirst(theLast(committed).getEvents()));
    }

    @Test
    public void should_have_all_of_the_committed_events_up_to_the_stream_revision_specified() {
        assertThat(stream.getCommittedEvents().size(), is(maxRevision - minRevision + 1));
    }

    @Test
    public void should_contain_the_headers_from_the_underlying_commits() {
        assertThat(stream.getCommittedHeaders().size(), is(2));
    }
}






/*
[Subject("OptimisticEventStream")]
public class when_building_a_stream : on_the_event_stream
{
    const int MinRevision = 2;
    const int MaxRevision = 7;
    static readonly int EachCommitHas = 2.Events();
    static readonly Commit[] Committed = new[]
    {
        BuildCommitStub(2, 1, EachCommitHas), // 1-2
        BuildCommitStub(4, 2, EachCommitHas), // 3-4
        BuildCommitStub(6, 3, EachCommitHas), // 5-6
        BuildCommitStub(8, 3, EachCommitHas) // 7-8
    };

    Establish context = () =>
    {
        Committed[0].Headers["Common"] = string.Empty;
        Committed[1].Headers["Common"] = string.Empty;
        Committed[2].Headers["Common"] = string.Empty;
        Committed[3].Headers["Common"] = string.Empty;
        Committed[0].Headers["Unique"] = string.Empty;

        persistence.Setup(x => x.GetFrom(streamId, MinRevision, MaxRevision)).Returns(Committed);
    };

        Because of = () =>
	    stream = new OptimisticEventStream(streamId, persistence.Object, MinRevision, MaxRevision);

	It should_have_the_correct_stream_identifier = () =>
	    stream.StreamId.ShouldEqual(streamId);

	It should_have_the_correct_head_stream_revision = () =>
	    stream.StreamRevision.ShouldEqual(MaxRevision);

	It should_have_the_correct_head_commit_sequence = () =>
	    stream.CommitSequence.ShouldEqual(Committed.Last().CommitSequence);

	It should_not_include_events_below_the_minimum_revision_indicated = () =>
	    stream.CommittedEvents.First().ShouldEqual(Committed.First().Events.Last());

	It should_not_include_events_above_the_maximum_revision_indicated = () =>
	    stream.CommittedEvents.Last().ShouldEqual(Committed.Last().Events.First());

	It should_have_all_of_the_committed_events_up_to_the_stream_revision_specified = () =>
	    stream.CommittedEvents.Count.ShouldEqual(MaxRevision - MinRevision + 1);

        It should_contain_the_headers_from_the_underlying_commits = () =>
	    stream.CommittedHeaders.Count.ShouldEqual(2);
}
*/
