package fr.avalonlab.warp10.dsl;

import fr.avalonlab.warp10.exception.MissingMandatoryDataException;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GTSInput {

    private static final String UTF_8 = "utf-8";
    private Long ts;
    private Double lat;
    private Double lon;
    private Long elev;
    private String name;
    private Map<String, String> labels;
    private String stringValue;
    private Long longValue;
    private Double doubleValue;
    private Boolean booleanValue;

    private GTSInput() {
        this.labels = new HashMap<>();
    }

    public static GTSInput builder() {
        return new GTSInput();
    }

    public GTSInput ts(Long timestampMicro) {
        this.ts = timestampMicro;
        return this;
    }

    public GTSInput ts(ZonedDateTime timeStamp) {
        this.ts = TimeUnit.MILLISECONDS.toMicros(timeStamp.toInstant().toEpochMilli());
        return this;
    }

    public GTSInput lat(Double lat) {
        this.lat = lat;
        return this;
    }

    public GTSInput lon(Double lon) {
        this.lon = lon;
        return this;
    }

    public GTSInput elev(Long elev) {
        this.elev = elev;
        return this;
    }

    public GTSInput name(String name) {
        this.name = URLEncoder.encode(name, Charset.forName(UTF_8));
        return this;
    }

    public GTSInput labels(Map<String, String> labels) {
        this.labels.putAll(labels);
        return this;
    }

    public GTSInput label(String key, String value) {
        this.labels.put(URLEncoder.encode(key, Charset.forName(UTF_8)), URLEncoder.encode(value, Charset.forName(UTF_8)));
        return this;
    }

    public GTSInput value(String value) {
        this.stringValue = value;
        return this;
    }

    public GTSInput value(Double value) {
        this.doubleValue = value;
        return this;
    }

    public GTSInput value(Boolean value) {
        this.booleanValue = value;
        return this;
    }

    public GTSInput value(Long value) {
        this.longValue = value;
        return this;
    }

    public String toInputFormat() {

        String warp10Format = "%s/%s/%s %s{%s} %s";

        return String.format(warp10Format, formatOptionalLongValue(ts), formatOptionalLatLon(), formatOptionalLongValue(elev), formatMandatoryFieldName(), formatLabels(), formatValue());
    }

    private String formatMandatoryFieldName() {
        if (name == null) {
            throw new MissingMandatoryDataException("name");
        }

        return name;
    }

    private String formatOptionalLatLon() {
        String latLon = "";
        if (lat != null && lon != null) {
            latLon = lat + ":" + lon;
        }
        return latLon;
    }

    private String formatLabels() {
        if (labels != null && !labels.isEmpty()) {
            return labels.entrySet().stream().map(entry -> entry.getKey() + '=' + entry.getValue()).collect(Collectors.joining(","));
        }
        throw new MissingMandatoryDataException("labels");
    }

    private String formatOptionalLongValue(Long possibleNullValue) {
        if (possibleNullValue == null) {
            return "";
        }
        return possibleNullValue.toString();
    }

    private String formatValue() {
        if (longValue != null) {
            return longValue.toString();
        } else if (doubleValue != null) {
            return doubleValue.toString();
        } else if (booleanValue != null) {
            return booleanValue ? "T" : "F";
        } else if (stringValue != null) {
            return "'" + URLEncoder.encode(stringValue, Charset.forName(UTF_8)) + "'";
        }
        throw new MissingMandatoryDataException("value");
    }

}
