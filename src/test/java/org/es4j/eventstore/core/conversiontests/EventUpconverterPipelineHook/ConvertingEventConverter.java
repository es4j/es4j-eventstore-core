package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

<<<<<<< HEAD
//import org.es4j.eventstore.api.conversion.IUpconvertEvents;
import org.es4j.eventstore.api.conversion.UpconvertEvents;

=======
import org.es4j.eventstore.api.conversion.UpconvertEvents;
>>>>>>> b18e09d391686c91b5e3c612b217b2e834403359


/**
 *
 * @author Esfand
 */
public class ConvertingEventConverter extends UpconvertEvents<ConvertingEvent, ConvertingEvent2> {

    @Override
    public ConvertingEvent2 convert(ConvertingEvent sourceEvent) {
        return new ConvertingEvent2(sourceEvent.getId(), "Temp");
    }
}
