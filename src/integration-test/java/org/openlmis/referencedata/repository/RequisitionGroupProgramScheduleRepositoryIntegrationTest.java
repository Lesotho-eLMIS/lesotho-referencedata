package org.openlmis.referencedata.repository;

import org.junit.Before;
import org.openlmis.referencedata.domain.ProcessingSchedule;
import org.openlmis.referencedata.domain.Program;
import org.openlmis.referencedata.domain.RequisitionGroupProgramSchedule;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Allow testing requisitionGroupProgramScheduleRepository.
 */
public class RequisitionGroupProgramScheduleRepositoryIntegrationTest
    extends BaseCrudRepositoryIntegrationTest<RequisitionGroupProgramSchedule> {

  @Autowired
  RequisitionGroupProgramScheduleRepository repository;

  @Autowired
  ProgramRepository programRepository;

  @Autowired
  ProcessingScheduleRepository scheduleRepository;

  private Program program;
  private ProcessingSchedule schedule;

  RequisitionGroupProgramScheduleRepository getRepository() {
    return this.repository;
  }

  RequisitionGroupProgramSchedule generateInstance() {
    RequisitionGroupProgramSchedule requisitionGroupProgramSchedule =
        new RequisitionGroupProgramSchedule();
    requisitionGroupProgramSchedule.setProgram(program);
    requisitionGroupProgramSchedule.setProcessingSchedule(schedule);
    requisitionGroupProgramSchedule.setDirectDelivery(false);
    return requisitionGroupProgramSchedule;
  }

  @Before
  public void setUp() {
    final String code = "RequisitionGroup";

    program = new Program(code);
    programRepository.save(program);

    schedule = new ProcessingSchedule();
    schedule.setCode(code);
    schedule.setName(code);
    scheduleRepository.save(schedule);
  }
}
