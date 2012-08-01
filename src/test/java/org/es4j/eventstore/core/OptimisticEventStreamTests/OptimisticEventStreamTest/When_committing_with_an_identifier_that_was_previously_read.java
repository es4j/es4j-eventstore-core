package org.es4j.eventstore.core.OptimisticEventStreamTests.OptimisticEventStreamTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.DuplicateCommitException;
import org.es4j.eventstore.api.ICommitEvents;
import org.es4j.eventstore.core.OptimisticEventStream;
import org.es4j.eventstore.core.UnderTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import static org.mockito.Mockito.mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_committing_with_an_identifier_that_was_previously_read extends OnTheEventStream {

    @UnderTest
    private OptimisticEventStream stream;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static UUID                   dupliateCommitId;

    @Before
    public void given() throws Exception {

        // Given
        UUID         streamId    = UUID.randomUUID();

        List<Commit> committed   = Arrays.asList(buildCommitStub(streamId, 1, 1, 1));

        ICommitEvents persistence = mock(ICommitEvents.class);
        BDDMockito.given(persistence.getFrom(streamId, 0, Integer.MAX_VALUE)).willReturn(committed);

        stream           = new OptimisticEventStream(streamId, persistence, 0, Integer.MAX_VALUE);
        dupliateCommitId = committed.get(0).getCommitId();
        thrown.expect(DuplicateCommitException.class);
    }

    @Test
    public void should_throw_a_DuplicateCommitException() {

        // When
        stream.commitChanges(dupliateCommitId);

        // Then
        // if no Exception, the test will fail.
    }
}



/*
// This behavior is primarily to support a NoSQL storage solution where CommitId is not being used as the "primary key"
// in a NoSQL environment, we'll most likely use StreamId + CommitSequence, which also enables optimistic concurrency.
[Subject("OptimisticEventStream")]
public class when_committing_with_an_identifier_that_was_previously_read : on_the_event_stream
{
    static readonly Commit[] Committed = new[] { BuildCommitStub(1, 1, 1) };
    static readonly Guid DupliateCommitId = Committed[0].CommitId;
    static Exception thrown;

    Establish context = () =>
    {
        persistence
            .Setup(x => x.GetFrom(streamId, 0, int.MaxValue))
            .Returns(Committed);

        stream = new OptimisticEventStream(
            streamId, persistence.Object, 0, int.MaxValue);
    };

    Because of = () =>
        thrown = Catch.Exception(() => stream.CommitChanges(DupliateCommitId));

    It should_throw_a_DuplicateCommitException = () =>
        thrown.ShouldBeOfType<DuplicateCommitException>();
}
 */


