package org.es4j.eventstore.core.OptimisticEventStoreTests.OptimisticEventStoreTest;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.StreamNotFoundException;
import java.util.LinkedList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_an_empty_stream_starting_above_revision_zero extends UsingPersistence {

    @Before
    public void setUp() {

        // Given
        establishContext();
        given(persistence.getFrom(streamId, minRevision, Integer.MAX_VALUE)).willReturn(new LinkedList<Commit>());

        // When
        thrown.expect(StreamNotFoundException.class);
        store.openStream(streamId, minRevision, Integer.MAX_VALUE);
    }

    @Test 
    public void should_throw_a_StreamNotFoundException() {

        // Then thrown.ShouldBeOfType<StreamNotFoundException>();
        //
    }
    

    static final int minRevision = 1;
   
    @Rule
    public static final ExpectedException thrown = ExpectedException.none();
}




/*
[Subject("OptimisticEventStore")]
public class when_opening_an_empty_stream_starting_above_revision_zero : using_persistence
{
    const int MinRevision = 1;
    static Exception thrown;

    Establish context = () =>
			persistence.Setup(x => x.GetFrom(streamId, MinRevision, int.MaxValue)).Returns(new Commit[0]);

    Because of = () =>
			thrown = Catch.Exception(() => store.OpenStream(streamId, MinRevision, int.MaxValue));

    It should_throw_a_StreamNotFoundException = () =>
        thrown.ShouldBeOfType<StreamNotFoundException>();
}
*/

