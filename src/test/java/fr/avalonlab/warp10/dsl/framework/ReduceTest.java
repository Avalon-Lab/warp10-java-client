package fr.avalonlab.warp10.dsl.framework;

import org.junit.jupiter.api.Test;

import static fr.avalonlab.warp10.dsl.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.assertThat;

class ReduceTest {

    @Test
    void zeroLabel() {
        Reduce reduce = Reduce.builder()
                .gts(SWAP)
                .reducer("reducer.sum");

        String reducer = reduce.formatScript();

        assertThat(reducer).isEqualTo(expectedZeroLabelReducer());
    }

    private String expectedZeroLabelReducer() {
        return "[ SWAP [  ] reducer.sum ] REDUCE";
    }

    @Test
    void oneLabel() {
        Reduce reduce = Reduce.builder()
                .gts(SWAP)
                .labels("type")
                .reducer("reducer.sum");

        String reducer = reduce.formatScript();

        assertThat(reducer).isEqualTo(expectedOneLabelReducer());
    }

    private String expectedOneLabelReducer() {
        return "[ SWAP [ 'type' ] reducer.sum ] REDUCE";
    }

    @Test
    void twoLabel() {
        Reduce reduce = Reduce.builder()
                .gts(SWAP)
                .labels("type", "time")
                .reducer("reducer.sum");

        String reducer = reduce.formatScript();

        assertThat(reducer).isEqualTo(expectedTwoLabelReducer());
    }

    private String expectedTwoLabelReducer() {
        return "[ SWAP [ 'type' 'time' ] reducer.sum ] REDUCE";
    }
}
