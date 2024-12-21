package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasRouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
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
    fun render(routes: List<CanvasRouteDto>, vehicles: List<CanvasVehicleDto>) {
        for (x in 0 until size) {
            for (y in 0 until size) {
                setBlock(Pair(x, y), Constants.BACKGROUND_MATERIAL)
            }
        }

        routes.forEach { route ->
            renderRoute(route)
        }

        vehicles.forEach { vehicle ->
            renderVehicle(vehicle)
        }
    }

    private fun renderRoute(route: CanvasRouteDto) {
        val material: Material = getRouteMaterialFromRoute(route.id)

        route.trips.forEach { trip ->
            trip.shape.coordinates.windowed(2).forEach { (start, end) ->
                renderLine(start, end, Constants.ROUTE_WEIGHT, material)
            }
        }
    }

    private fun renderLine(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        weight: Int,
        material: Material
    ) {
        val lineVector: Pair<Int, Int> = Pair(
            end.first - start.first,
            end.second - start.second
        )

        val steps: Int = maxOf(lineVector.first, lineVector.second)
        val stepX = lineVector.first.toDouble() / steps
        val stepY = lineVector.second.toDouble() / steps

        var x = start.first.toDouble()
        var y = start.second.toDouble()

        val range: IntRange = -weight..weight
        for (i in 0..steps) {
            for (dx in range) {
                for (dy in range) {
                    if (
                        !CircleHelper.isPointInCircle(Pair(dx, dy), weight)
                    ) {
                        continue
                    }

                    setBlock(
                        Pair((x + dx).toInt(), (y + dy).toInt()),
                        material
                    )
                }
            }

            x += stepX
            y += stepY
        }
    }

    private fun getRouteMaterialFromRoute(route: Route): Material {
        return when (route) {
            Route.RED, Route.MATTAPAN -> Material.RED_CONCRETE
            Route.ORANGE -> Material.ORANGE_CONCRETE
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> Material.GREEN_CONCRETE
            Route.BLUE -> Material.BLUE_CONCRETE
        }
    }

    private fun renderVehicle(vehicle: CanvasVehicleDto) {
        val coordinates: Pair<Int, Int>? = vehicle.coordinates
        if (coordinates == null) {
            logger.info("Skipped rendering vehicle ID ${vehicle.id} since it does not have coordinates.")
            return
        }

        val route = vehicle.route
        var material = Material.WHITE_CONCRETE
        if (route != null) {
            material = getVehicleMaterialFromRoute(route)
        }

        renderCircle(coordinates, Constants.VEHICLE_RADIUS, material)
    }

    private fun renderCircle(
        coordinates: Pair<Int, Int>,
        radius: Int,
        material: Material
    ) {
        val range: IntRange = -radius..radius
        for (dx in range) {
            for (dy in range) {
                if (!CircleHelper.isPointInCircle(Pair(dx, dy), radius)) {
                    continue
                }

                setBlock(
                    Pair(coordinates.first + dx, coordinates.second + dy),
                    material
                )
            }
        }
    }

    private fun setBlock(coordinates: Pair<Int, Int>, material: Material) {
        val blockX: Int = when (direction) {
            CanvasDirection.EAST -> originX + coordinates.first
            CanvasDirection.WEST -> originX - coordinates.first
            else -> originX
        }
        val blockY: Int = originY + coordinates.second
        val blockZ: Int = when (direction) {
            CanvasDirection.NORTH -> originZ - coordinates.first
            CanvasDirection.SOUTH -> originZ + coordinates.first
            else -> originZ
        }

        val block = world.getBlockAt(blockX, blockY, blockZ)
        block.type = material
    }

    private fun getVehicleMaterialFromRoute(route: Route): Material {
        return when (route) {
            Route.RED, Route.MATTAPAN -> Material.PEARLESCENT_FROGLIGHT
            Route.ORANGE -> Material.OCHRE_FROGLIGHT
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> Material.VERDANT_FROGLIGHT
            Route.BLUE -> Material.SEA_LANTERN
        }
    }
}