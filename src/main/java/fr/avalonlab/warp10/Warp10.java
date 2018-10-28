package fr.avalonlab.warp10;

import fr.avalonlab.warp10.DSL.GTSInput;
import fr.avalonlab.warp10.DSL.GTSOutput;
import fr.avalonlab.warp10.DSL.Warpscript;
import fr.avalonlab.warp10.exception.MissingMandatoryDataException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Warp10 {

    private final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient();
    private final String endPointUri;
    private String readToken;
    private String writeToken;
    private HttpRequest request;
    private HttpClient client;


    public Warp10(String endPointUri, String writeToken, String readToken) {
        this.endPointUri = endPointUri;
        this.writeToken = writeToken;
        this.readToken = readToken;
        this.client = DEFAULT_HTTP_CLIENT;
    }

    public Warp10(String endPointUri) {
        this.endPointUri = endPointUri;
        this.client = DEFAULT_HTTP_CLIENT;
    }

    public static Warp10 instance(String endpointURL) {
        return new Warp10(endpointURL);
    }

    public Warp10 withWriteToken(String token) {
        writeToken = token;
        return this;
    }

    public Warp10 withReadToken(String token) {
        readToken = token;
        return this;
    }

    public Warp10 withClient(HttpClient client) {
        this.client = client;
        return this;
    }

    public Warp10 ingress(GTSInput data) {

        if (writeToken == null) {
            throw new MissingMandatoryDataException("WRITE_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/update"))
                .header("Content-Type", "text/plain")
                .header("X-Warp10-Token", writeToken)
                .POST(HttpRequest.BodyPublishers.ofString(data.toInputFormat()))
                .build();

        return this;
    }

    public Warp10 fetch(String query) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/fetch?" + query))
                .header("X-Warp10-Token", readToken)
                .GET()
                .build();

        return this;
    }

    public Warp10 delete(String query) {

        if (writeToken == null) {
            throw new MissingMandatoryDataException("WRITE_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/delete?" + query))
                .header("X-Warp10-Token", writeToken)
                .GET()
                .build();

        return this;
    }

    public Warp10 exec(Warpscript warpscript) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/exec"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(warpscript.TOKEN(readToken).formatScript()))
                .build();

        return this;
    }

    public List<GTSOutput> send() throws IOException, InterruptedException {

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response != null) {
            return GTSOutput.fromOutputFormat(response.body());
        }

        return new ArrayList<>();
    }

    public CompletableFuture<List<GTSOutput>> sendAsync() {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> GTSOutput.fromOutputFormat(response.body()));
    }


}
