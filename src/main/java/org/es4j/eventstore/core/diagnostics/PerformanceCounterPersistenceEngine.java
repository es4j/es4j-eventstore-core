package org.es4j.eventstore.core.diagnostics;

import java.util.UUID;
//import org.es4j.dotnet.DateTime;
import org.es4j.dotnet.GC;
import org.es4j.dotnet.Stopwatch;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.Snapshot;
import org.es4j.eventstore.api.persistence.IPersistStreams;
import org.es4j.eventstore.api.persistence.StreamHead;
import org.es4j.util.DateTime;
//import org.joda.time.DateTime;

//using System.Diagnostics;
//using Persistence;


public class PerformanceCounterPersistenceEngine implements IPersistStreams {

    @Override // virtusl
    public void initialize() {
        this.persistence.initialize();
    }

    @Override // virtual
    public void commit(Commit attempt) {
        Stopwatch clock = Stopwatch.startNew();
        this.persistence.commit(attempt);
        clock.stop();

        this.counters.countCommit(attempt.getEvents().size(), clock.getElapsedMilliseconds());
    }

    @Override // virtual
    public void markCommitAsDispatched(Commit commit) {
        this.persistence.markCommitAsDispatched(commit);
        this.counters.countCommitDispatched();
    }

    @Override // virtual
    public Iterable<Commit> getUndispatchedCommits() {
        return this.persistence.getUndispatchedCommits();
    }

    @Override // virtual
    public Iterable<Commit> getFrom(UUID streamId, int minRevision, int maxRevision) {
        return this.persistence.getFrom(streamId, minRevision, maxRevision);
    }

    @Override // virtual
    public Iterable<Commit> getFrom(DateTime start) {
        return this.persistence.getFrom(start);
    }

    @Override // boolean
    public boolean addSnapshot(Snapshot snapshot) {
        boolean result = this.persistence.addSnapshot(snapshot);
        if (result) {
            this.counters.countSnapshot();
        }
        return result;
    }

    @Override // virtual
    public Snapshot getSnapshot(UUID streamId, int maxRevision) {
        return this.persistence.getSnapshot(streamId, maxRevision);
    }

    @Override // virtual
    public Iterable<StreamHead> getStreamsToSnapshot(int maxThreshold) {
        return this.persistence.getStreamsToSnapshot(maxThreshold);
    }

    @Override // virutal
    public void purge() {
        this.persistence.purge();
    }

    public PerformanceCounterPersistenceEngine(IPersistStreams persistence, String instanceName) {
        this.persistence = persistence;
        this.counters = new PerformanceCounters(instanceName);
    }

    //~PerformanceCounterPersistenceEngine()
    public void PerformanceCounterPersistenceEngineDestructor() throws Exception {
        this.dispose(false);
    }

    @Override
    public void close() throws Exception {
        dispose_FORNOW();
    }
    @Override
    public void dispose_FORNOW() throws Exception {
        this.dispose(true);
        GC.suppressFinalize(this);
    }

    // virtual
    protected void dispose(boolean disposing) throws Exception {
        if (!disposing) {
            return;
        }

        this.counters   .close(); //.dispose();
        this.persistence.close(); //.dispose();
    }

    private final PerformanceCounters counters;
    private final IPersistStreams persistence;
}
