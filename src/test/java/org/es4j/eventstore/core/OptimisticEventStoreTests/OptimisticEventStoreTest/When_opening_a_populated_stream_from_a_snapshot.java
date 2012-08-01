package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.Snapshot;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_a_populated_stream_from_a_snapshot extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(persistence.getFrom(streamId, 42, maxRevision)).willReturn(committed);

        // When
        store.openStream(snapshot, maxRevision);
    }

    @Test
    public void should_query_the_underlying_storage_using_the_revision_of_the_snapshot() {
        
        // Then
        verify(persistence, times(1)).getFrom(streamId, 42, maxRevision);
    }


    static final int maxRevision = Integer.MAX_VALUE;

    static final Snapshot     snapshot  = new Snapshot(streamId, 42, "snapshot");
    static final List<Commit> committed = Arrays.asList(buildCommitStub(42, 0));
}




/*
[Subject("OptimisticEventStore")]
public class when_opening_a_populated_stream_from_a_snapshot : using_persistence
{
    const int MaxRevision = int.MaxValue;
    static readonly Snapshot snapshot = new Snapshot(streamId, 42, "snapshot");
    static readonly Commit[] Committed = new[] { BuildCommitStub(42, 0) };

    Establish context = () =>
			persistence.Setup(x => x.GetFrom(streamId, 42, MaxRevision)).Returns(Committed);

    Because of = () =>
			store.OpenStream(snapshot, MaxRevision);

    It should_query_the_underlying_storage_using_the_revision_of_the_snapshot = () =>
			persistence.Verify(x => x.GetFrom(streamId, 42, MaxRevision), Times.Once());
}
*/
