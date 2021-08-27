package net.dericbourg.nominatim.api

data class AddressDetails(
    val continent: String?,
    val country: String?,

    val countryCode: String?,
    val county: String,
    val municipality: String?,
    val city: String?,
    val town: String?,
    val village: String?,
    val hamlet: String?,
    val croft: String?,
    val isolatedDwelling: String?,
    val neighbourhood: String?,
    val allotments: String?,
    val quarter: String?,
    val cityBlock: String?,
    val residential: String?,
    val farm: String?,
    val farmyard: String?,
    val industrial: String?,
    val commercial: String?,
    val retail: String?,
    val road: String?,
    val houseNumber: String,
    val houseName: String?
)