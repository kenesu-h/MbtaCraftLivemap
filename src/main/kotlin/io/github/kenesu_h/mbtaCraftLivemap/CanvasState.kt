package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.VehicleDto
import java.util.logging.Logger

class CanvasState(
    private val size: Int,
    private val logger: Logger
) {
    var vehicles: List<CanvasVehicleDto> = emptyList()

    fun getVehiclesAtPoint(x: Int, y: Int): List<CanvasVehicleDto> {
        val filtered: MutableList<CanvasVehicleDto> = mutableListOf()
        for (vehicle: CanvasVehicleDto in vehicles) {
            if (vehicle.x == null || vehicle.y == null) {
                logger.info("Filtered out vehicle ID ${vehicle.id} since it does not have coordinates.")
                continue
            }

            if (
                CircleHelper.isPointInCircle(x - vehicle.x!!, y - vehicle.y!!, Constants.VEHICLE_RADIUS)
            ) {
                filtered.add(vehicle)
            }
        }

        return filtered
    }

    fun updateVehicles(newVehicles: List<VehicleDto>) {
        vehicles = newVehicles.map {
            CanvasVehicleDto(vehicle = it, canvasSize = size)
        }
    }
}