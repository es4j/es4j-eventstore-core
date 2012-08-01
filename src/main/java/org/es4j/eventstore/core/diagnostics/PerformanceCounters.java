package org.es4j.eventstore.core.diagnostics;

import org.es4j.dotnet.GC;
import org.es4j.dotnet.IDisposable;
import org.es4j.dotnet.PerformanceCounter;

//using System.Diagnostics;


//internal
class PerformanceCounters implements IDisposable {

    public void countCommit(int eventsCount, long elapsedMilliseconds) {
        this.totalCommits.increment();
        this.commitsRate.increment();
        this.avgCommitDuration.incrementBy(elapsedMilliseconds);
        this.avgCommitDurationBase.increment();
        this.totalEvents.incrementBy(eventsCount);
        this.eventsRate.incrementBy(eventsCount);
        this.undispatchedCommits.increment();
    }

    public void countSnapshot() {
        this.totalSnapshots.increment();
        this.snapshotsRate.increment();
    }

    public void countCommitDispatched() {
        this.undispatchedCommits.decrement();
    }

    static void PerformanceCountersFactory() {
        /*
        if (PerformanceCounterCategory.exists(CategoryName)) {
            return;
        }

        var counters = new CounterCreationDataCollection
        {
				new CounterCreationData(TotalCommitsName, "Total number of commits persisted", PerformanceCounterType.NumberOfItems32),
				new CounterCreationData(CommitsRateName, "Rate of commits persisted per second", PerformanceCounterType.RateOfCountsPerSecond32),
				new CounterCreationData(AvgCommitDuration, "Average duration for each commit", PerformanceCounterType.AverageTimer32),
				new CounterCreationData(AvgCommitDurationBase, "Average duration base for each commit", PerformanceCounterType.AverageBase),
				new CounterCreationData(TotalEventsName, "Total number of events persisted", PerformanceCounterType.NumberOfItems32),
				new CounterCreationData(EventsRateName, "Rate of events persisted per second", PerformanceCounterType.RateOfCountsPerSecond32),
				new CounterCreationData(TotalSnapshotsName, "Total number of snapshots persisted", PerformanceCounterType.NumberOfItems32),
				new CounterCreationData(SnapshotsRateName, "Rate of snapshots persisted per second", PerformanceCounterType.RateOfCountsPerSecond32),
				new CounterCreationData(UndispatchedQueue, "Undispatched commit queue length", PerformanceCounterType.CountPerTimeInterval32)
        };

			// TODO: add other useful counts such as:
			//
			//  * Total Commit Bytes
			//  * Average Commit Bytes
			//  * Total Queries
			//  * Queries Per Second
			//  * Average Query Duration
			//  * Commits per Query (Total / average / per second)
			//  * Events per Query (Total / average / per second)
			//
			// Some of these will involve hooking into other parts of the EventStore

			PerformanceCounterCategory.create(CategoryName, "EventStore Event-Sourcing Persistence", PerformanceCounterCategoryType.MultiInstance, counters);
*/
    }

    public PerformanceCounters(String instanceName) {
        this.totalCommits = new PerformanceCounter(CategoryName, TotalCommitsName, instanceName, false);
        this.commitsRate = new PerformanceCounter(CategoryName, CommitsRateName, instanceName, false);
        this.avgCommitDuration = new PerformanceCounter(CategoryName, AvgCommitDuration, instanceName, false);
        this.avgCommitDurationBase = new PerformanceCounter(CategoryName, AvgCommitDurationBase, instanceName, false);
        this.totalEvents = new PerformanceCounter(CategoryName, TotalEventsName, instanceName, false);
        this.eventsRate = new PerformanceCounter(CategoryName, EventsRateName, instanceName, false);
        this.totalSnapshots = new PerformanceCounter(CategoryName, TotalSnapshotsName, instanceName, false);
        this.snapshotsRate = new PerformanceCounter(CategoryName, SnapshotsRateName, instanceName, false);
        this.undispatchedCommits = new PerformanceCounter(UndispatchedQueue, SnapshotsRateName, instanceName, false);
    }

    @Override
    public void close() {
        dispose_FORNOW();
    }
    //~PerformanceCounters()
    public void PerformanceCounterDestructor() {
        this.dispose(false);
    }

    @Override
    public void dispose_FORNOW() {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virtual
    protected void dispose(boolean disposing) {
        this.totalCommits.close(); //.dispose();
        this.commitsRate.close(); //();
        this.avgCommitDuration.close(); //();
        this.avgCommitDurationBase.close(); //();
        this.totalEvents.close(); //();
        this.eventsRate.close(); //();
        this.totalSnapshots.close(); //();
        this.snapshotsRate.close(); //();
        this.undispatchedCommits.close(); //();
    }
    private static final String CategoryName = "EventStore";
    private static final String TotalCommitsName = "Total Commits";
    private static final String CommitsRateName = "Commits/Sec";
    private static final String AvgCommitDuration = "Average Commit Duration";
    private static final String AvgCommitDurationBase = "Average Commit Duration Base";
    private static final String TotalEventsName = "Total Events";
    private static final String EventsRateName = "Events/Sec";
    private static final String TotalSnapshotsName = "Total Snapshots";
    private static final String SnapshotsRateName = "Snapshots/Sec";
    private static final String UndispatchedQueue = "Undispatched Queue Length";
    private final PerformanceCounter totalCommits;
    private final PerformanceCounter commitsRate;
    private final PerformanceCounter avgCommitDuration;
    private final PerformanceCounter avgCommitDurationBase;
    private final PerformanceCounter totalEvents;
    private final PerformanceCounter eventsRate;
    private final PerformanceCounter totalSnapshots;
    private final PerformanceCounter snapshotsRate;
    private final PerformanceCounter undispatchedCommits;
}
