package org.es4j.eventstore.core;

import java.util.UUID;
//import org.es4j.dotnet.DateTime;
//import org.es4j.dotnet.Guid;
import org.es4j.eventstore.api.Commit;
import org.es4j.eventstore.api.dispatcher.IScheduleDispatches;
import org.es4j.eventstore.core.temp.*;
import org.es4j.util.DateTime;
import static org.hamcrest.core.Is.is;
//import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class MyServiceImplTest {

    @Mock
    MyServiceHelper helper;

    @InjectMocks
    private MyServiceImpl myServiceImpl = new MyServiceImpl();

    @Captor
    ArgumentCaptor<ApplicantProgress> argument;

    @Spy
    Applicant applicant = new Applicant();

    @Test
    public void applyApprovalStateToApplicant(){

        // When
        myServiceImpl.initiateApprovalState(applicant);

        // Then
        verify(helper).approve(argument.capture());
        assertThat(argument.getValue().getStatus(), is(ApplicantStatus.APPROVED));
    }


    @Test
    public void Then1() {

        // When
        dispatchSchedulerHook.postCommit(commit);

        // Then
        verify(dispatcher, times(1)).scheduleDispatch(commit);
    }


    @InjectMocks
    IScheduleDispatches dispatcher = mock(IScheduleDispatches.class);

    @UnderTest
    DispatchSchedulerPipelineHook dispatchSchedulerHook = new DispatchSchedulerPipelineHook(dispatcher);

    private Commit commit = new Commit(UUID.randomUUID(), 0, UUID.randomUUID(), 0, new DateTime(0), null, null);

}