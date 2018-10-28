package fr.avalonlab.warp10.DSL;

import fr.avalonlab.warp10.DSL.framework.Framework;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Warpscript {

    public static final String SWAP = "SWAP";
    public static final String NOW = "NOW";
    private static final String NEW_LINE = "\n";

    private String rawQuery;
    private String token;
    private Framework[] functions;


    private Warpscript() {
    }

    public static Warpscript builder() {
        return new Warpscript();
    }

    public Warpscript rawQuery(String query) {
        rawQuery = query;
        return this;
    }

    public Warpscript TOKEN(String readToken) {
        this.token = readToken;
        return this;
    }


    public Warpscript functions(Framework... functions) {
        this.functions = functions;
        return this;
    }


    public String formatScript() {

        String script = "'" + token + "' " + "'TOKEN' STORE" + NEW_LINE;

        if (rawQuery != null && token == null) {
            return rawQuery;
        } else if (rawQuery != null) {
            return script + rawQuery;
        }

        if (functions.length > 0) {
            script += Stream.of(functions).map(Framework::formatScript).collect(Collectors.joining(NEW_LINE));
        }

        return script;
    }

}
