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
public class When_committing_with_a_non_positive_stream_revision_back_to_the_stream extends UsingPersistence {

    @Before
    public void setUp() {
        
        // Given
        establishContext();
        thrown.expect(ArgumentException.class);

        // When thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(invalidStreamRevision));
        ((ICommitEvents)store).commit(invalidStream);
    }

    @Test
    public void should_throw_an_ArgumentException() {

        // Then thrown.ShouldBeOfType<ArgumentException>();
        //
    }
    

    @Rule
    public static ExpectedException thrown = ExpectedException.none();

    static final int    invalidStreamRevision = 0;
    static final int    commitSequence        = 1;
    static final Commit invalidStream         = buildCommitStub(invalidStreamRevision, commitSequence);
}


/*
[Subject("OptimisticEventStore")]
public class when_committing_with_a_non_positive_stream_revision_back_to_the_stream : using_persistence
{
    const int InvalidStreamRevision = 0;
    const int CommitSequence = 1;
    static readonly Commit invalidStreamRevision = BuildCommitStub(InvalidStreamRevision, CommitSequence);
    static Exception thrown;

    Because of = () =>
        thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(invalidStreamRevision));

    It should_throw_an_ArgumentException = () =>
        thrown.ShouldBeOfType<ArgumentException>();
    }
*/
