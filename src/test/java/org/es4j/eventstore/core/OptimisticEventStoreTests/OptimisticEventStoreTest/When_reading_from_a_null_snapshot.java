package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.exceptions.ArgumentNullException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_reading_from_a_null_snapshot extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        thrown.expect(ArgumentNullException.class);

        // When
        store.openStream(null, Integer.MAX_VALUE);
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
public class when_reading_from_a_null_snapshot : using_persistence
{
		static Exception thrown;

		Because of = () =>
			thrown = Catch.Exception(() => store.OpenStream(null, int.MaxValue));

		It should_throw_an_ArgumentNullException = () =>
			thrown.ShouldBeOfType<ArgumentNullException>();
	}
*/
