package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

//import org.es4j.dotnet.Guid;

import java.util.UUID;



/**
 *
 * @author Esfand
 */
public class ConvertingEvent2 {

    private UUID   id;   // { get; set; }
    private String name; // { get; set; }

    public ConvertingEvent2(UUID id, String name) {
        this.id   = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
