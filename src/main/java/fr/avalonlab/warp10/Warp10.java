package fr.avalonlab.warp10;

import fr.avalonlab.warp10.dsl.GTSInput;
import fr.avalonlab.warp10.dsl.Warpscript;
import fr.avalonlab.warp10.exception.MissingMandatoryDataException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Warp10 {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private static final String X_WARP_10_TOKEN = "X-Warp10-Token";
    private static final HttpClient DEFAULT_HTTP_CLIENT = HttpClient.newHttpClient();
    private final String endPointUri;
    private String readToken;
    private String writeToken;
    private HttpRequest request;
    private HttpClient client;
    private Duration globalTimeout;

    /**
     * @param endPointUri : URI of your Warp10 server
     * @param writeToken : Your Warp10 write token
     * @param readToken : Your Warp10 read token
     * @param globalTimeout : (Nullable) a global timeout to used for fetch and exec
     */
    public Warp10(String endPointUri, String writeToken, String readToken, Duration globalTimeout) {
        this.endPointUri = endPointUri;
        this.writeToken = writeToken;
        this.readToken = readToken;
        this.client = DEFAULT_HTTP_CLIENT;
        this.globalTimeout = globalTimeout;
    }

    /**
     * @param endPointUri : URI of your Warp10 server
     */
    public Warp10(String endPointUri) {
        this.endPointUri = endPointUri;
        this.client = DEFAULT_HTTP_CLIENT;
    }

    public static Warp10 instance(String endpointURL) {
        return new Warp10(endpointURL);
    }

    /**
     * @param token : Your Warp10 write token
     * @return the current warp10 instance
     */
    public Warp10 withWriteToken(String token) {
        writeToken = token;
        return this;
    }

    /**
     * @param token : Your Warp10 read token
     * @return the current warp10 instance
     */
    public Warp10 withReadToken(String token) {
        readToken = token;
        return this;
    }

    /**
     * @param client : Your own configured HttpClient (otherwise will use  HttpClient.newHttpClient()
     * @return the current warp10 instance
     */
    public Warp10 withClient(HttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * @param globalTimeout : a global timeout to used for fetch and exec
     *                      The effect f not setting a timeout is the same as setting an infinite Duration,
     *                      ie. block forever.
     * @return the current warp10 instance
     */
    public Warp10 withGlobalTimeout(Duration globalTimeout) {
        this.globalTimeout = globalTimeout;
        return this;
    }

    /**
     * Push data to warp10
     * @param data : the GTSInput to push to Warp10
     * @return the current warp10 instance
     */
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

    /**
     * Push a gzipped bulk of datas in Warp10
     * @param data : List of GTSInput's string representation
     * @return the current warp10 instance
     */
    public Warp10 ingressGZip(List<String> data) {
        return this.ingressCompressed(() -> DefaultGzipCompressor.compress(data), DefaultGzipCompressor.CONTENT_TYPE);
    }

    /**
     * Push a compressed bulk of datas in Warp10
     * @param compressingSupplier : Supplier of your compression solution
     * @param contentType : the content type corresponding of your compression solution.
     *                    ie. for GZIP use "application/gzip"
     * @return the current warp10 instance
     */
    public Warp10 ingressCompressed(Supplier<byte[]> compressingSupplier, String contentType) {

        if (writeToken == null) {
            throw new MissingMandatoryDataException("WRITE_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create(endPointUri + "/update"))
                .header("Content-Type", contentType)
                .header(X_WARP_10_TOKEN, writeToken)
                .POST(HttpRequest.BodyPublishers.ofByteArray(compressingSupplier.get()))
                .build();

        return this;
    }

    /**
     * Fetch GTS from Warp10
     * @param query : your Fetch query
     * @return the current warp10 instance
     */
    public Warp10 fetch(String query) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/fetch?" + query))
                .header(X_WARP_10_TOKEN, readToken)
                .GET();

        if (globalTimeout != null) {
            requestBuilder = requestBuilder.timeout(globalTimeout);
        }

        request = requestBuilder.build();

        return this;
    }

    /**
     * Delete GTS from Warp10
     * @param query : your Delete query
     * @return the current warp10 instance
     */
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

    /**
     * Exec a complete Waprscript on your Warp10 server
     * @param warpscript : script to execute
     * @return the current warp10 instance
     */
    public Warp10 exec(Warpscript warpscript) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/exec"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(warpscript.token(readToken).formatScript()));

        if (globalTimeout != null) {
            requestBuilder = requestBuilder.timeout(globalTimeout);
        }

        request = requestBuilder.build();

        return this;
    }

    /**
     * Exec Warpscript on Warp10 with a specific timeout
     * @param warpscript to run
     * @param timeout, Specific timeout to use for this exec (wil override globalTimeout)
     * @return the current warp10 instance
     */
    public Warp10 execWithTimeout(Warpscript warpscript, Duration timeout) {

        if (readToken == null) {
            throw new MissingMandatoryDataException("READ_TOKEN");
        }

        request = HttpRequest.newBuilder()
                .uri(URI.create(endPointUri + "/exec"))
                .timeout(timeout)
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(warpscript.token(readToken).formatScript()))
                .build();

        return this;
    }

    /**
     * Send the built request to the Warp10 server
     * @return the response sent by the Warp10 server
     * @throws IOException .
     * @throws InterruptedException .
     */
    public HttpResponse<String> send() throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Send asynchronously the built request to the Warp10 server
     * @return the CompletableFuture with the response sent by the Warp10 server
     */
    public CompletableFuture<HttpResponse<String>> sendAsync() {
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
