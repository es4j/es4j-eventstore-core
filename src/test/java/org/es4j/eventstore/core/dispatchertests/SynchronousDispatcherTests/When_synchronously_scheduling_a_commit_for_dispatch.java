package org.es4j.eventstore.core.dispatchertests.SynchronousDispatcherTests;

import java.util.LinkedList;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.dispatcher.SynchronousDispatchScheduler;
import org.es4j.util.SystemTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_synchronously_scheduling_a_commit_for_dispatch {

    @Before
    public void setUp() {

        // Given
        given(persistence.getUndispatchedCommits()).willReturn(new LinkedList<Commit>());
        dispatchScheduler = new SynchronousDispatchScheduler(dispatcher, persistence);

        // When
        dispatchScheduler.scheduleDispatch(commit);
    }

    @Test
    public void should_provide_the_commit_to_the_dispatcher() {

        // Then
        verify(dispatcher, times(1)).dispatch(commit);
    }

    @Test
    public void should_mark_the_commit_as_dispatched() {

        // Then
        verify(persistence, times(1)).markCommitAsDispatched(commit);
    }

    SynchronousDispatchScheduler dispatchScheduler;

    static final Commit commit = new Commit(UUID.randomUUID(),0,UUID.randomUUID(),0,SystemTime.utcNow(),null,null);
    @Mock
    IDispatchCommits dispatcher;
    @Mock
    IPersistStreams  persistence;
}


/*
[Subject("SynchronousDispatchScheduler")]
public class when_synchronously_scheduling_a_commit_for_dispatch
{
    static readonly Commit commit = new Commit(Guid.NewGuid(), 0, Guid.NewGuid(), 0, SystemTime.UtcNow, null, null);
    static readonly Mock<IDispatchCommits> dispatcher = new Mock<IDispatchCommits>();
    static readonly Mock<IPersistStreams> persistence = new Mock<IPersistStreams>();
    static SynchronousDispatchScheduler dispatchScheduler;

		Establish context = () =>
		{
			dispatcher.Setup(x => x.Dispatch(commit));
			persistence.Setup(x => x.MarkCommitAsDispatched(commit));

			dispatchScheduler = new SynchronousDispatchScheduler(dispatcher.Object, persistence.Object);
		};

		Because of = () =>
			dispatchScheduler.ScheduleDispatch(commit);

		It should_provide_the_commit_to_the_dispatcher = () =>
			dispatcher.Verify(x => x.Dispatch(commit), Times.Once());

		It should_mark_the_commit_as_dispatched = () =>
			persistence.Verify(x => x.MarkCommitAsDispatched(commit), Times.Once());
	}
*/
