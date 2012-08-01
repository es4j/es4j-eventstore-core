package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.Snapshot;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_reading_from_a_snapshot_up_to_revision_revision_zero extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(persistence.getFrom(streamId, snapshot.getStreamRevision(), Integer.MAX_VALUE))
             .willReturn(Arrays.asList(committed));
        
        // When
        store.openStream(snapshot, 0);
    }

    @Test
    public void should_pass_the_maximum_possible_revision_to_the_persistence_infrastructure() {

        // Then
        verify(persistence, times(1)).getFrom(streamId, snapshot.getStreamRevision(), Integer.MAX_VALUE);
    }

    
    static final Snapshot snapshot  = new Snapshot(streamId, 1, "snapshot");
    static final Commit   committed = buildCommitStub(1, 1);
}



/*
[Subject("OptimisticEventStore")]
public class when_reading_from_a_snapshot_up_to_revision_revision_zero : using_persistence
	{
		static readonly Snapshot snapshot = new Snapshot(streamId, 1, "snapshot");
		static readonly Commit Committed = BuildCommitStub(1, 1);

		Establish context = () => persistence
			.Setup(x => x.GetFrom(streamId, snapshot.StreamRevision, int.MaxValue))
			.Returns(new[] { Committed });

		Because of = () =>
			store.OpenStream(snapshot, 0);

		It should_pass_the_maximum_possible_revision_to_the_persistence_infrastructure = () =>
			persistence.Verify(x => x.GetFrom(streamId, snapshot.StreamRevision, int.MaxValue), Times.Once());
}
*/