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
public class When_committing_with_a_valid_and_populated_attempt_to_a_stream extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
	given(pipelineHooks.get(0).preCommit(populatedAttempt)).willReturn(true);


        // When
        ((ICommitEvents)store).commit(populatedAttempt);
    }

    @Test
    public void should_provide_the_commit_to_the_precommit_hooks() {

        // Then
        for(IPipelineHook hook : pipelineHooks) {
            verify(hook, times(1)).preCommit(populatedAttempt);
        }
    }

    @Test
    public void should_provide_the_commit_attempt_to_the_configured_persistence_mechanism() {

        // Then
        verify(persistence, times(1)).commit(populatedAttempt);
    }

    @Test
    public void should_provide_the_commit_to_the_postcommit_hooks() {

        // Then
        for(IPipelineHook hook : pipelineHooks) {
            verify(hook, times(1)).postCommit(populatedAttempt);
        }
    }



    static final Commit populatedAttempt = buildCommitStub(1, 1);
}


/*
	[Subject("OptimisticEventStore")]
	public class when_committing_with_a_valid_and_populated_attempt_to_a_stream : using_persistence
	{
		static readonly Commit populatedAttempt = BuildCommitStub(1, 1);

		Establish context = () =>
		{
			persistence.Setup(x => x.Commit(populatedAttempt));

			pipelineHooks.Add(new Mock<IPipelineHook>());
			pipelineHooks[0].Setup(x => x.PreCommit(populatedAttempt)).Returns(true);
			pipelineHooks[0].Setup(x => x.PostCommit(populatedAttempt));
		};

		Because of = () =>
			((ICommitEvents)store).Commit(populatedAttempt);

		It should_provide_the_commit_to_the_precommit_hooks = () =>
			pipelineHooks.ForEach(x => x.Verify(hook => hook.PreCommit(populatedAttempt), Times.Once()));

		It should_provide_the_commit_attempt_to_the_configured_persistence_mechanism = () =>
			persistence.Verify(x => x.Commit(populatedAttempt), Times.Once());

		It should_provide_the_commit_to_the_postcommit_hooks = () =>
			pipelineHooks.ForEach(x => x.Verify(hook => hook.PostCommit(populatedAttempt), Times.Once()));
	}
*/
