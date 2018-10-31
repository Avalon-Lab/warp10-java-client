package fr.avalonlab.warp10.dsl.framework;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FetchTest {

    @Test
    void fetchWithTimeStamp() {
        Fetch script = Fetch.builder()
                .classSelector("~class.*")
                .addLabel("foo", "bar")
                .addExactMatchLabel("john", "doe")
                .addRegExpLabel("hero", "bat*")
                .start(ZonedDateTime.parse("2013-01-01T00:00:00Z"))
                .end(ZonedDateTime.parse("2014-01-01T00:00:00+01:00"));

        String result = script.formatScript();

        assertThat(result).isEqualTo(expectedFetchWithTimeStamp());
    }

    private String expectedFetchWithTimeStamp() {
        return "[ $token '~class.*' { 'foo' 'bar' 'john' '=doe' 'hero' '~bat*' } '2013-01-01T00:00:00Z' '2014-01-01T00:00:00+01:00' ] FETCH";
    }

    @Test
    void fetchWithTimespan() {
        Fetch script = Fetch.builder()
                .classSelector("~class.*")
                .addLabel("foo", "bar")
                .now()
                .timespan(-90);

        String result = script.formatScript();

        assertThat(result).isEqualTo(expectedFetchWithTimespan());
    }

    private String expectedFetchWithTimespan() {
        return "[ $token '~class.*' { 'foo' 'bar' } now -90 ] FETCH";
    }
}
