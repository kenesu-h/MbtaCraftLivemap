package io.github.kenesu_h.mbtaCraftLivemap.renderer

import io.github.kenesu_h.mbtaCraftLivemap.constant.CanvasConstant
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import org.bukkit.Material
import org.bukkit.World
import java.util.logging.Logger

class VehicleRenderer(
    world: World,
    originX: Int,
    originY: Int,
    originZ: Int,
    size: Int,
    direction: CanvasDirection,
    private val vehicle: CanvasVehicleDto,
    private val logger: Logger
) : BaseRenderer(
    world, originX, originY, originZ, size, direction
) {
    override fun render() {
        val coordinates: Pair<Int, Int>? = vehicle.coordinates
        if (coordinates == null) {
            logger.info("Skipped rendering vehicle ID ${vehicle.inner.id} since it does not have coordinates.")
            return
        }

        renderCircle(coordinates, CanvasConstant.VEHICLE_RADIUS, Material.WAXED_COPPER_BULB, true)
    }
}