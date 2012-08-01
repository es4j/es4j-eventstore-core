package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

import java.util.UUID;
//import org.es4j.dotnet.DateTime;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.DateTime;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_a_commit_that_does_not_have_convertible_events extends UsingEventConverter {

    static final   Commit commit       = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);
    private static Commit converted;


    @Before
    public void setUp() {

        // Given
        establishContext();
        commit.getEvents().add(new EventMessage(new NonConvertingEvent()));

        // When
        converted = eventUpconverter.select(commit);
    }

    @Test
    public void should_not_be_converted() {

        // Then
        assertThat(converted, is(commit)); //ShouldBeTheSameAs(commit);
    }

    @Test
    public void should_have_the_same_instance_of_the_event() {

        // Then
        fail(); //converted.getEvents().Single().ShouldEqual(commit.getEvents().Single());
    }
}



/*
[Subject("EventUpconverterPipelineHook")]
public class when_opening_a_commit_that_does_not_have_convertible_events : using_event_converter
{
		static readonly Commit commit = new Commit(
			Guid.NewGuid(), 0, Guid.NewGuid(), 0, DateTime.MinValue, null, null);
		static Commit converted;

		Establish context = () =>
			commit.Events.Add(new EventMessage { Body = new NonConvertingEvent() });

		Because of = () =>
			converted = eventUpconverter.Select(commit);

		It should_not_be_converted = () =>
			converted.ShouldBeTheSameAs(commit);

		It should_have_the_same_instance_of_the_event = () =>
			converted.Events.Single().ShouldEqual(commit.Events.Single());
}
*/
