package org.es4j.eventstore.core.OptimisticCommitHookTests.OptimisticCommitHookTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import org.es4j.dotnet.Guid;
//import org.es4j.dotnet.SystemTime;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.core.OptimisticPipelineHook;
import org.es4j.eventstore.core.UnderTest;
import org.es4j.util.Consts;
//import org.es4j.eventstore.core.util.Consts;
import org.es4j.util.SystemTime;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class When_tracking_commits {

    @Before
    public void setUp() {

        // Given
        hook = new OptimisticPipelineHook(maxStreamsToTrack);

        // When
        for (Commit commit : trackedCommits) {
            hook.track(commit);
        }
    }

    @Test
    public void should_only_contain_streams_explicitly_tracked() {

        // Then
        Commit untracked = buildCommit(Consts.EMPTY_UUID, trackedCommits.get(0).getCommitId());
        assertFalse(hook.contains(untracked));
    }

    @Test
    public void should_find_tracked_streams() {

        // Then
        Commit stillTracked = buildCommit(trackedCommits.get(trackedCommits.size()-1).getStreamId(),
                                          trackedCommits.get(trackedCommits.size()-1).getCommitId());
        assertTrue(hook.contains(stillTracked));
    };

    @Test
    public void should_only_track_the_specified_number_of_streams() {

        // Then
        Commit droppedFromTracking = buildCommit(trackedCommits.get(0).getStreamId(),
                                                 trackedCommits.get(0).getCommitId());
        assertFalse(hook.contains(droppedFromTracking));
    };


    private static Commit buildCommit(UUID streamId, UUID commitId) {
        return new Commit(streamId, 0, commitId, 0, SystemTime.utcNow(), null, null);
    }

    static final int maxStreamsToTrack = 2;

    //static final Guid     streamId = Guid.newGuid();
    static final List<Commit> trackedCommits = Arrays.asList(
        buildCommit(UUID.randomUUID(), UUID.randomUUID()),
        buildCommit(UUID.randomUUID(), UUID.randomUUID()),
        buildCommit(UUID.randomUUID(), UUID.randomUUID())
    );

    @UnderTest
    static OptimisticPipelineHook hook;  // IPipelineHook
}


/*
[Subject("CommitTracker")]
public class when_tracking_commits
{
    const int MaxStreamsToTrack = 2;
    static readonly Guid StreamId = Guid.NewGuid();
    static readonly Commit[] TrackedCommits = new[]
    {
        BuildCommit(Guid.NewGuid(), Guid.NewGuid()),
        BuildCommit(Guid.NewGuid(), Guid.NewGuid()),
        BuildCommit(Guid.NewGuid(), Guid.NewGuid())
    };

    static OptimisticPipelineHook hook;

    Establish context = () =>
        hook = new OptimisticPipelineHook(MaxStreamsToTrack);

    Because of = () =>
    {
        foreach (var commit in TrackedCommits)
            hook.Track(commit);
    };

		It should_only_contain_streams_explicitly_tracked = () =>
		{
			var untracked = BuildCommit(Guid.Empty, TrackedCommits[0].CommitId);
			hook.Contains(untracked).ShouldBeFalse();
		};

		It should_find_tracked_streams = () =>
		{
			var stillTracked = BuildCommit(TrackedCommits.Last().StreamId, TrackedCommits.Last().CommitId);
			hook.Contains(stillTracked).ShouldBeTrue();
		};

		It should_only_track_the_specified_number_of_streams = () =>
		{
			var droppedFromTracking = BuildCommit(
				TrackedCommits.First().StreamId, TrackedCommits.First().CommitId);
			hook.Contains(droppedFromTracking).ShouldBeFalse();
		};

		private static Commit BuildCommit(Guid streamId, Guid commitId)
		{
			return new Commit(streamId, 0, commitId, 0, SystemTime.UtcNow, null, null);
		}
	}
*/
