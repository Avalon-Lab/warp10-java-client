package fr.avalonlab.warp10.DSL.framework;

import org.junit.jupiter.api.Test;

import static fr.avalonlab.warp10.DSL.Warpscript.NOW;
import static fr.avalonlab.warp10.DSL.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.assertThat;

class BucketizeTest {

    @Test
    public void swapBucketizer() {
        Bucketize bucket = Bucketize.builder()
                .GTS(SWAP)
                .BUCKETIZER("bucketizer.sum")
                .LASTBUCKET(NOW)
                .BUCKETCOUNT(1);

        String result = bucket.formatScript();

        assertThat(result).isEqualTo(expectedSwapBucketizer());
    }

    private String expectedSwapBucketizer() {
        return "[ SWAP bucketizer.sum NOW 0 1 ] BUCKETIZE";
    }

}
