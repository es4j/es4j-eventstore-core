package org.es4j.eventstore.core.temp;

public class ApplicantProgress {

    private ApplicantStatus applicantStatus = ApplicantStatus.PENDING;

    public ApplicantStatus getStatus() {
        return applicantStatus;
    }

    public void setStatus(ApplicantStatus applicantStatus) {
        this.applicantStatus = applicantStatus;
    }
}

