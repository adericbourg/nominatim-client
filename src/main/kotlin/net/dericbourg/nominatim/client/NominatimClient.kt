package net.dericbourg.nominatim.client

import net.dericbourg.nominatim.api.ReverseResult

interface NominatimClient {

    /**
     * The search API allows you to look up a location from a textual description or address.
     * Nominatim supports structured and free-form search queries.
     */
    fun search(request: SearchRequest): SearchResponse

    /**
     * The reverse geocoding API generates an address from a coordinate given as `latitude` and `longitude`.
     *
     * Returns `null` when Nominatim finds no place for the given location.
     */
    fun reverse(request: ReverseRequest): ReverseResult?

    companion object {

        private const val UserAgentEnvVar = "NOMINATIM_USER_AGENT"
        private const val DefaultUserAgent = "nominatim-client (+https://github.com/adericbourg/nominatim-client)"

        /**
         * Creates a client using the `NOMINATIM_USER_AGENT` environment variable as
         * the HTTP `User-Agent`, falling back to a default value if it is not set.
         *
         * The [Nominatim Usage Policy](https://operations.osmfoundation.org/policies/nominatim/)
         * requires a valid `User-Agent` identifying your application. Prefer
         * [create] with an explicit value when possible.
         */
        fun create(): NominatimClient {
            val userAgent = System.getenv(UserAgentEnvVar) ?: DefaultUserAgent
            return NominatimHttpClient(userAgent)
        }

        /**
         * Creates a client using [userAgent] as the HTTP `User-Agent`, as required by the
         * [Nominatim Usage Policy](https://operations.osmfoundation.org/policies/nominatim/).
         */
        fun create(userAgent: String): NominatimClient {
            return NominatimHttpClient(userAgent)
        }
    }
}
