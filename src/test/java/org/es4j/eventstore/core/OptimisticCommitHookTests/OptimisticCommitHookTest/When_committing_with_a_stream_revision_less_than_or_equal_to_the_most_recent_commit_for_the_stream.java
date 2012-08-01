package org.es4j.eventstore.core.OptimisticCommitHookTests.OptimisticCommitHookTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.core.OptimisticPipelineHook;
import org.es4j.eventstore.core.UnderTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_with_a_stream_revision_less_than_or_equal_to_the_most_recent_commit_for_the_stream extends UsingCommitHooks {

    @Test
    public void should_throw_a_ConcurrencyException() {

        // Given
        hook = new OptimisticPipelineHook();
        hook.postCommit(successfulAttempt);
        thrown.expect(ConcurrencyException.class);

	// When
        hook.preCommit(failedAttempt);

        // Then
        // thrown.ShouldBeOfType<ConcurrencyException>();
    }


    static final int duplicateStreamRevision = 2;

    static final Commit successfulAttempt = buildCommitStub(duplicateStreamRevision, 1);
    static final Commit failedAttempt     = buildCommitStub(duplicateStreamRevision, 2);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @UnderTest
    IPipelineHook hook; // = new OptimisticPipelineHook();
}

/*
[Subject("OptimisticCommitHook")]
public class when_committing_with_a_stream_revision_less_than_or_equal_to_the_most_recent_commit_for_the_stream : using_commit_hooks
{
    const int DuplicateStreamRevision = 2;

    static readonly Commit SuccessfulAttempt = BuildCommitStub(DuplicateStreamRevision, 1);
    static readonly Commit FailedAttempt = BuildCommitStub(DuplicateStreamRevision, 2);
    static Exception thrown;

        Establish context = () =>
		hook.PostCommit(SuccessfulAttempt);

	Because of = () =>
		thrown = Catch.Exception(() => hook.PreCommit(FailedAttempt));

    It should_throw_a_ConcurrencyException = () =>
        thrown.ShouldBeOfType<ConcurrencyException>();
}

*/
