package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

import java.time.ZonedDateTime

data class VehicleDto(
    val id: String,
    val occupancyStatus: OccupancyStatus?,
    val speed: Double?,
    val latitude: Double?,
    val bearing: Int?,
    val longitude: Double?,
    val label: String?,
    val revenueStatus: RevenueStatus?,
    val updatedAt: ZonedDateTime?,
    val currentStatus: VehicleStopStatus?,
    val currentStopSequence: Int?,
    val directionId: Int?,
    val carriages: List<CarriageDto>,
    val route: Route?
)