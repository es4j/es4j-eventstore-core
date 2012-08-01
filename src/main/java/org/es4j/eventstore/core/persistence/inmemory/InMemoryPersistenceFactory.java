package org.es4j.eventstore.core.persistence.inmemory;

import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.api.persistence.IPersistenceFactory;


public class InMemoryPersistenceFactory implements IPersistenceFactory {

    @Override // virtual
    public IPersistStreams build() {
        return new InMemoryPersistenceEngine();
    }
}
