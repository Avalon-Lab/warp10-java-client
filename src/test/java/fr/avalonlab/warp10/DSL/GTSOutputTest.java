package fr.avalonlab.warp10.DSL;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class GTSOutputTest {

  @Test
  public void parseWarp10GtsSingleOutput() {
    String singleOutput = "{\"c\":\"fr.avalonlab.dashboard.category.domain.Category\",\"l\":{\"label\":\"ju+in\",\"type\":\"DEBIT\",\"category\":\"15e99-0d2a\",\".app\":\"dashboard\"},\"a\":{},\"v\":[[1529964000000000,4000]]}";

    GTSOutput gts = GTSOutput.fromOutputFormat(singleOutput);

    assertThat(gts.getId()).isEmpty();
    assertThat(gts.getClassName()).isEqualTo("fr.avalonlab.dashboard.category.domain.Category");
    assertThat(gts.getPoints()).containsOnly(DataPoint.of("4000", 1529964000000000L));
    assertThat(gts.getAttributes()).isEmpty();
    assertThat(gts.getLabels()).containsOnly(Map.entry("label", "ju+in"), Map.entry("type", "DEBIT"), Map.entry("category","15e99-0d2a"), Map.entry(".app", "dashboard"));
  }


  @Test
  public void parseWarp10GtsSingleOutputWithMultipleValue() {
    String warp10Output = "{\"c\":\"fr.avalonlab.dashboard.category.domain.Category\",\"l\":{\"type\":\"CREDIT\",\"category\":\"b414301f-08b3\",\".app\":\"dashboard\"},\"a\":{},\"v\":[[1527152658209000,50.27],[1529744658209000,150.8],[1532336658209000,600]]}";

    GTSOutput gts = GTSOutput.fromOutputFormat(warp10Output);

    assertThat(gts.getId()).isEmpty();
    assertThat(gts.getClassName()).isEqualTo("fr.avalonlab.dashboard.category.domain.Category");
    assertThat(gts.getPoints()).containsOnly(DataPoint.of("50.27", 1527152658209000L),DataPoint.of("150.8", 1529744658209000L),DataPoint.of("600", 1532336658209000L));
    assertThat(gts.getAttributes()).isEmpty();
    assertThat(gts.getLabels()).containsOnly(Map.entry("type", "CREDIT"), Map.entry("category","b414301f-08b3"), Map.entry(".app", "dashboard"));
  }

  @Test
  public void parseWarp10GtsComplexOutput() {
    String complexOutput = "[{\"c\":\"fr.avalonlab.dashboard.category.domain.Category\",\"l\":{\"type\":\"CREDIT\",\"category\":\"b414301f-08b3-4b8d-bde0-3e493ff82bd7\",\".app\":\"dashboard\"},\"a\":{},\"v\":[[1527152658209000,50.27],[1529744658209000,150.8],[1532336658209000,600]]},{\"c\":\"fr.avalonlab.dashboard.category.domain.Category\",\"l\":{\"type\":\"DEBIT\",\"category\":\"15c92e99-0d2a-461c-813c-bf575b32746e\",\".app\":\"dashboard\"},\"a\":{},\"v\":[[1529744658209000,3000],[1532336658209000,4000.5],[1534928658209000,4700]]}]";




  }

}
