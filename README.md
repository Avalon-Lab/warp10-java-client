# warp10-java-client

[![pipeline status](https://gitlab.com/avalon-lab/oss/warp10-java-client/badges/integration/pipeline.svg)](https://gitlab.com/avalon-lab/oss/warp10-java-client/commits/integration)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=warp10-java-client&metric=alert_status)](https://sonarcloud.io/api/project_badges/measure?project=warp10-java-client)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=warp10-java-client&metric=coverage)](https://sonarcloud.io/api/project_badges/measure?project=warp10-java-client)

This library provide a Java 11 Client and a DSL for Warp10.  
It use the newly generally available (with jdk11) HTTP API (HttpClient, HttpRequest, HttpResponse).

**DISCLAIMER : this library, even if working, is still a huge work in progress, we extracted it from one of our internal project because we thought that it can be usefull for somebody.  
It's features reflect currently what we need for our project.**

## Warp 10

[Warp10](http://www.warp10.io/) is a Geo-Time Series database, unlike many databases there is no driver, you interact
with the database via its own REST API.

## Reference Documentation

The reference documentation is available at https://avalon-lab.github.io/warp10-java-client/

## Contributing

Contributions are _very_ welcome!

If you see an issue that you'd like to see fixed, the best way to make it happen is to help out by submitting a pull request implementing it.

## License

warp10-java-client is Open Source and available under the Apache 2 License.
