package net.dericbourg.nominatim.client

/**
 * The query to search for, either free-form or structured.
 *
 * See the [Nominatim /search documentation](https://nominatim.org/release-docs/3.3/api/Search/)
 * for the difference between the two query styles.
 */
sealed interface SearchQuery {
    companion object {
        /** A free-form search, e.g. `SearchQuery.freeForm("tour eiffel, paris")`. */
        fun freeForm(value: String): SearchQuery {
            return FreeFormSearchQuery(value)
        }

        /** A structured search over individual address components. */
        fun structured(
            street: String? = null,
            city: String? = null,
            county: String? = null,
            state: String? = null,
            country: String? = null,
            postalCode: String? = null,
        ): SearchQuery {
            return StructuredSearchQuery(
                street = street,
                city = city,
                county = county,
                state = state,
                country = country,
                postalCode = postalCode,
            )
        }
    }
}

internal data class FreeFormSearchQuery(val value: String) : SearchQuery

internal data class StructuredSearchQuery(
    val street: String?,
    val city: String?,
    val county: String?,
    val state: String?,
    val country: String?,
    val postalCode: String?,
) : SearchQuery

/**
 * A rectangular area, expressed as its two opposite corners, used to bias or restrict
 * search results (Nominatim's `viewbox` parameter).
 */
data class ViewBox(val x1: Double, val y1: Double, val x2: Double, val y2: Double) {
    internal fun toParam(): String = "$x1,$y1,$x2,$y2"
}

/** A request to the Nominatim `/search` endpoint. */
data class SearchRequest(
    val query: SearchQuery,
    /** Limit the number of returned results (1-50, Nominatim default is 10). */
    val limit: Int? = null,
    /** Limit results to the given ISO 3166-1alpha2 country codes. */
    val countryCodes: List<String>? = null,
    /** Preferred area to find search results (`bounded` controls whether it's a hard restriction). */
    val viewBox: ViewBox? = null,
    /** Restrict results to those contained within [viewBox]. */
    val bounded: Boolean? = null,
    /** OSM place ids to skip in the results, e.g. to paginate through repeated searches. */
    val excludePlaceIds: List<Long>? = null,
    /** Whether to detect and remove duplicate OSM objects (Nominatim default is true). */
    val dedupe: Boolean? = null,
    /** Preferred language order for results, as an RFC2616 Accept-Language string. */
    val acceptLanguage: String? = null,
    /** Identifies the request source for usage tracking, per the Nominatim usage policy. */
    val email: String? = null,
    /** Include additional information in the result if available (e.g. Wikipedia link, opening hours). */
    val extraTags: Boolean? = null,
) {
    companion object {
        /** A free-form search, e.g. `SearchRequest.query("tour eiffel, paris")`. */
        fun query(value: String): SearchRequest {
            return SearchRequest(SearchQuery.freeForm(value))
        }

        /** A structured search over individual address components. */
        fun structured(
            street: String? = null,
            city: String? = null,
            county: String? = null,
            state: String? = null,
            country: String? = null,
            postalCode: String? = null,
        ): SearchRequest {
            return SearchRequest(
                SearchQuery.structured(
                    street = street,
                    city = city,
                    county = county,
                    state = state,
                    country = country,
                    postalCode = postalCode,
                )
            )
        }
    }
}
