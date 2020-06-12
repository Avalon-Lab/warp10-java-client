package fr.avalonlab.warp10;

import fr.avalonlab.warp10.dsl.GTSInput;
import fr.avalonlab.warp10.dsl.Warpscript;
import fr.avalonlab.warp10.exception.MissingMandatoryDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Warp10Test {

    private static final String ENDPOINT_URL = "http://hello";
    private HttpTestClient testClient;
    private GTSInput input;

    @BeforeEach
    void init() {
        testClient = new HttpTestClient();
        input = GTSInput.builder()
                .ts(1380475081000000L)
                .name("foo")
                .label("label1", "val1")
                .value("Toto");
    }

    @Test
    void fetchQuery() throws IOException, InterruptedException {
        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withReadToken("1234567890")
                .fetch("world");

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/fetch?world"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("GET");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).contains("1234567890");
    }

    @Test
    void checkFetchMandatoryReadToken() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .fetch("world"));

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'READ_TOKEN' was not set.");
    }

    @Test
    void fetchWithBadURI() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> Warp10.instance("hello://world")
                .withClient(testClient)
                .withReadToken("blabla")
                .fetch("warp10"));

        assertThat(exception.getMessage()).isEqualTo("invalid URI scheme hello");
    }

    @Test
    void ingress() throws IOException, InterruptedException {
        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withWriteToken("908766")
                .ingress(input);

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/update"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("POST");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).contains("908766");
        assertThat(testClient.calculatedRequest.headers().firstValue("Content-Type")).contains("text/plain");
    }

    @Test
    void batchIngress() throws IOException, InterruptedException {
        GTSInput input2 = GTSInput.builder()
                .ts(1380475082000000L)
                .name("foo")
                .label("label1", "val1")
                .value("Toto2");

        GTSInput input3 = GTSInput.builder()
                .ts(1380475083000000L)
                .name("foo")
                .label("label1", "val1")
                .value("Toto3");


        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withWriteToken("908766")
                .ingressGZip(List.of(input.toInputFormat(), input2.toInputFormat(), input3.toInputFormat()));

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/update"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("POST");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).contains("908766");
        assertThat(testClient.calculatedRequest.headers().firstValue("Content-Type")).contains("application/gzip");
    }

    @Test
    void checkIngressMandatoryWriteToken() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .ingress(input));

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'WRITE_TOKEN' was not set.");
    }

    @Test
    void delete() throws IOException, InterruptedException {
        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withWriteToken("ER5446")
                .delete("selector=~t.t.%7Bb=4%7D&start=2015-09-02T12:00:00Z");

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/delete?selector=~t.t.%7Bb=4%7D&start=2015-09-02T12:00:00Z"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("GET");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).contains("ER5446");
    }

    @Test
    void checkDeleteMandatoryWriteToken() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .delete("selector=~t.t.%7Bb=4%7D&start=2015-09-02T12:00:00Z"));

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'WRITE_TOKEN' was not set.");
    }

    @Test
    void exec() throws IOException, InterruptedException {
        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withReadToken("3442GFG")
                .exec(Warpscript.builder().rawQuery("toto"));

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/exec"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("POST");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).isEmpty();
        assertThat(testClient.calculatedRequest.headers().firstValue("Content-Type")).contains("text/plain");
    }

    @Test
    void execWithTimeout() throws IOException, InterruptedException {
        Warp10 warp10 = Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .withReadToken("3442GFG")
                .execWithTimeout(Warpscript.builder().rawQuery("toto"), Duration.ofMinutes(5));

        warp10.send();

        assertThat(testClient.calculatedRequest.uri()).isEqualTo(URI.create("http://hello/exec"));
        assertThat(testClient.calculatedRequest.method()).isEqualTo("POST");
        assertThat(testClient.calculatedRequest.headers().firstValue("X-Warp10-Token")).isEmpty();
        assertThat(testClient.calculatedRequest.headers().firstValue("Content-Type")).contains("text/plain");
        assertThat(testClient.calculatedRequest.timeout()).hasValue(Duration.ofMinutes(5));
    }

    @Test
    void checkExecMandatoryReadToken() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .exec(Warpscript.builder().rawQuery("toto")));

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'READ_TOKEN' was not set.");
    }

    @Test
    void checkExecWithTimeoutMandatoryReadToken() {
        Throwable exception = assertThrows(MissingMandatoryDataException.class, () -> Warp10.instance(ENDPOINT_URL)
                .withClient(testClient)
                .execWithTimeout(Warpscript.builder().rawQuery("toto"), Duration.ofMinutes(5)));

        assertThat(exception).isInstanceOf(MissingMandatoryDataException.class);
        assertThat(exception.getMessage()).isEqualTo("The data 'READ_TOKEN' was not set.");
    }

    private class HttpTestClient extends HttpClient {
        private HttpRequest calculatedRequest;

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
            this.calculatedRequest = request;
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
            this.calculatedRequest = request;
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            return null;
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return null;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return null;
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return null;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }

    }
}

