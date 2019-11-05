package fr.avalonlab.warp10;

import fr.avalonlab.warp10.dsl.GTSInput;
import fr.avalonlab.warp10.dsl.Warpscript;
import fr.avalonlab.warp10.exception.MissingMandatoryDataException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

public class Warp10 {

    private static final String X_WARP_10_TOKEN = "X-Warp10-Token";
    private static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient();
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
                .header(X_WARP_10_TOKEN, writeToken)
                .POST(HttpRequest.BodyPublishers.ofString(data.toInputFormat()))
                .build();

        return this;
    }

    public Warp10 ingressGZip(List<String> data) throws IOException {

        if (writeToken == null) {
            throw new MissingMandatoryDataException("WRITE_TOKEN");
        }

        String batchData = String.join(System.getProperty("line.separator"), data);

        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(endPointUri + "/update"))
                .header("Content-Type", "application/gzip")
                .header(X_WARP_10_TOKEN, writeToken)
                .POST(HttpRequest.BodyPublishers.ofByteArray(compressData(batchData)))
                .build();

        return this;
    }

    private byte[] compressData(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressed = bos.toByteArray();
        bos.close();

        return compressed;
    }

    public Warp10 fetch(String query) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/fetch?" + query))
                .header(X_WARP_10_TOKEN, readToken)
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
                .header(X_WARP_10_TOKEN, writeToken)
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
                .POST(HttpRequest.BodyPublishers.ofString(warpscript.token(readToken).formatScript()))
                .build();

        return this;
    }

    public HttpResponse<String> send() throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> sendAsync() {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }


}
