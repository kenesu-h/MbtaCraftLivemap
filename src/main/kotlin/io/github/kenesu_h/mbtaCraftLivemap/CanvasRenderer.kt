package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.Route
import org.bukkit.Material
import org.bukkit.World
import java.util.logging.Logger

class CanvasRenderer(
    private val world: World,
    private val originX: Int,
    private val originY: Int,
    private val originZ: Int,
    private val size: Int,
    private val direction: CanvasDirection,
    private val logger: Logger
) {
    fun render(vehicles: List<CanvasVehicleDto>) {
        for (x in 0 until size) {
            for (y in 0 until size) {
                setBlock(x, y, Constants.BACKGROUND_MATERIAL)
            }
        }

        vehicles.forEach { vehicle ->
            renderVehicle(vehicle)
        }
    }

    private fun renderVehicle(vehicle: CanvasVehicleDto) {
        if (vehicle.x == null || vehicle.y == null) {
            logger.info("Skipped rendering vehicle ID ${vehicle.id} since it does not have coordinates.")
        }

        val range: IntRange = -Constants.VEHICLE_RADIUS..Constants.VEHICLE_RADIUS
        for (x in range) {
            for (y in range) {
                if (!CircleHelper.isPointInCircle(x, y, Constants.VEHICLE_RADIUS)) {
                    continue
                }

                val route = vehicle.route
                var material = Material.WHITE_CONCRETE
                if (route != null) {
                    material = getMaterialFromRoute(route)
                }

                setBlock(vehicle.x!! + x, vehicle.y!! + y, material)
            }
        }
    }

    private fun setBlock(x: Int, y: Int, material: Material) {
        val blockX: Int = when (direction) {
            CanvasDirection.EAST -> originX + x
            CanvasDirection.WEST -> originX - x
            else -> originX
        }
        val blockY: Int = originY + y
        val blockZ: Int = when (direction) {
            CanvasDirection.NORTH -> originZ - x
            CanvasDirection.SOUTH -> originZ + x
            else -> originZ
        }

        val block = world.getBlockAt(blockX, blockY, blockZ)
        block.type = material
    }

    private fun getMaterialFromRoute(route: Route): Material {
        return when (route) {
            Route.RED, Route.MATTAPAN -> Material.RED_CONCRETE
            Route.ORANGE -> Material.ORANGE_CONCRETE
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> Material.GREEN_CONCRETE
            Route.BLUE -> Material.BLUE_CONCRETE
        }
    }
}