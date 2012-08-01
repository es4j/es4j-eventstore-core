package org.es4j.eventstore.core.conversiontests.EventUpconverterPipelineHook;

//import org.es4j.dotnet.Guid;
import java.util.UUID;

/**
 *
 * @author Esfand
 */
public class ConvertingEvent3 {

    private UUID    id;          // { get; set; }
    private String  name;        // { get; set; }
    private boolean imExplicit;  // { get; set; }

    public ConvertingEvent3(UUID id, String name, boolean imExplicit) {
        this.id         = id;
        this.name       = name;
        this.imExplicit = imExplicit;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isImExplicit() {
        return imExplicit;
    }

    public void setImExplicit(boolean imExplicit) {
        this.imExplicit = imExplicit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
