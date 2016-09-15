package org.openlmis.referencedata.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.referencedata.repository.SupplyLineRepository;
import org.openlmis.referencedata.service.SupplyLineService;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SupplyLineServiceTest {

  @Mock
  private SupplyLineRepository supplyLineRepository;

  @InjectMocks
  private SupplyLineService supplyLineService;

  @Test
  public void shouldFindSupplyLineIfMatchedProgramAndSupervisoryNode() {
    Program program = mock(Program.class);
    SupervisoryNode supervisoryNode = mock(SupervisoryNode.class);
    SupplyLine supplyLine = mock(SupplyLine.class);

    when(supplyLineRepository
            .searchSupplyLines(program, supervisoryNode))
            .thenReturn(Arrays.asList(supplyLine));

    List<SupplyLine> receivedSupplyLines = supplyLineService.searchSupplyLines(
        program, supervisoryNode);

    assertEquals(1, receivedSupplyLines.size());
    assertEquals(supplyLine, receivedSupplyLines.get(0));
  }
}
