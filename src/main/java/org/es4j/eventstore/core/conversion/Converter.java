package org.es4j.eventstore.core.conversion;


public abstract class Converter<TSource, TTarget> {
    public abstract TTarget convert(TSource source);
}
