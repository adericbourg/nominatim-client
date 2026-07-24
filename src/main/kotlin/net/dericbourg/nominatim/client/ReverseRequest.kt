package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.OsmType

/**
 * The location to reverse geocode, either WGS84 coordinates or an OSM object reference.
 *
 * See the [Nominatim /reverse documentation](https://nominatim.org/release-docs/3.3/api/Reverse/).
 */
sealed interface ReverseLocation {
    companion object {
        fun coordinates(lat: Double, lon: Double): ReverseLocation {
            return CoordinatesReverseLocation(lat, lon)
        }

        fun osmObject(type: OsmType, id: Long): ReverseLocation {
            return OsmObjectReverseLocation(type, id)
        }
    }
}

internal data class CoordinatesReverseLocation(val lat: Double, val lon: Double) : ReverseLocation

internal data class OsmObjectReverseLocation(val type: OsmType, val id: Long) : ReverseLocation

data class ReverseRequest(
    val location: ReverseLocation,
    /** Level of detail for the address breakdown, 0 (country) to 18 (building); Nominatim default is 18. */
    val zoom: Int? = null,
    /** Preferred language order for results, as an RFC2616 Accept-Language string. */
    val acceptLanguage: String? = null,
    /** Identifies the request source for usage tracking, per the Nominatim usage policy. */
    val email: String? = null,
    /** Include additional information in the result if available (e.g. Wikipedia link, opening hours). */
    val extraTags: Boolean? = null,
) {
    companion object {
        /** Reverse geocode WGS84 [lat]/[lon] coordinates. */
        fun coordinates(lat: Double, lon: Double): ReverseRequest {
            return ReverseRequest(ReverseLocation.coordinates(lat, lon))
        }

        /** Reverse geocode a known OSM object, e.g. `ReverseRequest.osmObject(OsmType.WAY, 5013364)`. */
        fun osmObject(type: OsmType, id: Long): ReverseRequest {
            return ReverseRequest(ReverseLocation.osmObject(type, id))
        }
    }
}
