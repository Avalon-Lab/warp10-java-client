package fr.avalonlab.warp10.dsl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DataPointTest {

    @Test
    void emptyDataPoint() {
        DataPoint dp = new DataPoint(null, null, null, null, null);

        assertThat(dp.isEmpty()).isTrue();
        assertThat(dp).isEqualTo(DataPoint.empty());
    }

    @Test
    void timedDataPoint() {
        DataPoint dp = DataPoint.of("toto", 12345670000L);

        assertThat(dp).isEqualTo(new DataPoint(12345670000L, null, null, null, "toto"));
        assertThat(dp.getMsTimestamp()).isEqualTo(12345670000L);
        assertThat(dp.getValue()).isEqualTo("toto");
    }

    @Test
    void geoLocDataPoint() {
        DataPoint dp = DataPoint.of("toto", 12345670000L)
                .atLongitude(877633883000L)
                .atLatitude(9874333000L);

        assertThat(dp).isEqualTo(new DataPoint(12345670000L, 9874333000.0, 877633883000.0, null, "toto"));
        assertThat(dp.getLongitude()).isEqualTo(877633883000L);
        assertThat(dp.getLatitude()).isEqualTo(9874333000L);
    }

    @Test
    void geoLocWithElevationDataPoint() {
        DataPoint dp = DataPoint.of("toto", 12345670000L)
                .atLongitude(877633883000L)
                .atLatitude(9874333000L)
                .atElevation(-94333344000L);

        assertThat(dp).isEqualTo(new DataPoint(12345670000L, 9874333000.0, 877633883000.0, -94333344000L, "toto"));
        assertThat(dp.getElevation()).isEqualTo(-94333344000L);
    }

    @Test
    void extractDataPointWithMultiplePoint() {
        String source = "[[1527152658209000,50.27],[1529744658209000,150.8],[1532336658209000,600]]";

        List<DataPoint> results = DataPoint.extractDataPoint(source);

        assertThat(results).containsOnly(DataPoint.of("50.27", 1527152658209000L), DataPoint.of("150.8", 1529744658209000L), DataPoint.of("600", 1532336658209000L));
    }

    @Test
    void extractDataPointWithTimeAndValue() {
        String source = "[[1527152658209000,50.27]]";

        List<DataPoint> results = DataPoint.extractDataPoint(source);

        assertThat(results).containsOnly(DataPoint.of("50.27", 1527152658209000L));
    }

    @Test
    void extractDataPointWithTimeAndValueAndElevation() {
        String source = "[[1527152658209000,-544555445,50.27]]";

        List<DataPoint> results = DataPoint.extractDataPoint(source);

        assertThat(results).containsOnly(DataPoint.of("50.27", 1527152658209000L).atElevation(-544555445L));
    }

    @Test
    void extractDataPointWithTimeAndValueAndLatAndLon() {
        String source = "[[1527152658209000,45.43,-23.45,50.27]]";

        List<DataPoint> results = DataPoint.extractDataPoint(source);

        assertThat(results).containsOnly(DataPoint.of("50.27", 1527152658209000L).atLatitude(45.43).atLongitude(-23.45));
    }

    @Test
    void extractDataPointWithTimeAndValueAndLatAndLonAndElev() {
        String source = "[[1527152658209000,45.43,-23.45,13400000,50.27]]";

        List<DataPoint> results = DataPoint.extractDataPoint(source);

        assertThat(results).containsOnly(DataPoint.of("50.27", 1527152658209000L).atLatitude(45.43).atLongitude(-23.45).atElevation(13400000L));
    }

}