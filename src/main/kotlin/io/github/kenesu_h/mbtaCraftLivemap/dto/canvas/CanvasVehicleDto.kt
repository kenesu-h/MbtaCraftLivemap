package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleStopStatus
import java.time.ZonedDateTime

data class CanvasVehicleDto(
    val coordinates: Pair<Int, Int>?,
    val vehicle: VehicleDto
) {
    val id: String get() = vehicle.id
    val occupancyStatus: OccupancyStatus? get() = vehicle.occupancyStatus
    val speed: Double? get() = vehicle.speed
    val geographicCoordinates: Pair<Double, Double>? get() = vehicle.coordinates
    val bearing: Int? get() = vehicle.bearing
    val label: String? get() = vehicle.label
    val revenueStatus: RevenueStatus? get() = vehicle.revenueStatus
    val updatedAt: ZonedDateTime? get() = vehicle.updatedAt
    val currentStatus: VehicleStopStatus? get() = vehicle.currentStatus
    val currentStopSequence: Int? get() = vehicle.currentStopSequence
    val directionId: Int? get() = vehicle.directionId
    val carriages: List<CarriageDto> get() = vehicle.carriages
    val route: Route? get() = vehicle.route
}