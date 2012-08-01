package org.es4j.eventstore.core.OptimisticCommitHookTests.OptimisticCommitHookTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime_DELETE;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ConcurrencyException;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.core.OptimisticPipelineHook;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.SystemTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_with_a_revision_less_or_equal_to_than_the_most_recent_revision_read_for_the_stream {

    @Test
    public void should_throw_a_ConcurrencyException() {

        // Given
        hook = new OptimisticPipelineHook();
        hook.postCommit(committed);
        thrown.expect(ConcurrencyException.class);

        // When
        hook.preCommit(failedAttempt);

        // Then
        // thrown.ShouldBeOfType<ConcurrencyException>();
    }


    static final int headStreamRevision = 3;
    static final int headCommitSequence = 2;
    static final int duplicateStreamRevision = headStreamRevision;

    static final Commit committed     = buildCommitStub(headStreamRevision,      headCommitSequence);
    static final Commit failedAttempt = buildCommitStub(duplicateStreamRevision, headCommitSequence + 1);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @UnderTest
    IPipelineHook hook; // = new OptimisticPipelineHook();

    protected static UUID streamId = UUID.randomUUID();

    protected static Commit buildCommitStub(int streamRevision, int commitSequence) {
        List<EventMessage> events = Arrays.asList(new EventMessage(""));
        return new Commit(streamId, streamRevision, UUID.randomUUID(), commitSequence, SystemTime.utcNow(), null, events);
    }
}


/*
[Subject("OptimisticCommitHook")]
public class when_committing_with_a_revision_less_or_equal_to_than_the_most_recent_revision_read_for_the_stream : using_commit_hooks
{
    const int HeadStreamRevision = 3;
    const int HeadCommitSequence = 2;
    const int DuplicateStreamRevision = HeadStreamRevision;
    static readonly Commit Committed = BuildCommitStub(HeadStreamRevision, HeadCommitSequence);
    static readonly Commit FailedAttempt = BuildCommitStub(DuplicateStreamRevision, HeadCommitSequence + 1);
    static Exception thrown;

    Establish context = () =>
        hook.PostCommit(Committed);

    Because of = () =>
        thrown = Catch.Exception(() => hook.PreCommit(FailedAttempt));

    It should_throw_a_ConcurrencyException = () =>
        thrown.ShouldBeOfType<ConcurrencyException>();
}
 */
