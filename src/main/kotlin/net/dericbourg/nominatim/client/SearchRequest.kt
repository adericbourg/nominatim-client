package net.dericbourg.nominatim.client

/**
 * The query to search for, either free-form or structured.
 *
 * See the [Nominatim /search documentation](https://nominatim.org/release-docs/3.3/api/Search/)
 * for the difference between the two query styles.
 */
sealed interface SearchQuery {
    companion object {
        fun freeForm(value: String): SearchQuery {
            return FreeFormSearchQuery(value)
        }

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

data class SearchRequest(val query: SearchQuery) {
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
