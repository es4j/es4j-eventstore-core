package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

import org.es4j.eventstore.api.conversion.IUpconvertEvents;


/**
 *
 * @author Esfand
 */
public class ConvertingEventConverter implements IUpconvertEvents<ConvertingEvent, ConvertingEvent2> {

    @Override
    public ConvertingEvent2 convert(ConvertingEvent sourceEvent) {
        return new ConvertingEvent2(sourceEvent.getId(), "Temp");
    }
}
