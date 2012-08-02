package org.es4j.eventstore.core.conversion;

import java.util.Map;
import org.es4j.dotnet.GC;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.IPipelineHook;
import org.es4j.eventstore.core.Resources;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.util.logging.ILog;
import org.es4j.util.logging.LogFactory;
import org.es4j.messaging.api.EventMessage;

public class EventUpconverterPipelineHook implements IPipelineHook {

    private static final ILog logger = LogFactory.buildLogger(EventUpconverterPipelineHook.class);

    //private final Map<Type, Func2<Object, Object>> converters;
    private final Map<Class, Converter<Object, Object>> converters;

    public EventUpconverterPipelineHook(Map<Class, Converter<Object, Object>> converters) {
        if (converters == null) {
            throw new ArgumentNullException("converters");
        }
        this.converters = converters;
    }

    @Override
    public void close() {
        dispose();
    }
    @Override
    public void dispose() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virutal
    protected void dispose(boolean disposing) {
        this.converters.clear();
    }

    // virtual
    @Override
    public Commit select(Commit committed) {
        for (EventMessage eventMessage : committed.getEvents()) {
            eventMessage.setBody(this.convert(eventMessage.getBody()));
        }
        return committed;
    }

    private <TTarget, TSource> TTarget convert(TSource source) {
        Converter<Object, Object> converter;

        if(!this.converters.containsKey(source.getClass())) {
            return (TTarget)source;
        }
        converter = this.converters.get(source.getClass());

        Object target = converter.convert(source);
        logger.debug(Resources.ConvertingEvent(), source.getClass().getName(), target.getClass().getName());

        return this.convert(target);
    }

    // virtual
    @Override
    public boolean preCommit(Commit attempt) {
        return true;
    }

    // virtual
    @Override
    public void postCommit(Commit committed) {
    }
}