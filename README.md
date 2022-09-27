# OpenStreetMap Nominatim client for the JVM

[![Java/Maven CI](https://github.com/adericbourg/nominatim-client/actions/workflows/maven-ci.yaml/badge.svg)](https://github.com/adericbourg/nominatim-client/actions/workflows/maven-ci.yaml)

> ❗ This library is in an early development phase: expect stability
> issues and breaking changes. 
>
> Any help is welcome.

## Getting started

### Create a client

```kotlin
import net.dericbourg.nominatim.client.NominatimClient

val client = NominatimClient.create()
```

### Perform a request

```kotlin
import net.dericbourg.nominatim.client.SearchRequest

val searchResponse = client.search(
    SearchRequest.query("tour eiffel, paris")
)
```

