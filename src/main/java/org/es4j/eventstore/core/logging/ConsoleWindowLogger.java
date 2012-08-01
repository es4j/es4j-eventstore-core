package org.es4j.eventstore.core.logging;

import org.es4j.dotnet.Console;
import org.es4j.dotnet.ConsoleColor;
import org.es4j.util.logging.ILog;


public class ConsoleWindowLogger implements ILog {

    private static final Object Sync = new Object();
    private final ConsoleColor  originalColor = Console.ForegroundColor;
    private final Class          typeToLog;

    public ConsoleWindowLogger(Class typeToLog) {
        this.typeToLog = typeToLog;
    }

    @Override
    public void verbose(String message, Object... values) {
        this.log(ConsoleColor.DarkGreen, message, values);
    }

    @Override
    public void debug(String message, Object... values) {
        this.log(ConsoleColor.Green, message, values);
    }

    @Override
    public void info(String message, Object... values) {
        this.log(ConsoleColor.White, message, values);
    }

    @Override
    public void warn(String message, Object... values) {
        this.log(ConsoleColor.Yellow, message, values);
    }

    @Override
    public void error(String message, Object... values) {
        this.log(ConsoleColor.DarkRed, message, values);
    }

    @Override
    public void fatal(String message, Object... values) {
        this.log(ConsoleColor.Red, message, values);
    }

    private void log(ConsoleColor color, String message, Object... values) {
        synchronized (Sync) {
            Console.ForegroundColor = color;
            //Console.WriteLine(String.format(this.typeToLog, values));
            Console.ForegroundColor = this.originalColor;
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

}