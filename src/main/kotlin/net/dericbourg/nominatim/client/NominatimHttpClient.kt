package net.dericbourg.nominatim.client

class NominatimHttpClient : NominatimClient {

    private val api = NominatimRetrofitApi.nominatimRetrofitApi

    override fun search(request: SearchRequest): SearchResponse {
        val queryMap = if (request is QuerySearchRequest) {
            mapOf("q" to request.value)
        } else {
            mapOf()
        }

        val response = api.search(
            queryMap + mapOf(
                "addressdetails" to "1",
                "format" to "jsonv2"
            )
        ).execute()

        // todo handle errors
        return SearchResponse(response.body()!!)
    }
}