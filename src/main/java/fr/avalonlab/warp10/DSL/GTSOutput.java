package fr.avalonlab.warp10.DSL;


import java.util.HashMap;
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
  private Long msTimestamp;
  private Long latitude;
  private Long longitude;
  private Long elevation;
  private String value;

  public static GTSOutput fromOutputFormat(String output) {
    final String regex = "\\{(\"c\"):(?<c>.*),(\"l\"):(?<l>\\{.*\\}),(\"a\"):(?<a>\\{.*\\}),((\"i\"):(?<i>\".*\"),)?(\"v\"):(?<v>\\[\\[.*]])\\}";

    Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(output);

    GTSOutput gts = new GTSOutput();

    if(matcher.find()) {
      gts.className = stripExtraQuotes(matcher.group("c"));
      gts.labels = populateMap(matcher.group("l"));
      gts.attributes = populateMap(matcher.group("a"));
      gts.id = stripExtraQuotes(matcher.group("i"));
      extractValues(matcher.group("v"), gts);
    }

    return gts;
  }

  private static String stripExtraQuotes(String string) {
    if (string != null) {
      return string.replaceAll("\"", "");
    }
    return "";
  }

  private static void extractValues(String source, GTSOutput gts) {
    String[] values = source.replaceAll("\\[\\[", "").replaceAll("]]", "").split(",");

    if (values.length > 0) {
      gts.msTimestamp = Long.parseLong(values[0]);

      switch (values.length) {
        case 2:
          gts.value = stripExtraQuotes(values[1]);
          break;
        case 3:
          gts.value = stripExtraQuotes(values[2]);
          gts.elevation = Long.parseLong(values[1]);
          break;
        case 4:
          gts.value = stripExtraQuotes(values[3]);
          gts.latitude = Long.parseLong(values[1]);
          gts.longitude = Long.parseLong(values[2]);
          break;
        case 5:
          gts.value = stripExtraQuotes(values[4]);
          gts.latitude = Long.parseLong(values[1]);
          gts.longitude = Long.parseLong(values[2]);
          gts.elevation = Long.parseLong(values[3]);
          break;
      }
    }
  }

  private static Map<String, String> populateMap(String source) {
    source = source.replace("{", "").replace("}", "");

    String[] groups = source.split(",");

    if(!source.equals("") && groups.length > 0) {
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

  public Long getMsTimestamp() {
    return msTimestamp;
  }

  public Long getLatitude() {
    return latitude;
  }

  public Long parseLongitude() {
    return longitude;
  }

  public Long getElevation() {
    return elevation;
  }

  public String getValue() {
    return value;
  }
}
