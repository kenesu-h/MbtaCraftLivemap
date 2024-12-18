package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.CarriageDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.CarriageExternalDto
import java.time.ZonedDateTime

data class VehicleAttributesExternalDto(
    @SerializedName("occupancy_status")
    val occupancyStatus: OccupancyStatus?,
    val speed: Double?,
    val latitude: Double?,
    val bearing: Int?,
    val longitude: Double?,
    val label: String?,
    // For some odd reason, the field is named "revenue" on the event, but "revenue_status" in the Swagger docs
    @SerializedName("revenue")
    val revenueStatus: String?,
    @SerializedName("updated_at")
    val updatedAt: ZonedDateTime?,
    @SerializedName("current_status")
    val currentStatus: String?,
    @SerializedName("current_stop_sequence")
    val currentStopSequence: Int?,
    @SerializedName("direction_id")
    val directionId: Int?,
    val carriages: List<CarriageExternalDto>?
) {
    fun getCarriageDtos(): List<CarriageDto> {
        var carriageDtos: List<CarriageDto> = emptyList()
        if (carriages != null) {
            carriageDtos = carriages.map { it.toCarriageDto() }
        }

        return carriageDtos
    }
}