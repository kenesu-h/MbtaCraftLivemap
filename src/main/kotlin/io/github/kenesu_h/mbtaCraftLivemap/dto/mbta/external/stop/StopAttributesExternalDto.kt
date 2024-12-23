package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.stop

import com.google.gson.annotations.SerializedName

data class StopAttributesExternalDto(
    @SerializedName("on_street")
    val onStreet: String?,
    @SerializedName("location_type")
    val locationType: Int,
    val name: String,
    val latitude: Double,
    @SerializedName("vehicle_type")
    val vehicleType: Int?,
    @SerializedName("at_street")
    val atStreet: String?,
    val longitude: Double,
    @SerializedName("wheelchair_boarding")
    val wheelchairBoarding: Int,
    val address: String?,
    @SerializedName("platform_name")
    val platformName: String?,
    @SerializedName("platform_code")
    val platformCode: String?,
    val municipality: String?,
    val description: String?
)