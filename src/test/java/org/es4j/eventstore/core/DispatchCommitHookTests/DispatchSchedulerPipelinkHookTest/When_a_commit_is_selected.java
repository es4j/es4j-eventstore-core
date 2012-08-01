package org.es4j.eventstore.core.DispatchCommitHookTests.DispatchSchedulerPipelinkHookTest;

import java.util.UUID;
import org.es4j.eventstore.core.DispatchSchedulerPipelineHook;
//import org.es4j.dotnet.DateTime;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.util.DateTime;
import static org.hamcrest.core.Is.is;
//import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_a_commit_is_selected {

    @Test
    public void should_always_return_the_exact_same_commit() {

        // When
        selected = dispatchSchedulerHook.select(commit);

        // Then
        assertThat(selected, is(commit));
    }


    static final Commit commit = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);
    Commit selected;

    @UnderTest
    IPipelineHook dispatchSchedulerHook = new DispatchSchedulerPipelineHook();
}


/*
[Subject("DispatchSchedulerPipelinkHook")]
public class when_a_commit_is_selected
{
    static readonly Commit commit = new Commit(
            Guid.NewGuid(), 0, Guid.NewGuid(), 0, DateTime.MinValue, null, null);
    static readonly DispatchSchedulerPipelineHook DispatchSchedulerHook = new DispatchSchedulerPipelineHook();
    static Commit selected;

    Because of = () =>
        selected = DispatchSchedulerHook.Select(commit);

    It should_always_return_the_exact_same_commit = () =>
        ReferenceEquals(selected, commit).ShouldBeTrue();
}
*/
