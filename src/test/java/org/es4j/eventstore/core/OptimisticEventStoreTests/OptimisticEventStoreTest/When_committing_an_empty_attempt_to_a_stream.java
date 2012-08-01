package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_an_empty_attempt_to_a_stream extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();

        // When
        ((ICommitEvents)store).commit(attemptWithNoEvents);
    }

    @Test
    public void should_drop_the_commit_provided() {

        //Then
        verify(persistence, times(0)).commit(attemptWithNoEvents);
    }

    static final Commit attemptWithNoEvents = buildCommitStub(UUID.randomUUID());
}


/*
[Subject("OptimisticEventStore")]
public class when_committing_an_empty_attempt_to_a_stream : using_persistence
{
    static readonly Commit attemptWithNoEvents = BuildCommitStub(Guid.NewGuid());

    Establish context = () =>
        persistence.Setup(x => x.Commit(attemptWithNoEvents));

    Because of = () =>
        ((ICommitEvents)store).Commit(attemptWithNoEvents);

    It should_drop_the_commit_provided = () =>
        persistence.Verify(x => x.Commit(attemptWithNoEvents), Times.AtMost(0));
}
*/
