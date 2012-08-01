package org.es4j.eventstore.core.logging;

import org.es4j.util.logging.ILog;

//using System.Diagnostics;


public class OutputWindowLogger implements ILog {

    private static final Object sync = new Object();
    private        final Class   typeToLog;

    public OutputWindowLogger(Class typeToLog) {
        this.typeToLog = typeToLog;
    }

    @Override
    public void verbose(String message, Object... values) {
        this.debugWindow("Verbose", message, values);
    }

    @Override
    public void debug(String message, Object... values) {
        this.debugWindow("Debug", message, values);
    }

    @Override
    public void info(String message, Object... values) {
        this.traceWindow("Info", message, values);
    }

    @Override
    public void warn(String message, Object... values) {
        this.traceWindow("Warn", message, values);
    }

    @Override
    public void error(String message, Object... values) {
        this.traceWindow("Error", message, values);
    }

    @Override
    public void fatal(String message, Object... values) {
        this.traceWindow("Fatal", message, values);
    }

    protected void debugWindow(String category, String message, Object... values) {
        synchronized (sync) {
            //System.Diagnostics.Debug.WriteLine(category, message.FormatMessage(this.typeToLog, values));
        }
    }

    protected void traceWindow(String category, String message, Object... values) {
        synchronized (sync) {
            //Trace.WriteLine(category, message.FormatMessage(this.typeToLog, values));
        }
    }
}