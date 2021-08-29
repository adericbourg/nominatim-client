package net.dericbourg.nominatim.api

import com.google.gson.annotations.SerializedName

data class AddressDetails(
    @SerializedName("continent") val continent: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("country_code") val countryCode: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("state_district") val stateDistrict: String?,
    @SerializedName("county") val county: String,
    @SerializedName("municipality") val municipality: String?,
    @SerializedName("postcode") val postCode: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("town") val town: String?,
    @SerializedName("village") val village: String?,
    @SerializedName("hamlet") val hamlet: String?,
    @SerializedName("croft") val croft: String?,
    @SerializedName("isolated_dwelling") val isolatedDwelling: String?,
    @SerializedName("neighbourhood") val neighbourhood: String?,
    @SerializedName("allotments") val allotments: String?,
    @SerializedName("quarter") val quarter: String?,
    @SerializedName("city_block") val cityBlock: String?,
    @SerializedName("residential") val residential: String?,
    @SerializedName("farm") val farm: String?,
    @SerializedName("farmyard") val farmyard: String?,
    @SerializedName("industrial") val industrial: String?,
    @SerializedName("commercial") val commercial: String?,
    @SerializedName("retail") val retail: String?,
    @SerializedName("road") val road: String?,
    @SerializedName("house_number") val houseNumber: String?,
    @SerializedName("house_name") val houseName: String?
)