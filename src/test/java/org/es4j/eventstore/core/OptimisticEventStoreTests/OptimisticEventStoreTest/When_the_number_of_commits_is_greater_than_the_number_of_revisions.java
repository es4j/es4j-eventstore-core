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
public class When_the_number_of_commits_is_greater_than_the_number_of_revisions extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        thrown.expect(ArgumentException.class);

        // When
        ((ICommitEvents)store).commit(corrupt);
    }
    
    @Test
    public void should_throw_a_StorageException() {
        
        // Then thrown.ShouldBeOfType<ArgumentException>();
        //
    }
 
    private void commit(Commit corrupt) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    static final int    streamRevision = 1;
    static final int    commitSequence = 2; // should never be greater than StreamRevision.
    static final Commit corrupt = buildCommitStub(streamRevision, commitSequence);

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}


/*
[Subject("OptimisticEventStore")]
public class when_the_number_of_commits_is_greater_than_the_number_of_revisions : using_persistence
{
		const int StreamRevision = 1;
		const int CommitSequence = 2; // should never be greater than StreamRevision.
		static readonly Commit corrupt = BuildCommitStub(StreamRevision, CommitSequence);
		static Exception thrown;

		Because of = () =>
			thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(corrupt));

		It should_throw_a_StorageException = () =>
			thrown.ShouldBeOfType<ArgumentException>();
}
*/
