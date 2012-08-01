package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IEventStream;
import org.es4j.eventstore.api.IPipelineHook;
import java.util.Arrays;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_a_populated_stream extends UsingPersistence {
 
    @Before
    public void setUp() {
        
        // Given
        establishContext();
        given(persistence.getFrom(streamId, minRevision, maxRevision)).willReturn(Arrays.asList(committed));
        given(pipelineHooks.get(0).select(committed)).willReturn(committed);

        // When
        stream = store.openStream(streamId, minRevision, maxRevision);
    }

    @Test 
    public void should_invoke_the_underlying_infrastructure_with_the_values_provided() {
        verify(persistence, times(1)).getFrom(streamId, minRevision, maxRevision);
    }

    @Test 
    public void should_provide_the_commits_to_the_selection_hooks() {
        for(IPipelineHook hook : pipelineHooks) {
            verify(hook, times(1)).select(committed);
        }
    }

    @Test 
    public void should_return_an_event_stream_containing_the_correct_stream_identifer() {
        assertThat(stream.getStreamId(), is(streamId)); //.ShouldEqual(streamId);
    }


    static final int minRevision = 17;
    static final int maxRevision = 42;

    static final Commit committed = buildCommitStub(minRevision, 1);

    static IEventStream stream;
}


/*
[Subject("OptimisticEventStore")]
public class when_opening_a_populated_stream : using_persistence
{
    const int MinRevision = 17;
    const int MaxRevision = 42;
    static readonly Commit Committed = BuildCommitStub(MinRevision, 1);
    static IEventStream stream;

        Establish context = () =>
	{
            persistence.Setup(x => x.GetFrom(streamId, MinRevision, MaxRevision)).Returns(new[] { Committed });
            pipelineHooks.Add(new Mock<IPipelineHook>());
            pipelineHooks[0].Setup(x => x.Select(Committed)).Returns(Committed);
        };

        Because of = () =>
            stream = store.OpenStream(streamId, MinRevision, MaxRevision);

    It should_invoke_the_underlying_infrastructure_with_the_values_provided = () =>
        persistence.Verify(x => x.GetFrom(streamId, MinRevision, MaxRevision), Times.Once());

    It should_provide_the_commits_to_the_selection_hooks = () =>
        pipelineHooks.ForEach(x => x.Verify(hook => hook.Select(Committed), Times.Once()));

    It should_return_an_event_stream_containing_the_correct_stream_identifer = () =>
        stream.StreamId.ShouldEqual(streamId);
}
*/
