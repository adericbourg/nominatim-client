package net.dericbourg.nominatim.client

import com.google.gson.Gson
import net.dericbourg.nominatim.api.LookupResult
import net.dericbourg.nominatim.api.ReverseResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NominatimHttpClient internal constructor(userAgent: String, baseUrl: String? = null) : NominatimClient {

    constructor(userAgent: String) : this(userAgent, null)

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    private val api = if (baseUrl != null) {
        NominatimRetrofitApi.create(userAgent, baseUrl)
    } else {
        NominatimRetrofitApi.create(userAgent)
    }
    private val gson = Gson()

    override fun search(request: SearchRequest): SearchResponse {
        val queryMap = buildMap {
            when (val query = request.query) {
                is FreeFormSearchQuery -> put("q", query.value)
                is StructuredSearchQuery -> {
                    query.street?.let { put("street", it) }
                    query.city?.let { put("city", it) }
                    query.county?.let { put("county", it) }
                    query.state?.let { put("state", it) }
                    query.country?.let { put("country", it) }
                    query.postalCode?.let { put("postalcode", it) }
                }
            }
            request.limit?.let { put("limit", it.toString()) }
            request.countryCodes?.let { put("countrycodes", it.joinToString(",")) }
            request.viewBox?.let { put("viewbox", it.toParam()) }
            request.bounded?.let { put("bounded", if (it) "1" else "0") }
            request.excludePlaceIds?.let { put("exclude_place_ids", it.joinToString(",")) }
            request.dedupe?.let { put("dedupe", if (it) "1" else "0") }
            request.acceptLanguage?.let { put("accept-language", it) }
            request.email?.let { put("email", it) }
            request.extraTags?.let { put("extratags", if (it) "1" else "0") }
            put("addressdetails", "1")
            put("namedetails", "1")
            put("format", "jsonv2")
        }

        val response = api.search(queryMap).execute()

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
            in 400..500 -> throw BadRequestException("Invalid request")
            else -> throw UnavailableException("Unable to perform search")
        }
    }

    override fun reverse(request: ReverseRequest): ReverseResult? {
        val queryMap = buildMap {
            when (val location = request.location) {
                is CoordinatesReverseLocation -> {
                    put("lat", location.lat.toString())
                    put("lon", location.lon.toString())
                }

                is OsmObjectReverseLocation -> {
                    put("osm_type", location.type.code)
                    put("osm_id", location.id.toString())
                }
            }
            request.zoom?.let { put("zoom", it.toString()) }
            request.acceptLanguage?.let { put("accept-language", it) }
            request.email?.let { put("email", it) }
            request.extraTags?.let { put("extratags", if (it) "1" else "0") }
            put("addressdetails", "1")
            put("namedetails", "1")
            put("format", "jsonv2")
        }

        val response = api.reverse(queryMap).execute()

        if (response.isSuccessful) {
            val body = response.body()
            if (body == null || body.has("error")) {
                return null
            }
            return gson.fromJson(body, ReverseResult::class.java)
        }
        if (response.errorBody() != null) {
            log.error("Failed to reverse geocode. Got: '${response.errorBody()}'")
        }
        when (response.code()) {
            in 400..500 -> throw BadRequestException("Invalid request")
            else -> throw UnavailableException("Unable to reverse geocode")
        }
    }

    override fun lookup(request: LookupRequest): List<LookupResult> {
        val queryMap = buildMap {
            put("osm_ids", request.osmIds.joinToString(",") { it.toParam() })
            request.acceptLanguage?.let { put("accept-language", it) }
            request.email?.let { put("email", it) }
            request.extraTags?.let { put("extratags", if (it) "1" else "0") }
            put("addressdetails", "1")
            put("namedetails", "1")
            put("format", "jsonv2")
        }

        val response = api.lookup(queryMap).execute()

        if (response.isSuccessful) {
            if (response.body() == null) {
                log.warn("Got successful lookup response with empty body")
                return emptyList()
            }
            return response.body()!!
        }
        if (response.errorBody() != null) {
            log.error("Failed to lookup. Got: '${response.errorBody()}'")
        }
        when (response.code()) {
            in 400..500 -> throw BadRequestException("Invalid request")
            else -> throw UnavailableException("Unable to lookup")
        }
    }
}