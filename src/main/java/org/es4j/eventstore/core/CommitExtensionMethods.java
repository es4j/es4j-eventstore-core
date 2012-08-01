package org.es4j.eventstore.core;

import org.es4j.eventstore.api.Commit;
//import org.es4j.eventstore.core.util.Consts;
import org.es4j.exceptions.ArgumentException;
import org.es4j.exceptions.ArgumentNullException;
import org.es4j.util.Consts;


// internal static
public class CommitExtensionMethods {

    public static boolean isValid(/*this*/ Commit attempt) {

        if (attempt == null) {
	    throw new ArgumentNullException("attempt");
        }

        if (!hasIdentifier(attempt)) {
            throw new ArgumentException(Resources.CommitsMustBeUniquelyIdentified(), "attempt");
        }

        if (attempt.getCommitSequence() <= 0) {
            throw new ArgumentException(Resources.NonPositiveSequenceNumber(), "attempt");
        }

        if (attempt.getStreamRevision() <= 0) {
            throw new ArgumentException(Resources.NonPositiveRevisionNumber(), "attempt");
        }

        if (attempt.getStreamRevision() < attempt.getCommitSequence()) {
            throw new ArgumentException(Resources.RevisionTooSmall(), "attempt");
        }

        return true;
    }

    public static boolean hasIdentifier(/*this*/ Commit attempt) {
        return attempt.getStreamId() != Consts.EMPTY_UUID && attempt.getCommitId() != Consts.EMPTY_UUID;
    }

    public static boolean isEmpty(/*this*/ Commit attempt) {
        return attempt == null || attempt.getEvents().isEmpty();
    }
}