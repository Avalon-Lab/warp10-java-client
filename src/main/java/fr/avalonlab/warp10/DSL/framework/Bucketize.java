package fr.avalonlab.warp10.DSL.framework;

public class Bucketize extends Framework {

  private String[] gts;
  private String bucketizer;
  private String lastbucket = "0";
  private Long bucketspan = 0L;
  private Integer bucketcount = 0;

  private Bucketize() {
    frameworkName = "BUCKETIZE";
  }

  public static Bucketize builder() {
    return new Bucketize();
  }

  public Bucketize GTS(String... gts) {
    this.gts = gts;
    return this;
  }

  public Bucketize BUCKETIZER(String bucketizer) {
    this.bucketizer = bucketizer;
    return this;
  }

  public Bucketize LASTBUCKET(String NOW) {
    this.lastbucket = NOW;
    return this;
  }

  public Bucketize LASTBUCKET(Long lastbucket) {
    this.lastbucket = lastbucket.toString();
    return this;
  }

  public Bucketize BUCKETSPAN(Long bucketspan) {
    this.bucketspan = bucketspan;
    return this;
  }

  public Bucketize BUCKETCOUNT(Integer bucketcount) {
    this.bucketcount = bucketcount;
    return this;
  }

  @Override
  public String formatScript() {
    return "[ " + formatGts() + " " + bucketizer + " " + lastbucket + " " + bucketspan + " " + bucketcount + " ] " + frameworkName;
  }

  private String formatGts() {
    return String.join(" ", gts);
  }

}
