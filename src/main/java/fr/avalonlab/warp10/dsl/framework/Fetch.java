package fr.avalonlab.warp10.dsl.framework;


import fr.avalonlab.warp10.exception.MissingMandatoryDataException;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.avalonlab.warp10.dsl.Warpscript.NOW;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class Fetch extends Framework {

    private static final String UTF_8 = "utf-8";
    private String classSelector;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Integer timespan;
    private Map<String, String> labelsSelectors;
    private boolean now = false;

    private Fetch() {
        frameworkName = "FETCH";
        labelsSelectors = new HashMap<>();
    }

    public static Fetch builder() {
        return new Fetch();
    }

    public Fetch classSelector(String classSelector) {
        this.classSelector = classSelector;
        return this;
    }

    public Fetch start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public Fetch end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public Fetch now() {
        this.now = true;
        return this;
    }

    public Fetch timespan(Integer timespan) {
        this.timespan = timespan;
        return this;
    }

    public Fetch labels(Map<String, String> labelsSelectors) {
        this.labelsSelectors.putAll(labelsSelectors);
        return this;
    }

    public Fetch addLabel(String key, String value) {
        this.labelsSelectors.put(URLEncoder.encode(key, Charset.forName(UTF_8)), URLEncoder.encode(value, Charset.forName(UTF_8)));
        return this;
    }

    public Fetch addExactMatchLabel(String key, String value) {
        this.labelsSelectors.put(URLEncoder.encode(key, Charset.forName(UTF_8)), "=" + URLEncoder.encode(value, Charset.forName(UTF_8)));
        return this;
    }

    public Fetch addRegExpLabel(String key, String value) {
        this.labelsSelectors.put(URLEncoder.encode(key, Charset.forName(UTF_8)), "~" + URLEncoder.encode(value, Charset.forName(UTF_8)));
        return this;
    }

    @Override
    public String formatScript() {
        return "[ $token '" + classSelector + "' { " + formatLabelsSelecor() + " } " + formatTimeSelector() + " ] " + frameworkName;
    }

    private String formatTimeSelector() {
        if (start != null && end != null) {
            return "'" + ISO_OFFSET_DATE_TIME.format(start) + "' '" + ISO_OFFSET_DATE_TIME.format(end) + "'";

        } else if (now && timespan != null) {
            return NOW + " " + timespan.toString();
        }

        throw new MissingMandatoryDataException("start / end / timespan");
    }

    private String formatLabelsSelecor() {
        if (labelsSelectors.isEmpty()) {
            return "";
        }

        return labelsSelectors.entrySet().stream().map(entry -> "'" + entry.getKey() + "' '" + entry.getValue() + "'").collect(Collectors.joining(" "));
    }

}
