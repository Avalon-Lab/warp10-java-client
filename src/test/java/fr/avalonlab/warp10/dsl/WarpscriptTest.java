package fr.avalonlab.warp10.dsl;


import fr.avalonlab.warp10.dsl.framework.Bucketize;
import fr.avalonlab.warp10.dsl.framework.Fetch;
import fr.avalonlab.warp10.dsl.framework.Reduce;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static fr.avalonlab.warp10.dsl.Warpscript.NOW;
import static fr.avalonlab.warp10.dsl.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.assertThat;

class WarpscriptTest {

    private final String NEW_LINE = System.getProperty("line.separator");

    @Test
    void warpscriptFromRawQuery() {
        String rawQuery = "'34RT-REE2-RERER' 'MY_TOKEN' STORE" + NEW_LINE
                + "my warpscipt command";

        String script = Warpscript.builder().rawQuery(rawQuery).formatScript();

        assertThat(script).isEqualTo(rawQuery);
    }

    @Test
    void warpscriptFromRawQueryWithToken() {
        String rawQuery = "my super" + NEW_LINE
                + "warpscript" + NEW_LINE
                + "command";

        String script = Warpscript.builder().token("44TT-55Yy-KJ98").rawQuery(rawQuery).formatScript();

        assertThat(script).isEqualTo(expectedWarpScriptWithToken());
    }

    @Test
    void completeWarpScript() {
        Fetch fetch = Fetch.builder().classSelector("~fr.avalonlab.Test.*").start(ZonedDateTime.parse("2018-04-20T00:00:00Z")).end(ZonedDateTime.parse("2018-08-28T00:00:00Z"));
        Bucketize bucketize = Bucketize.builder().gts(SWAP).bucketizer("bucketizer.sum").lastbucket(NOW).bucketcount(1);
        Reduce reduce = Reduce.builder().gts(SWAP).labels("type").reducer("reducer.sum");

        Warpscript warpscript = Warpscript.builder()
                .token("READ_TOKEN")
                .functions(fetch, bucketize, reduce);

        String script = warpscript.formatScript();

        assertThat(script).isEqualTo(expectedWarpScript());
    }

    private String expectedWarpScriptWithToken() {
        return "'44TT-55Yy-KJ98' 'token' STORE" + NEW_LINE +
                "my super" + NEW_LINE +
                "warpscript" + NEW_LINE +
                "command";
    }


    private String expectedWarpScript() {
        return "'READ_TOKEN' 'token' STORE" + NEW_LINE +
                "[ $token '~fr.avalonlab.Test.*' {  } '2018-04-20T00:00:00Z' '2018-08-28T00:00:00Z' ] FETCH" + NEW_LINE +
                "[ SWAP bucketizer.sum now 0 1 ] BUCKETIZE" + NEW_LINE +
                "[ SWAP [ 'type' ] reducer.sum ] REDUCE";
    }
}
