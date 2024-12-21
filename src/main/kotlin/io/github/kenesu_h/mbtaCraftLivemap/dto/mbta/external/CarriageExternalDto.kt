package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.CarriageDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus

data class CarriageExternalDto(
    @SerializedName("occupancy_status")
    val occupancyStatus: OccupancyStatus,
    @SerializedName("occupancy_percentage")
    val occupancyPercentage: Int?,
    val label: String
) {
    fun toCarriageDto(): CarriageDto {
        return CarriageDto(
            occupancyStatus = occupancyStatus,
            occupancyPercentage = occupancyPercentage,
            label = label
        )
    }
}