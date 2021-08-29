package net.dericbourg.nominatim.api

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("boundingbox") val boundingBox: List<Double>,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("category") val category: String,
    @SerializedName("importance") val importance: Double,
    @SerializedName("place_rank") val placeRank: Int,
    @SerializedName("icon") val icon: String?,
    @SerializedName("address") val address: AddressDetails,
    @SerializedName("namedetails") val nameDetails: Map<String, String>,
)
