package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.persistence.IPersistStreams;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class When_accessing_the_underlying_persistence extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        
        // When
        advanced = store.advanced();

    }

    @Test
    public void should_return_a_reference_to_the_underlying_persistence_infrastructure() {

        // Then
        assertThat(advanced, is(persistence));
    }


    IPersistStreams advanced;
}


/*
	[Subject("OptimisticEventStore")]
	public class when_accessing_the_underlying_persistence : using_persistence
	{
		It should_return_a_reference_to_the_underlying_persistence_infrastructure = () =>
			store.Advanced.ShouldBeTheSameAs(persistence.Object);
	}
*/
