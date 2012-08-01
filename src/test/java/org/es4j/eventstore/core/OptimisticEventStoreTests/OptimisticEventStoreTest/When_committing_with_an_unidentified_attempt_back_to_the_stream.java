package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

//import org.es4j.dotnet.Guid;
import java.util.UUID;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.ICommitEvents;
//import org.es4j.eventstore.core.util.Consts;
import org.es4j.exceptions.ArgumentException;
import org.es4j.util.Consts;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_with_an_unidentified_attempt_back_to_the_stream extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        thrown.expect(ArgumentException.class);

        // When
        ((ICommitEvents)store).commit(unidentified);
    }

    @Test
    public void should_throw_an_ArgumentException() {

        // Then thrown.ShouldBeOfType<ArgumentException>();
        //
    }


    static final UUID   emptyIdentifier = Consts.EMPTY_UUID;
    static final Commit unidentified = buildCommitStub(emptyIdentifier);

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}



/*
[Subject("OptimisticEventStore")]
public class when_committing_with_an_unidentified_attempt_back_to_the_stream : using_persistence
{
		static readonly Guid emptyIdentifier = Guid.Empty;
		static readonly Commit unidentified = BuildCommitStub(emptyIdentifier);
		static Exception thrown;

		Because of = () =>
			thrown = Catch.Exception(() => ((ICommitEvents)store).Commit(unidentified));

		It should_throw_an_ArgumentException = () =>
			thrown.ShouldBeOfType<ArgumentException>();
}
*/
