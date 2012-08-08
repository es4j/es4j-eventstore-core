package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

import org.es4j.eventstore.api.conversion.UpconvertEvents;


/**
 *
 * @author Esfand
 */
public class ExplicitConvertingEventConverter extends UpconvertEvents<ConvertingEvent2, ConvertingEvent3> {

    @Override
    public ConvertingEvent3 /*UpconvertEvents<ConvertingEvent2, ConvertingEvent3>.*/convert(ConvertingEvent2 sourceEvent) {
        return new ConvertingEvent3(sourceEvent.getId(), "Temp", true);
    }

}

