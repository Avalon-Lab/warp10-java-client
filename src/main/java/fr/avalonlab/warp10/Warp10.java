package fr.avalonlab.warp10;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import fr.avalonlab.warp10.DSL.Warpscript;
import fr.avalonlab.warp10.DSL.GTSInput;

public class Warp10 {

  private final String endPointUri;
  private String readToken;
  private String writeToken;
  private HttpRequest request;

  public Warp10(String endPointUri, String writeToken, String readToken) {
    this.endPointUri = endPointUri;
    this.writeToken = writeToken;
    this.readToken = readToken;
  }

  public Warp10(String endPointuri) {
    this.endPointUri = endPointuri;
  }

  public Warp10 withWriteToken(String token) {
    this.writeToken = writeToken;
    return this;
  }

  public Warp10 withReadToken(String token) {
    this.readToken = readToken;
    return this;
  }

  public Warp10 ingress(GTSInput data) {
    request = HttpRequest.newBuilder()
      .uri(URI.create(endPointUri + "/update"))
      .header("Content-Type", "text/plain")
      .header("X-Warp10-Token", writeToken)
      .POST(HttpRequest.BodyPublishers.ofString(data.toInputFormat()))
      .build();

    return this;
  }

  public Warp10 fetch(String query) {
    request = HttpRequest.newBuilder()
      .uri(URI.create(endPointUri + "/fetch?" + query))
      .header("X-Warp10-Token", readToken)
      .GET()
      .build();

    return this;
  }

  public Warp10 delette(String query) {
    request = HttpRequest.newBuilder()
      .uri(URI.create(endPointUri + "/delete?" + query))
      .header("X-Warp10-Token", writeToken)
      .GET()
      .build();

    return this;
  }

  public Warp10 exec(Warpscript warpscript) {
    request = HttpRequest.newBuilder()
      .uri(URI.create(endPointUri + "/update"))
      .header("Content-Type", "text/plain")
      .POST(HttpRequest.BodyPublishers.ofString(warpscript.TOKEN(readToken).formatScript()))
      .build();

    return this;
  }

  public HttpResponse<String> send() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();

    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  public CompletableFuture<HttpResponse<String>> sendAsync() {
    HttpClient client = HttpClient.newHttpClient();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }


}
