package net.dericbourg.nominatim.api

data class Place(
    val placeId: Long,
    val boundingBox: List<Double>,
    val lat: Double,
    val lon: Double,
    val displayName: String,
    val category: String,
    val importance: Double,
    val placeRank: Int,
    val icon: String,
    val address: AddressDetails,
    val namedetails: Map<String, String>,
)
