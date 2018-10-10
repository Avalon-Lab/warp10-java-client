package fr.avalonlab.warp10.DSL.framework;

import org.junit.jupiter.api.Test;

import static fr.avalonlab.warp10.DSL.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.*;

class ReduceTest {

  @Test
  public void zeroLabel() {
    Reduce reduce = Reduce.builder()
      .GTS(SWAP)
      .reducer("reducer.sum");

    String reducer = reduce.formatScript();

    assertThat(reducer).isEqualTo(expectedZeroLabelReducer());
  }

  private String expectedZeroLabelReducer() {
    return "[ SWAP [  ] reducer.sum ] REDUCE";
  }

  @Test
  public void oneLabel() {
    Reduce reduce = Reduce.builder()
      .GTS(SWAP)
      .labels("type")
      .reducer("reducer.sum");

    String reducer = reduce.formatScript();

    assertThat(reducer).isEqualTo(expectedOneLabelReducer());
  }

  private String expectedOneLabelReducer() {
    return "[ SWAP [ 'type' ] reducer.sum ] REDUCE";
  }

  @Test
  public void twoLabel() {
    Reduce reduce = Reduce.builder()
      .GTS(SWAP)
      .labels("type", "time")
      .reducer("reducer.sum");

    String reducer = reduce.formatScript();

    assertThat(reducer).isEqualTo(expectedTwoLabelReducer());
  }

  private String expectedTwoLabelReducer() {
    return "[ SWAP [ 'type' 'time' ] reducer.sum ] REDUCE";
  }
}
