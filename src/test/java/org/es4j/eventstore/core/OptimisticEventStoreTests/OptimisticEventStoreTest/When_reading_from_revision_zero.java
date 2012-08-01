package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.ICommitEvents;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_reading_from_revision_zero extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(persistence.getFrom(streamId, 0, Integer.MAX_VALUE)).willReturn(new LinkedList());

        // When
        ((ICommitEvents)store).getFrom(streamId, 0, Integer.MAX_VALUE);
    }

    @Test
    public void should_pass_a_revision_range_to_the_persistence_infrastructure() {

        // Then
        verify(persistence, times(1)).getFrom(streamId, 0, Integer.MAX_VALUE);
    }

}



/*
[Subject("OptimisticEventStore")]
public class when_reading_from_revision_zero : using_persistence
{
		Establish context = () =>
			persistence.Setup(x => x.GetFrom(streamId, 0, int.MaxValue)).Returns(new Commit[] { });

		Because of = () =>
			((ICommitEvents)store).GetFrom(streamId, 0, int.MaxValue).ToList();

		It should_pass_a_revision_range_to_the_persistence_infrastructure = () =>
			persistence.Verify(x => x.GetFrom(streamId, 0, int.MaxValue), Times.Once());
}
*/

