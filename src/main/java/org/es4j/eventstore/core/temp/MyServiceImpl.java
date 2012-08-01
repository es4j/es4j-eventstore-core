package org.es4j.eventstore.core.temp;

public class MyServiceImpl {

    private MyServiceHelper helper;

    public void initiateApprovalState(Applicant applicant) {

        ApplicantProgress progress = new ApplicantProgress();
        progress.setStatus(ApplicantStatus.APPROVED);

        helper.approve(progress);
    }

    public void setHelper(MyServiceHelper helper) {
        this.helper = helper;
    }
}
