package fr.avalonlab.warp10.dsl;

import fr.avalonlab.warp10.exception.MissingMandatoryDataException;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GTSInputTest {

    @Test
    void withoutLatLonElev() {
        GTSInput newGTSInput = GTSInput.builder()
                .ts(1380475081000000L)
                .name("foo")
                .label("label1", "val1")
                .value("Toto");

        String result = newGTSInput.toInputFormat();

        assertThat(result).isEqualTo("1380475081000000// foo{label1=val1} 'Toto'");
    }

    @Test
    void withoutTsAndElev() {
        Map<String, String> mapOfLabels = new HashMap<>();
        mapOfLabels.put("label2", "val2");

        GTSInput newGTSInput = GTSInput.builder()
                .lat(48.0)
                .lon(-4.5)
                .name("bar")
                .labels(mapOfLabels)
                .value(-3.14);

        String result = newGTSInput.toInputFormat();

        assertThat(result).isEqualTo("/48.0:-4.5/ bar{label2=val2} -3.14");
    }

    @Test
    void fullDataGTS() {
        GTSInput newGTSInput = GTSInput.builder()
                .ts(1380475081123456L)
                .lat(45.0)
                .lon(-0.01)
                .elev(10000000L)
                .name("foobar")
                .label("label0", "val0")
                .label("label1", "val1")
                .value(Boolean.TRUE);

        String result = newGTSInput.toInputFormat();

        assertThat(result).isEqualTo("1380475081123456/45.0:-0.01/10000000 foobar{label0=val0,label1=val1} T");
    }

    @Test
    void withZoneDateTimeTS() {
        GTSInput newGTSInput = GTSInput.builder()
                .ts(ZonedDateTime.parse("2018-05-26T00:00:00+02:00"))
                .lat(45.0)
                .lon(-0.01)
                .elev(10000000L)
                .name("foobar")
                .label("label0", "val0")
                .label("label1", "val1")
                .value("Toto");

        String result = newGTSInput.toInputFormat();

        assertThat(result).isEqualTo("1527285600000000/45.0:-0.01/10000000 foobar{label0=val0,label1=val1} 'Toto'");
    }

    @Test
    void labelsAreMandatory() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> GTSInput.builder().name("toto").toInputFormat());

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'labels' was not set.");
    }

    @Test
    void nameIsMandatory() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> GTSInput.builder().toInputFormat());

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'name' was not set.");
    }

    @Test
    void valueIsMandatory() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> GTSInput.builder().name("toto").label("plip", "plop").toInputFormat());

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'value' was not set.");
    }

    @Test
    void canCreateGTSInputFromAnOtherGTSInput() {
        GTSInput point = GTSInput.builder()
                .ts(1380475081123456L)
                .lat(45.0)
                .lon(-0.01)
                .elev(10000000L)
                .name("foobar")
                .label("label0", "val0")
                .label("label1", "val1")
                .value(Boolean.TRUE);

        GTSInput otherPoint = GTSInput.from(point);

        assertThat(otherPoint).isEqualToComparingOnlyGivenFields(point, "ts","lat","lon", "elev", "name", "labels");
    }
}
