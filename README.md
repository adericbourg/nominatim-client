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

The [Nominatim Usage Policy](https://operations.osmfoundation.org/policies/nominatim/)
requires a valid HTTP `User-Agent` identifying your application. Set one explicitly:

```kotlin
val client = NominatimClient.create("MyApp/1.0 (contact@example.com)")
```

Alternatively, `NominatimClient.create()` reads the `NOMINATIM_USER_AGENT`
environment variable if set, and otherwise falls back to a generic default.

### Search

```kotlin
import net.dericbourg.nominatim.client.SearchRequest

val searchResponse = client.search(
    SearchRequest.query("tour eiffel, paris")
)
```

A structured query over individual address components is also supported:

```kotlin
val searchResponse = client.search(
    SearchRequest.structured(street = "avenue Anatole France", city = "Paris", country = "France")
)
```

`SearchRequest` also accepts optional result-filtering parameters (`limit`, `countryCodes`,
`viewBox`, `bounded`, `excludePlaceIds`, `dedupe`, `acceptLanguage`, `email`, `extraTags`):

```kotlin
val searchResponse = client.search(
    SearchRequest.query("tour eiffel").copy(limit = 5, countryCodes = listOf("fr"))
)
```

### Reverse geocode

```kotlin
import net.dericbourg.nominatim.client.ReverseRequest

val place = client.reverse(ReverseRequest.coordinates(lat = 48.8583, lon = 2.2945))
```

`client.reverse(...)` returns `null` when Nominatim finds no place for the given location.
A known OSM object can be reverse geocoded instead of coordinates:

```kotlin
import net.dericbourg.nominatim.api.OsmType

val place = client.reverse(ReverseRequest.osmObject(OsmType.WAY, 5013364))
```

### Lookup

```kotlin
import net.dericbourg.nominatim.client.LookupRequest
import net.dericbourg.nominatim.client.OsmId

val places = client.lookup(
    LookupRequest(osmIds = listOf(OsmId(OsmType.WAY, 5013364), OsmId(OsmType.NODE, 7992604958)))
)
```

