package org.es4j.eventstore.core.dispatcher;

import org.es4j.eventstore.api.Commit;


public abstract class DispatcherDelegate<T extends Commit> {
    
    public abstract void dispatch(T commit);
    
}
