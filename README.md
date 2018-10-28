# warp10-java-client
This library provide a Java 11 Client and a DSL for Warp10.  
It use the newly generally available (with jdk11) HTTP API (HttpClient, HttpRequest, HttpResponse).  

**DISCLAIMER : this library, even if working, is still a huge work in progress, we extracted it from one of our internal project because we thought that it can be usefull for somebody.  
It's features reflect currently what we need for our project.**

## Warp 10
[Warp10](http://www.warp10.io/) is a Geo-Time Series database, unlike many databases there is no driver, you interact 
with the database via its own REST API.

## Prerequisites
This project require at least **Java 11**.

## Table of Contents
* [Client](#client)
* [DSL](#dsl)

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


## DSL
WIP
