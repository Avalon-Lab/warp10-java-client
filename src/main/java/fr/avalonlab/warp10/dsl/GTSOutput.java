package fr.avalonlab.warp10.dsl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GTSOutput {

    private String className;
    private Map<String, String> labels;
    private Map<String, String> attributes;
    private String id;
    private List<DataPoint> points;

    public static List<GTSOutput> fromOutputFormat(String output) {
        final String regex = "\\{(\"c\"):(?<c>.*?),(\"l\"):(?<l>\\{.*?\\}),(\"a\"):(?<a>\\{.*?\\}),((\"i\"):(?<i>\".*?\"),)?(\"v\"):(?<v>\\[\\[.*?]])\\}";

        Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(output);

        List<GTSOutput> outputs = new ArrayList<>();

        while (matcher.find()) {
            GTSOutput gts = new GTSOutput();

            gts.className = stripExtraQuotes(matcher.group("c"));
            gts.labels = populateMap(matcher.group("l"));
            gts.attributes = populateMap(matcher.group("a"));
            gts.id = stripExtraQuotes(matcher.group("i"));
            gts.points = extractDataPoint(matcher.group("v"));

            outputs.add(gts);
        }

        return outputs;
    }

    private static String stripExtraQuotes(String string) {
        if (string != null) {
            return string.replaceAll("\"", "");
        }
        return "";
    }

    private static List<DataPoint> extractDataPoint(String source) {

        String[] values = source.replaceAll("\\[\\[", "[").replaceAll("]]", "]").split("],");

        List<DataPoint> allPoints = new ArrayList<>();

        for (String item : values) {
            String[] data = item.replaceAll("\\[", "").replaceAll("]", "").split(",");

            if (data.length > 0) {
                DataPoint dp = DataPoint.empty();
                Long msTimestamp = Long.parseLong(data[0]);

                if (data.length == 2) {
                    dp = DataPoint.of(stripExtraQuotes(data[1]), msTimestamp);

                } else if (data.length == 3) {
                    dp = DataPoint.of(stripExtraQuotes(data[2]), msTimestamp)
                            .atElevation(Long.parseLong(data[1]));

                } else if (data.length == 4) {
                    dp = DataPoint.of(stripExtraQuotes(data[3]), msTimestamp);

                } else if (data.length == 5) {
                    dp = DataPoint.of(stripExtraQuotes(data[4]), msTimestamp)
                            .atLatitude(Long.parseLong(data[1]))
                            .atLongitude(Long.parseLong(data[2]))
                            .atElevation(Long.parseLong(data[3]));

                }
                allPoints.add(dp);
            }
        }
        return allPoints;
    }

    private static Map<String, String> populateMap(String source) {
        source = source.replace("{", "").replace("}", "");

        String[] groups = source.split(",");

        if (!source.equals("") && groups.length > 0) {
            return Stream.of(groups).collect(Collectors.toMap(GTSOutput::extractMapKey, GTSOutput::extractMapValue));
        }
        return new HashMap<>();
    }

    private static String extractMapValue(String item) {
        return stripExtraQuotes(item.split(":")[1]);
    }

    private static String extractMapKey(String item) {
        return stripExtraQuotes(item.split(":")[0]);
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getId() {
        return id;
    }

    public List<DataPoint> getPoints() {
        return points;
    }
}
