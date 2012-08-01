package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.exceptions.ArgumentNullException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_a_null_attempt_back_to_the_stream extends UsingPersistence {
    
    @Before
    public void setUp() {

        // Given
        establishContext();
        thrown.expect(ArgumentNullException.class);

        // When
        ((ICommitEvents)store).commit(null);
    }

    @Test
    public void should_throw_an_ArgumentNullException() {

        // Then thrown.ShouldBeOfType<ArgumentNullException>();
        //
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();
}


/*
[Subject("OptimisticEventStore")]
public class when_committing_a_null_attempt_back_to_the_stream : using_persistence
{
		static Exception thrown;

		Because of = () =>
			thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(null));

		It should_throw_an_A rgumentNullException = () =>
			thrown.ShouldBeOfType<ArgumentNullException>();
	}
 */
