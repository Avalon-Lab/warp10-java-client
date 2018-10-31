package fr.avalonlab.warp10.dsl;

import java.util.Objects;

public class DataPoint {
    private Long msTimestamp;
    private Long latitude;
    private Long longitude;
    private Long elevation;
    private String value;

    public DataPoint(Long msTimestamp, Long latitude, Long longitude, Long elevation, String value) {
        this.msTimestamp = msTimestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.value = value;
    }

    private DataPoint(Long msTimestamp, String value) {
        this.msTimestamp = msTimestamp;
        this.value = value;
    }

    private DataPoint() {

    }

    public static DataPoint empty() {
        return new DataPoint();
    }

    public boolean isEmpty() {
        return msTimestamp == null && value == null;
    }

    public static DataPoint of(String value, Long msTimeStamp) {
        return new DataPoint(msTimeStamp, value);
    }

    public DataPoint atLatitude(Long lat) {
        latitude = lat;
        return this;
    }

    public DataPoint atLongitude(Long lgt) {
        longitude = lgt;
        return this;
    }

    public DataPoint atElevation(Long elev) {
        elevation = elev;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Long getMsTimestamp() {
        return msTimestamp;
    }

    public Long getLatitude() {
        return latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public Long getElevation() {
        return elevation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPoint dataPoint = (DataPoint) o;
        return Objects.equals(msTimestamp, dataPoint.msTimestamp) &&
                Objects.equals(latitude, dataPoint.latitude) &&
                Objects.equals(longitude, dataPoint.longitude) &&
                Objects.equals(elevation, dataPoint.elevation) &&
                Objects.equals(value, dataPoint.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msTimestamp, latitude, longitude, elevation, value);
    }
}
