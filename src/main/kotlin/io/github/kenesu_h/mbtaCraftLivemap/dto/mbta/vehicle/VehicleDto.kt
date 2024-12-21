package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.CarriageDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import java.time.ZonedDateTime

data class VehicleDto(
    val id: String,
    val occupancyStatus: OccupancyStatus?,
    val speed: Double?,
    val coordinates: Pair<Double, Double>?,
    val bearing: Int?,
    val label: String?,
    val revenueStatus: RevenueStatus?,
    val updatedAt: ZonedDateTime?,
    val currentStatus: VehicleStopStatus?,
    val currentStopSequence: Int?,
    val directionId: Int?,
    val carriages: List<CarriageDto>,
    val route: Route?
)