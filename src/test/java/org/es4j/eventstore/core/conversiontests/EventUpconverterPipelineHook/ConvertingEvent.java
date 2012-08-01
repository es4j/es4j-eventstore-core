package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

//import org.es4j.dotnet.Guid;

import java.util.UUID;


/**
 *
 * @author Esfand
 */
public class ConvertingEvent {

    private UUID id; // { get; set; }

    public ConvertingEvent(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
