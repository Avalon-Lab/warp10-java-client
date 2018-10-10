# warp10-java-client
This library provide a Java 11 Client and a DSL for Warp10.  
It use the newly generally available (with jdk11) HTTP API (HttpClient, HttpRequest, HttpResponse).

## The Client
The first part of the library is a client to interact with the [warp10](http://www.warp10.io/) Rest API.  

The client class is the `Warp10.java` class. It require at least your warp10 instance URL.  
You can also provide your read token and your write token.  

To ease its usage, if you use a dependency injection framework we provide an injectable client `Warp10InjectableClient.java` that will three named properties : 
- warp10.endpoint
- warp10.token.write
- warp10.token.read

If you use Guice, we also provide a Guice module that you cas use : `Warp10ClientModule.java`

This client designed as a builder, exposes two methods to set the tokens `withWriteToken` and `withReadToken`.  
It also exposes the complete [warp10 APIs](http://www.warp10.io/apis/) : 
- fetch
- delete
- exec
- ingress  

Its final operations are `send()` and `sendAsync()`, the last one return a `CompletableFuture` to handle your request asynchronously.


## The DSL
WIP
