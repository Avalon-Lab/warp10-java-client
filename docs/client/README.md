## Client
The first part of the library is a client to interact with the [warp10](http://www.warp10.io/) Rest API.  

The client class is the `Warp10.java` class.  It require at least your warp10 instance URL.  
You can also provide your read token and your write token.  

To ease its usage, if you use a dependency injection framework we provide an injectable client `Warp10InjectableClient.java`
 that use three named properties : 
- warp10.endpoint
- warp10.token.write
- warp10.token.read

This client is designed as a builder, it exposes two methods to set the tokens `withWriteToken` and `withReadToken`.  
It also exposes the complete [warp10 APIs](http://www.warp10.io/apis/) : 
- fetch
- delete
- exec
- ingress  

Each function take a String query, so you can use the Client part without the DSL part, 
independently.

Its final operations are `send()` and `sendAsync()`, the last one return a `CompletableFuture` to handle your request 
asynchronously.

Example : 
````jvm
List<GTSOutput> response = Warp10.instance(endpointURL)
    .withReadToken(readToken)
    .fetch(query)
    .send();   
````