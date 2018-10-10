package fr.avalonlab.warp10.DSL;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

class GTSOutputTest {

  @Test
  public void parseWarp10GtsSingleOutput() {
    String singleOutput = "{\"c\":\"fr.avalonlab.dashboard.category.domain.Category\",\"l\":{\"label\":\"ju+in\",\"type\":\"DEBIT\",\"category\":\"15e99-0d2a-\",\".app\":\"dashboard\"},\"a\":{},\"v\":[[1529964000000000,4000]]}";

    GTSOutput gts = GTSOutput.fromOutputFormat(singleOutput);

    assertThat(gts.getId()).isEmpty();
    assertThat(gts.getClassName()).isEqualTo("fr.avalonlab.dashboard.category.domain.Category");
    assertThat(gts.getValue()).isEqualTo("4000");
    assertThat(gts.getMsTimestamp()).isEqualTo(1529964000000000L);
    assertThat(gts.getAttributes()).isEmpty();
    assertThat(gts.getLabels()).containsOnly(Map.entry("label", "ju+in"), Map.entry("type", "DEBIT"), Map.entry("category","15e99-0d2a-"), Map.entry(".app", "dashboard"));
  }
}
