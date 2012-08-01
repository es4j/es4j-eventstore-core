package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_disposing_the_event_store extends UsingPersistence {
 
    @Test
    public void should_dispose_the_underlying_persistence() throws IOException {

        // Given
        establishContext();
        
        // When
        try {
            store.close();
        } catch (Exception ex) {
            Assert.fail("IO Exception thrown");
        }
        
        //Then
        try {           
            verify(persistence, times(1)).close();
        } catch (Exception ex) {
            Assert.fail("IO Exception thrown");
        }
    }
}


/*
	[Subject("OptimisticEventStore")]
	public class when_disposing_the_event_store : using_persistence
	{
		Because of = () =>
			store.Dispose();

		It should_dispose_the_underlying_persistence = () =>
			persistence.Verify(x => x.Dispose(), Times.Once());
	}
*/
