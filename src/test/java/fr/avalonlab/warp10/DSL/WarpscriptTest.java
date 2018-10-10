package fr.avalonlab.warp10.DSL;


import fr.avalonlab.warp10.DSL.framework.Bucketize;
import fr.avalonlab.warp10.DSL.framework.Fetch;
import fr.avalonlab.warp10.DSL.framework.Reduce;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static fr.avalonlab.warp10.DSL.Warpscript.NOW;
import static fr.avalonlab.warp10.DSL.Warpscript.SWAP;
import static org.assertj.core.api.Assertions.*;

class WarpscriptTest {

  @Test
  public void completeWarpScript() {
    Fetch fetch = Fetch.builder().CLASS("~fr.avalonlab.Test.*").START(ZonedDateTime.parse("2018-04-20T00:00:00Z")).END(ZonedDateTime.parse("2018-08-28T00:00:00Z"));
    Bucketize bucketize = Bucketize.builder().GTS(SWAP).BUCKETIZER("bucketizer.sum").LASTBUCKET(NOW).BUCKETCOUNT(1);
    Reduce reduce = Reduce.builder().GTS(SWAP).labels("type").reducer("reducer.sum");

    Warpscript warpscript = Warpscript.builder()
      .TOKEN("READ_TOKEN")
      .functions(fetch, bucketize, reduce);

    String script = warpscript.formatScript();

    assertThat(script).isEqualTo(expectedWarpScript());
  }

  private String expectedWarpScript() {
    return "'READ_TOKEN' 'TOKEN' STORE\n" +
      "[ $TOKEN '~fr.avalonlab.Test.*' {  } '2018-04-20T00:00:00Z' '2018-08-28T00:00:00Z' ] FETCH\n" +
      "[ SWAP bucketizer.sum NOW 0 1 ] BUCKETIZE\n" +
      "[ SWAP [ 'type' ] reducer.sum ] REDUCE";
  }
}
