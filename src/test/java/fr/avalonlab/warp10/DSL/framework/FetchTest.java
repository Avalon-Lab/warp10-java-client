package fr.avalonlab.warp10.DSL.framework;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FetchTest {

    @Test
    public void fetchWithTimeStamp() {
        Fetch script = Fetch.builder()
                .CLASS("~class.*")
                .addLabel("foo", "bar")
                .addExactMatchLabel("john", "doe")
                .addRegExpLabel("hero", "bat*")
                .START(ZonedDateTime.parse("2013-01-01T00:00:00Z"))
                .END(ZonedDateTime.parse("2014-01-01T00:00:00+01:00"));

        String result = script.formatScript();

        assertThat(result).isEqualTo(expectedFetchWithTimeStamp());
    }

    private String expectedFetchWithTimeStamp() {
        return "[ $TOKEN '~class.*' { 'foo' 'bar' 'john' '=doe' 'hero' '~bat*' } '2013-01-01T00:00:00Z' '2014-01-01T00:00:00+01:00' ] FETCH";
    }

    @Test
    public void fetchWithTimespan() {
        Fetch script = Fetch.builder()
                .CLASS("~class.*")
                .addLabel("foo", "bar")
                .NOW()
                .TIMESPAN(-90);

        String result = script.formatScript();

        assertThat(result).isEqualTo(expectedFetchWithTimespan());
    }

    private String expectedFetchWithTimespan() {
        return "[ $TOKEN '~class.*' { 'foo' 'bar' } NOW -90 ] FETCH";
    }
}
