package org.es4j.eventstore.core;

import org.es4j.eventstore.api.Commit;
import java.util.Iterator;

public class IterableCounter implements Iterable<Commit> {

    private final Iterable<Commit> iterable;
    private int   iteratorCallCount; // { get; private set; }

    public IterableCounter(Iterable<Commit> iterable) {
        this.iterable = iterable;
        this.iteratorCallCount = 0;
    }
    
    public int getIteratorCallCount() {
        return iteratorCallCount;
    }

    @Override
    public Iterator<Commit> iterator() {
        this.iteratorCallCount++;
        return this.iterable.iterator();
    }
}