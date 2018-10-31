package fr.avalonlab.warp10.dsl.framework;

import org.junit.jupiter.api.Test;

import static fr.avalonlab.warp10.dsl.Warpscript.NOW;
import static fr.avalonlab.warp10.dsl.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.assertThat;

class BucketizeTest {

    @Test
    void swapBucketizer() {
        Bucketize bucket = Bucketize.builder()
                .gts(SWAP)
                .bucketizer("bucketizer.sum")
                .lastbucket(NOW)
                .bucketcount(1);

        String result = bucket.formatScript();

        assertThat(result).isEqualTo(expectedSwapBucketizer());
    }

    private String expectedSwapBucketizer() {
        return "[ SWAP bucketizer.sum now 0 1 ] BUCKETIZE";
    }

}
