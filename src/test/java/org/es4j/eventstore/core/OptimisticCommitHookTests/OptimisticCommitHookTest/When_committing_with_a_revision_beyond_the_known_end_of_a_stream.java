package org.es4j.eventstore.core.OptimisticCommitHookTests.OptimisticCommitHookTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime_DELETE;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.api.persistence.StorageException;
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
public class When_committing_with_a_revision_beyond_the_known_end_of_a_stream {

    @Test
    public void should_throw_a_PersistenceException() {

        // Given
        hook = new OptimisticPipelineHook();
        hook.postCommit(alreadyCommitted);
        thrown.expect(StorageException.class);

        // When
        hook.preCommit(beyondEndOfStream);

        // Then
        //thrown.ShouldBeOfType<StorageException>();
    }


    static final int HeadCommitSequence = 1;
    static final int HeadStreamRevision = 1;
    static final int NumberOfEventsBeingCommitted = 1;
    static final int ExpectedNextStreamRevision = HeadStreamRevision + 1 + NumberOfEventsBeingCommitted;
    static final int BeyondEndOfStreamRevision = ExpectedNextStreamRevision + 1;

    static final Commit alreadyCommitted  = buildCommitStub(HeadStreamRevision, HeadCommitSequence);
    static final Commit beyondEndOfStream = buildCommitStub(BeyondEndOfStreamRevision, HeadCommitSequence + 1);

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
public class when_committing_with_a_revision_beyond_the_known_end_of_a_stream : using_commit_hooks
{
    const int HeadCommitSequence = 1;
    const int HeadStreamRevision = 1;
    const int NumberOfEventsBeingCommitted = 1;
    const int ExpectedNextStreamRevision = HeadStreamRevision + 1 + NumberOfEventsBeingCommitted;
    const int BeyondEndOfStreamRevision = ExpectedNextStreamRevision + 1;
    static readonly Commit alreadyCommitted = BuildCommitStub(HeadStreamRevision, HeadCommitSequence);
    static readonly Commit beyondEndOfStream = BuildCommitStub(BeyondEndOfStreamRevision, HeadCommitSequence + 1);
    static Exception thrown;

    Establish context = () =>
        hook.PostCommit(alreadyCommitted);

    Because of = () =>
        thrown = Catch.Exception(() => hook.PreCommit(beyondEndOfStream));

    It should_throw_a_PersistenceException = () =>
        thrown.ShouldBeOfType<StorageException>();
}
*/

