package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.CoordinateNormalizer
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
import java.time.ZonedDateTime

data class CanvasVehicleDto(
    val vehicle: VehicleDto,
    val canvasSize: Int
) {
    private val normalizer = CoordinateNormalizer(size = canvasSize)
    var x: Int? = null
    var y: Int? = null

    val id: String get() = vehicle.id
    val occupancyStatus: OccupancyStatus? get() = vehicle.occupancyStatus
    val speed: Double? get() = vehicle.speed
    val latitude: Double? get() = vehicle.latitude
    val bearing: Int? get() = vehicle.bearing
    val longitude: Double? get() = vehicle.longitude
    val label: String? get() = vehicle.label
    val revenueStatus: RevenueStatus? get() = vehicle.revenueStatus
    val updatedAt: ZonedDateTime? get() = vehicle.updatedAt
    val currentStatus: VehicleStopStatus? get() = vehicle.currentStatus
    val currentStopSequence: Int? get() = vehicle.currentStopSequence
    val directionId: Int? get() = vehicle.directionId
    val carriages: List<CarriageDto> get() = vehicle.carriages
    val route: Route? get() = vehicle.route

    init {
        if (vehicle.longitude != null) {
            x = normalizer.normalizeLongitude(vehicle.longitude)
        }

        if (vehicle.latitude != null) {
            y = normalizer.normalizeLatitude(vehicle.latitude)
        }
    }
}