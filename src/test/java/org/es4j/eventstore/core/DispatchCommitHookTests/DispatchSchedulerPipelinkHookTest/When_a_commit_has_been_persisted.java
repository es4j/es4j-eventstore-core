package org.es4j.eventstore.core.DispatchCommitHookTests.DispatchSchedulerPipelinkHookTest;

import java.util.UUID;
import org.es4j.eventstore.core.DispatchSchedulerPipelineHook;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IScheduleDispatches;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.util.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_a_commit_has_been_persisted {

    @Test
    public void should_invoke_the_configured_dispatcher() {

        // When
        dispatchSchedulerHook.postCommit(commit);

        // Then
        verify(dispatcher, times(1)).scheduleDispatch(commit);
    }

    @InjectMocks
    IScheduleDispatches dispatcher = mock(IScheduleDispatches.class);

    @UnderTest
    DispatchSchedulerPipelineHook dispatchSchedulerHook = new DispatchSchedulerPipelineHook(dispatcher);

    private Commit commit = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);
}


/*
[Subject("DispatchSchedulerPipelinkHook")]
public class when_a_commit_has_been_persisted
{
    static readonly Commit commit = new Commit(
			Guid.NewGuid(), 0, Guid.NewGuid(), 0, DateTime.MinValue, null, null);
    static readonly Mock<IScheduleDispatches> dispatcher = new Mock<IScheduleDispatches>();
    static readonly DispatchSchedulerPipelineHook DispatchSchedulerHook = new DispatchSchedulerPipelineHook(dispatcher.Object);

    Establish context = () =>
        dispatcher.Setup(x => x.ScheduleDispatch(null));

    Because of = () =>
        DispatchSchedulerHook.PostCommit(commit);

    It should_invoke_the_configured_dispatcher = () =>
        dispatcher.Verify(x => x.ScheduleDispatch(commit), Times.Once());
}
*/
