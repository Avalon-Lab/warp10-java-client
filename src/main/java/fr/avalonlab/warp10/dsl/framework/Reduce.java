package fr.avalonlab.warp10.dsl.framework;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reduce extends Framework {

    private String[] gts;
    private String reducer;
    private String[] labels;


    private Reduce() {
        frameworkName = "REDUCE";
        labels = new String[]{};
    }

    public static Reduce builder() {
        return new Reduce();
    }

    public Reduce gts(String... gts) {
        this.gts = gts;
        return this;
    }

    public Reduce reducer(String reducer) {
        this.reducer = reducer;
        return this;
    }

    public Reduce labels(String... labels) {
        this.labels = labels;
        return this;
    }

    @Override
    public String formatScript() {
        return "[ " + formatGts() + " [ " + formatLabels() + " ] " + reducer + " ] " + frameworkName;
    }

    private String formatLabels() {
        if (labels.length > 0) {
            return Stream.of(labels).collect(Collectors.joining("' '", "'", "'"));
        }

        return "";
    }

    private String formatGts() {
        return String.join(" ", gts);
    }
}
