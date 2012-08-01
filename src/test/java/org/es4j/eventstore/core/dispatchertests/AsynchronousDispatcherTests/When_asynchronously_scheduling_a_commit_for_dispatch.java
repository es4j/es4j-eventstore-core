package org.es4j.eventstore.core.dispatchertests.AsynchronousDispatcherTests;

import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import junit.framework.Assert;
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
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_asynchronously_scheduling_a_commit_for_dispatch {

    @Before
    public void setUp() {
    }

    @Test
    public void should_provide_the_commit_to_the_dispatcher() {
        ExecutorService  threadPool = Executors.newFixedThreadPool(3);
        AsynchronousDispatchScheduler dispatchScheduler;

        // Given
        IDispatchCommits dispatcher  = Mockito.mock(IDispatchCommits.class);
        IPersistStreams  persistence = Mockito.mock(IPersistStreams.class);
        given(persistence.getUndispatchedCommits()).willReturn(new LinkedList<Commit>());
        dispatchScheduler = new AsynchronousDispatchScheduler(dispatcher, persistence, threadPool);

        // When
        dispatchScheduler.scheduleDispatch(commit);

        // Then  should_provide_the_commit_to_the_dispatcher
        verify(dispatcher,  times(1)).dispatch(commit);
        // Then should_mark_the_commit_as_dispatched
        verify(persistence, times(1)).markCommitAsDispatched(commit); //
    }

    @Ignore
    @Test
    public void should_mark_the_commit_as_dispatched() {
        ExecutorService  threadPool = Executors.newFixedThreadPool(3);
        AsynchronousDispatchScheduler dispatchScheduler;

        // Given
        IDispatchCommits dispatcher  = Mockito.mock(IDispatchCommits.class);
        IPersistStreams  persistence = Mockito.mock(IPersistStreams.class);
        given(persistence.getUndispatchedCommits()).willReturn(new LinkedList<Commit>());
        dispatchScheduler = new AsynchronousDispatchScheduler(dispatcher, persistence, threadPool);

        // When
        dispatchScheduler.scheduleDispatch(commit);

        // Then
        verify(dispatcher,  times(1)).dispatch(commit);
        verify(persistence, times(1)).markCommitAsDispatched(commit); //
    }

    @Test
    public void should_take_a_few_milliseconds_for_the_other_thread_to_execute() {
        ExecutorService  threadPool = Executors.newFixedThreadPool(3);
        AsynchronousDispatchScheduler dispatchScheduler;

        // Given
        //MockitoAnnotations.initMocks(this);
        IDispatchCommits dispatcher  = Mockito.mock(IDispatchCommits.class);
        IPersistStreams  persistence = Mockito.mock(IPersistStreams.class);
        given(persistence.getUndispatchedCommits()).willReturn(new LinkedList<Commit>());
        dispatchScheduler = new AsynchronousDispatchScheduler(dispatcher, persistence, threadPool);

        // When
        dispatchScheduler.scheduleDispatch(commit);

        // Then
        try {
            Thread.sleep(25); // just a precaution because we're doing async tests
        } catch (InterruptedException ex) {
            Assert.fail("Exception throun");
        }
    }


    final Commit           commit     = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, SystemTime.utcNow(), null, null);
//  static final ExecutorService  threadPool = Executors.newFixedThreadPool(3);

    //@UnderTest
    //AsynchronousDispatchScheduler dispatchScheduler;
}




/*

[Subject("AsynchronousDispatchScheduler")]
public class when_asynchronously_scheduling_a_commit_for_dispatch
{
    static readonly Commit commit = new Commit(Guid.NewGuid(), 0, Guid.NewGuid(), 0, SystemTime.UtcNow, null, null);
    static readonly Mock<IDispatchCommits> dispatcher = new Mock<IDispatchCommits>();
    static readonly Mock<IPersistStreams> persistence = new Mock<IPersistStreams>();
    static AsynchronousDispatchScheduler dispatchScheduler;

    Establish context = () =>
    {
        dispatcher.Setup(x => x.Dispatch(commit));
        persistence.Setup(x => x.MarkCommitAsDispatched(commit));

        dispatchScheduler = new AsynchronousDispatchScheduler(dispatcher.Object, persistence.Object);
    };

    Because of = () =>
        dispatchScheduler.ScheduleDispatch(commit);

    It should_take_a_few_milliseconds_for_the_other_thread_to_execute = () =>
        Thread.Sleep(25); // just a precaution because we're doing async tests

    It should_provide_the_commit_to_the_dispatcher = () =>
        dispatcher.Verify(x => x.Dispatch(commit), Times.Once());

    It should_mark_the_commit_as_dispatched = () =>
        persistence.Verify(x => x.MarkCommitAsDispatched(commit), Times.Once());
}
 */