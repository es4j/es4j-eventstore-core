package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

import java.util.UUID;
//import org.es4j.dotnet.DateTime;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.messaging.api.EventMessage;
import org.es4j.util.DateTime;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_opening_a_commit_that_has_convertible_events extends UsingEventConverter {

    static final Commit       commit       = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);
    static final UUID         id           = UUID.randomUUID();
    static final EventMessage eventMessage = new EventMessage(new ConvertingEvent(id));

    private static Commit     converted;

    @Before
    public void setUp() {

        // Given
        establishContext();
        commit.getEvents().add(eventMessage);

        // When
        converted = eventUpconverter.select(commit);
    }

    @Test
    public void should_be_of_the_converted_type() {

        // Then
        fail(); //converted.getEvents().Single().Body.GetType().ShouldEqual(typeof(ConvertingEvent3));
    }

    @Test
    public void should_have_the_same_id_of_the_commited_event() {

        // Then
        fail(); //((ConvertingEvent3) converted.getEvents().Single().Body).getId().ShouldEqual(id);
    }
}


/*
[Subject("EventUpconverterPipelineHook")]
public class when_opening_a_commit_that_has_convertible_events : using_event_converter
{
		static readonly Commit commit = new Commit(
			Guid.NewGuid(), 0, Guid.NewGuid(), 0, DateTime.MinValue, null, null);
		static readonly Guid id = Guid.NewGuid();
		static readonly EventMessage eventMessage = new EventMessage
		{
			Body = new ConvertingEvent(id)
		};
		static Commit converted;

		Establish context = () =>
			commit.Events.Add(eventMessage);

		Because of = () =>
			converted = eventUpconverter.Select(commit);

		It should_be_of_the_converted_type = () =>
			converted.Events.Single().Body.GetType().ShouldEqual(typeof(ConvertingEvent3));

		It should_have_the_same_id_of_the_commited_event = () =>
			((ConvertingEvent3)converted.Events.Single().Body).Id.ShouldEqual(id);
}

*/
