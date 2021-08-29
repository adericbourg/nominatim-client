package net.dericbourg.nominatim.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NominatimHttpClient : NominatimClient {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

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
                "namedetails" to "1",
                "format" to "jsonv2"
            )
        ).execute()

        if (response.isSuccessful) {
            if (response.body() == null) {
                log.warn("Got successful search response with empty body")
                return SearchResponse(emptyList())
            }
            return SearchResponse(response.body()!!)
        }
        if (response.errorBody() != null) {
            log.error("Failed to search. Got: '${response.errorBody()}'")
        }
        when (response.code()) {
            in 400..500 -> throw BadRequestSearchException("Invalid request")
            else -> throw UnavailableSearchException("Unable for perform search")
        }
    }

    abstract class SearchFailureException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
    class BadRequestSearchException(message: String) : SearchFailureException(message)
    class UnavailableSearchException(message: String) : SearchFailureException(message)
}