package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.OsmType

/** An OSM object reference, as used by the `/lookup` endpoint's `osm_ids` parameter. */
data class OsmId(val type: OsmType, val id: Long) {
    internal fun toParam(): String = "${type.code}$id"
}

/**
 * The lookup API allows querying the address and other details of one or multiple places by
 * their OSM ids. Nominatim accepts at most 50 [osmIds] per request.
 *
 * See the [Nominatim /lookup documentation](https://nominatim.org/release-docs/3.3/api/Lookup/).
 */
data class LookupRequest(
    val osmIds: List<OsmId>,
    /** Preferred language order for results, as an RFC2616 Accept-Language string. */
    val acceptLanguage: String? = null,
    /** Identifies the request source for usage tracking, per the Nominatim usage policy. */
    val email: String? = null,
    /** Include additional information in the result if available (e.g. Wikipedia link, opening hours). */
    val extraTags: Boolean? = null,
)
