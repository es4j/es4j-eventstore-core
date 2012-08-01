package org.es4j.eventstore.core.dispatchertests.SynchronousDispatcherTests;

import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IDispatchCommits;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.eventstore.core.dispatcher.SynchronousDispatchScheduler;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_disposing_the_synchronous_dispatch_scheduler {

    @Before
    public void setUp() throws IOException {
        
        // Given
        MockitoAnnotations.initMocks(this);
        given(persistence.getUndispatchedCommits()).willReturn(new LinkedList<Commit>());
        dispatchScheduler = new SynchronousDispatchScheduler(dispatcher, persistence);

        // When
        try {
            dispatchScheduler.close();
            dispatchScheduler.close();
        } catch (Exception ex) {
            Assert.fail("Exception thrown");
        }
    }

    @Test 
    public void should_dispose_the_underlying_dispatcher_exactly_once() {
        
        // Then
        try {
            verify(dispatcher, times(1)).close();
        } catch (Exception ex) {
            Assert.fail("Exception thrown");
        }
    }

    @Test 
    public void should_dispose_the_underlying_persistence_infrastructure_exactly_once() {

        // Then
        try {
            verify(persistence, times(1)).close();
        } catch (Exception ex) {
            Assert.fail("Exception thrown");
        }
    }


    @Mock
    IDispatchCommits dispatcher; //  = mock(IDispatchCommits.class);
    @Mock
    IPersistStreams  persistence; // = mock(IPersistStreams.class);

    @UnderTest
    SynchronousDispatchScheduler dispatchScheduler;
}


/*
[Subject("SynchronousDispatchScheduler")]
public class when_disposing_the_synchronous_dispatch_scheduler
{
    static readonly Mock<IDispatchCommits> dispatcher = new Mock<IDispatchCommits>();
    static readonly Mock<IPersistStreams> persistence = new Mock<IPersistStreams>();
    static SynchronousDispatchScheduler dispatchScheduler;

    Establish context = () =>
    {
        dispatcher.Setup(x => x.Dispose());
        persistence.Setup(x => x.Dispose());
        dispatchScheduler = new SynchronousDispatchScheduler(dispatcher.Object, persistence.Object);
    };

    Because of = () =>
    {
			dispatchScheduler.Dispose();
			dispatchScheduler.Dispose();
    };

    It should_dispose_the_underlying_dispatcher_exactly_once = () =>
        dispatcher.Verify(x => x.Dispose(), Times.Once());

    It should_dispose_the_underlying_persistence_infrastructure_exactly_once = () =>
        dispatcher.Verify(x => x.Dispose(), Times.Once());
}
 */