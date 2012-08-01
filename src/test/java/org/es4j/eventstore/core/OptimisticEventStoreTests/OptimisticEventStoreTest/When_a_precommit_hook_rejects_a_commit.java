package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.api.IPipelineHook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_a_precommit_hook_rejects_a_commit extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(pipelineHooks.get(0).preCommit(attempt)).willReturn(false);

        // When
        ((ICommitEvents)store).commit(attempt);

    }

    @Test
    public void should_not_call_the_underlying_infrastructure() {

        // Then
        verify(persistence, times(0)).commit(attempt);
    }

    @Test
    public void  should_not_provide_the_commit_to_the_postcommit_hooks() {

        // Then
        for(IPipelineHook pipelineHook : pipelineHooks) {
            verify(pipelineHook, times(0)).postCommit(attempt);
        }
    }

    //protected static final Guid streamId = Guid.newGuid();

    //public List<IPipelineHook>  pipelineHooks;

    //@Mock
    //public  IPersistStreams      persistence;

    static final Commit attempt = buildCommitStub(1, 1);

    //@UnderTest
    //protected OptimisticEventStore store;
}



/*
	[Subject("OptimisticEventStore")]
	public class when_a_precommit_hook_rejects_a_commit : using_persistence
	{
		static readonly Commit attempt = BuildCommitStub(1, 1);

		Establish context = () =>
		{
			pipelineHooks.Add(new Mock<IPipelineHook>());
			pipelineHooks[0].Setup(x => x.PreCommit(attempt)).Returns(false);
		};

		Because of = () =>
			((ICommitEvents)store).Commit(attempt);

		It should_not_call_the_underlying_infrastructure = () =>
			persistence.Verify(x => x.Commit(attempt), Times.Never());

		It should_not_provide_the_commit_to_the_postcommit_hooks = () =>
			pipelineHooks.ForEach(x => x.Verify(y => y.PostCommit(attempt), Times.Never()));
	}
 */
