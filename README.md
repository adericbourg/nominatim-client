# OpenStreetMap Nominatim client for the JVM

[![Java/Gradle CI](https://github.com/adericbourg/nominatim-client/actions/workflows/gradle-ci.yaml/badge.svg)](https://github.com/adericbourg/nominatim-client/actions/workflows/gradle-ci.yaml)

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
        credentials {
            username $githubUser
            password $githubToken
        }
    }
}

dependencies {
    implementation 'net.dericbourg:nominatim-client:latest.release'
}
```

> [How to get my `githubToken`?](https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token)

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

