# OpenStreetMap Nominatim client for the JVM

> ❗ This library is in an early development phase: expect stability
> issues and breaking changes. 
>
> Any help is welcome.

## Getting started

### Import the library

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/adericbourg/nominatim-client")
    }
}

dependencies {
    implementation 'net.dericbourg:nominatim-client:latest.release'
}
```

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

