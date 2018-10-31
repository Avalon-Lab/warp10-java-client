package fr.avalonlab.warp10.dsl.framework;

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

    public Bucketize gts(String... gts) {
        this.gts = gts;
        return this;
    }

    public Bucketize bucketizer(String bucketizer) {
        this.bucketizer = bucketizer;
        return this;
    }

    public Bucketize lastbucket(String NOW) {
        this.lastbucket = NOW;
        return this;
    }

    public Bucketize lastbucket(Long lastbucket) {
        this.lastbucket = lastbucket.toString();
        return this;
    }

    public Bucketize bucketspan(Long bucketspan) {
        this.bucketspan = bucketspan;
        return this;
    }

    public Bucketize bucketcount(Integer bucketcount) {
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
