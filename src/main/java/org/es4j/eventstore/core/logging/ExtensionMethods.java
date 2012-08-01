package org.es4j.eventstore.core.logging;

import java.util.Locale;
import org.es4j.util.DateTime;
//import org.joda.time.DateTime;
//import org.es4j.dotnet.DateTime;

//using System.Globalization;
//using System.Threading;
//internal static


class ExtensionMethods {
    private static final String messageFormat = "{0:yyyy/MM/dd HH:mm:ss.ff} - {1} - {2} - {3}";

    public static String formatMessage(/*this*/ String message, Class/*Type*/ typeToLog, Object... values) {
        if(true) throw new UnsupportedOperationException("Not yet implemented");

        return String.format(Locale.getDefault(),
                             messageFormat,
                             DateTime.utcNow().toString(),
                             getName(Thread.currentThread()), //.CurrentThread.getName(),
                             typeToLog.getName(), //.FullName,
                             String.format(Locale.getDefault(), message, values));
    }

    private static String getName(/*this*/ Thread thread) {
        String threadName = thread.getName();
        if(threadName != null && !threadName.isEmpty()) {
            return threadName;
        }
        else {
            return Long.toString(thread.getId());
        }
        //return !String.IsNullOrEmpty(thread.Name)
	//			? thread.Name
	//			: thread.ManagedThreadId.ToString(CultureInfo.InvariantCulture);
	//	}
    }
}