package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.exceptions.ArgumentException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_with_a_nonpositive_commit_sequence_back_to_the_stream extends UsingPersistence {
    @Before
    public void setUp() {

        // Given
        establishContext();
        thrown.expect(ArgumentException.class);
    
        // When  thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(invalidCommitSequence));
        ((ICommitEvents)store).commit(invalidCommit);
    }

    @Test
    public void should_throw_an_ArgumentException() {
        
        // Then thrown.ShouldBeOfType<ArgumentException>();
        //
    }

    static final int streamRevision = 1;
    static final int invalidCommitSequence = 0;

    static final Commit invalidCommit = buildCommitStub(streamRevision, invalidCommitSequence);

    @Rule
    public static ExpectedException thrown = ExpectedException.none();

}



/*
[Subject("OptimisticEventStore")]
public class when_committing_with_a_nonpositive_commit_sequence_back_to_the_stream : using_persistence
{
    const int StreamRevision = 1;
    const int InvalidCommitSequence = 0;
    static readonly Commit invalidCommitSequence = BuildCommitStub(StreamRevision, InvalidCommitSequence);
    static Exception thrown;

    Because of = () =>
        thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(invalidCommitSequence));

    It should_throw_an_ArgumentException = () =>
        thrown.ShouldBeOfType<ArgumentException>();
}
*/
