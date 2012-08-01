package org.es4j.eventstore.core.dispatchertests.AsynchronousDispatcherTests;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.dispatcher.AsynchronousDispatchScheduler;
import org.es4j.util.SystemTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_instantiating_the_asynchronous_dispatch_scheduler {

    @Before
    public void setUp() {

        // Given
        ExecutorService  threadPool = Executors.newFixedThreadPool(3);

        persistence = BDDMockito.mock(IPersistStreams.class);
        doNothing().when(persistence).initialize();
	given(persistence.getUndispatchedCommits()).willReturn(commits);

        dispatcher  = BDDMockito.mock(IDispatchCommits.class);
	doNothing().when(dispatcher).dispatch(commits.get(0));
	doNothing().when(dispatcher).dispatch(commits.get(1));

        // When
        new AsynchronousDispatchScheduler(dispatcher, persistence, threadPool);
    }

    @Test
    public void should_take_a_few_milliseconds_for_the_other_thread_to_execute() throws InterruptedException {

        // Then
        Thread.sleep(25); // just a precaution because we're doing async tests
    }

    @Test
    public void should_initialize_the_persistence_engine() {

        // Then should_initialize_the_persistence_engine
        verify(persistence,times(1)).initialize();

        // Then should_get_the_set_of_undispatched_commits
        verify(persistence, times(1)).getUndispatchedCommits();

        // Then should_provide_the_commits_to_the_dispatcher
        for(int indx=0; indx < commits.size(); indx++) {
            verify(dispatcher).dispatch(commits.get(indx));
        }
    }

    @Ignore
    @Test
    public void should_get_the_set_of_undispatched_commits() {

        // Then
        verify(persistence, times(1)).getUndispatchedCommits();
    }

    @Ignore
    @Test
    public void should_provide_the_commits_to_the_dispatcher() {

        // Then
        for(int indx=0; indx < commits.size(); indx++) {
            verify(dispatcher).dispatch(commits.get(indx));
        }
    }


    static final UUID streamId = UUID.randomUUID();
    private static final List<Commit> commits = Arrays.asList(
            new Commit(streamId, 0, UUID.randomUUID(), 0, SystemTime.utcNow(), null, null),
            new Commit(streamId, 0, UUID.randomUUID(), 0, SystemTime.utcNow(), null, null));
    @Mock
    IDispatchCommits dispatcher;
    @Mock
    IPersistStreams  persistence;
}



/*
[Subject("AsynchronousDispatchScheduler")]
public class when_instantiating_the_asynchronous_dispatch_scheduler
{
		static readonly Guid streamId = Guid.NewGuid();
		private static readonly Commit[] commits =
		{
			new Commit(streamId, 0, Guid.NewGuid(), 0, SystemTime.UtcNow, null, null),
			new Commit(streamId, 0, Guid.NewGuid(), 0, SystemTime.UtcNow, null, null)
		};
		static readonly Mock<IDispatchCommits> dispatcher = new Mock<IDispatchCommits>();
		static readonly Mock<IPersistStreams> persistence = new Mock<IPersistStreams>();

		Establish context = () =>
		{
			persistence.Setup(x => x.Initialize());
			persistence.Setup(x => x.GetUndispatchedCommits()).Returns(commits);
			dispatcher .Setup(x => x.Dispatch(commits.First()));
			dispatcher .Setup(x => x.Dispatch(commits.Last()));
		};

		Because of = () =>
			new AsynchronousDispatchScheduler(dispatcher.Object, persistence.Object);

		It should_take_a_few_milliseconds_for_the_other_thread_to_execute = () =>
			Thread.Sleep(25); // just a precaution because we're doing async tests

		It should_initialize_the_persistence_engine = () =>
			persistence.Verify(x => x.Initialize(), Times.Once());

		It should_get_the_set_of_undispatched_commits = () =>
			persistence.Verify(x => x.GetUndispatchedCommits(), Times.Once());

		It should_provide_the_commits_to_the_dispatcher = () =>
			dispatcher.VerifyAll();
	}
 */