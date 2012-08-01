package org.es4j.eventstore.core.DispatchCommitHookTests.DispatchSchedulerPipelinkHookTest;

import java.util.UUID;
import org.es4j.eventstore.core.DispatchSchedulerPipelineHook;
//import org.es4j.dotnet.DateTime;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.core.ExceptionAssert;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.util.DateTime;
//import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_the_hook_has_no_dispatcher_configured {

    // Given
    @UnderTest
    public IPipelineHook dispatchSchedulerHook = new DispatchSchedulerPipelineHook();

    @Test
    public void should_not_throw_an_exception() {

        // When
        dispatchSchedulerHook.postCommit(commit);

        // Then
        //thrown.ShouldBeNull();
    }


    public static final Commit commit = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);

    @Rule
    public ExceptionAssert thrown = new ExceptionAssert();

}


/*
[Subject("DispatchSchedulerPipelinkHook")]
public class when_the_hook_has_no_dispatcher_configured
{
    static readonly Commit commit = new Commit(
            Guid.NewGuid(), 0, Guid.NewGuid(), 0, DateTime.MinValue, null, null);
    static readonly DispatchSchedulerPipelineHook DispatchSchedulerHook = new DispatchSchedulerPipelineHook();
    static Exception thrown;

    Because of = () =>
        thrown = Catch.Exception(() => DispatchSchedulerHook.PostCommit(commit));

    It should_not_throw_an_exception = () =>
        thrown.ShouldBeNull();
}
*/
