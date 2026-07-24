package net.dericbourg.nominatim.api

import com.google.gson.annotations.SerializedName

/** A place matching a [net.dericbourg.nominatim.client.LookupRequest], as returned by the Nominatim `/lookup` endpoint. */
data class LookupResult(
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("licence") val licence: String?,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("osm_id") val osmId: Long,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("boundingbox") val boundingBox: List<Double>,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("category") val category: String,
    @SerializedName("type") val type: String,
    @SerializedName("place_rank") val placeRank: Int,
    @SerializedName("importance") val importance: Double?,
    @SerializedName("addresstype") val addressType: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("address") val address: AddressDetails,
    @SerializedName("namedetails") val nameDetails: Map<String, String>?,
    @SerializedName("extratags") val extraTags: Map<String, String>? = null,
)
