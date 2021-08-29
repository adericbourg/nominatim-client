package net.dericbourg.nominatim.client

interface NominatimClient {

    /**
     * The search API allows you to look up a location from a textual description or address.
     * Nominatim supports structured and free-form search queries.
     */
    fun search(request: SearchRequest): SearchResponse

    companion object {
        fun create(): NominatimClient {
            return NominatimHttpClient()
        }
    }
}
