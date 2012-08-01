package org.es4j.eventstore.core.dispatchertests.SynchronousDispatcherTests;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.eventstore.core.dispatcher.SynchronousDispatchScheduler;
import org.es4j.util.SystemTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_instantiating_the_synchronous_dispatch_scheduler {

    @Before
    public void setUp() {

        // Given
	given(persistence.getUndispatchedCommits()).willReturn(commits);

        // When
        scheduler = new SynchronousDispatchScheduler(dispatcher, persistence);
    }

    @Test
    public void should_initialize_the_persistence_engine() {

        // Then
        verify(persistence, times(1)).initialize();
    }

    @Test
    public void should_get_the_set_of_undispatched_commits() {

        // Then
        verify(persistence, times(1)).getUndispatchedCommits();
    }

    @Test
    public void should_provide_the_commits_to_the_dispatcher() {

        // Then
        for(Commit commit : commits) {
            verify(dispatcher, times(1)).dispatch(commit);
        }
        verify(dispatcher, times(commits.size())).dispatch(any(Commit.class));
    }

    static final UUID streamId = UUID.randomUUID();
    private static final List<Commit> commits = Arrays.asList
    (
        new Commit(streamId, 0, UUID.randomUUID(), 0, SystemTime.utcNow(), null, null),
        new Commit(streamId, 0, UUID.randomUUID(), 0, SystemTime.utcNow(), null, null)
    );


    @UnderTest
    SynchronousDispatchScheduler scheduler;

    @Mock
    IDispatchCommits dispatcher;
    @Mock
    IPersistStreams  persistence;
}



/*
[Subject("SynchronousDispatchScheduler")]
public class when_instantiating_the_synchronous_dispatch_scheduler
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
			dispatcher.Setup(x => x.Dispatch(commits.First()));
			dispatcher.Setup(x => x.Dispatch(commits.Last()));
		};

		Because of = () =>
			new SynchronousDispatchScheduler(dispatcher.Object, persistence.Object);

		It should_initialize_the_persistence_engine = () =>
			persistence.Verify(x => x.Initialize(), Times.Once());

		It should_get_the_set_of_undispatched_commits = () =>
			persistence.Verify(x => x.GetUndispatchedCommits(), Times.Once());

		It should_provide_the_commits_to_the_dispatcher = () =>
			dispatcher.VerifyAll();
	}
*/
