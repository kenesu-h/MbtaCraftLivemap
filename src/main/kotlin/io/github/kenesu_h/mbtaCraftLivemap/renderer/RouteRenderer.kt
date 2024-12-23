package io.github.kenesu_h.mbtaCraftLivemap.renderer

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasRouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasStopDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import org.bukkit.Material
import org.bukkit.World

class RouteRenderer(
    world: World,
    originX: Int,
    originY: Int,
    originZ: Int,
    size: Int,
    direction: CanvasDirection,
    private val route: CanvasRouteDto
) : BaseRenderer(
    world, originX, originY, originZ, size, direction
) {
    override fun render() {
        val material: Material = getRouteMaterialFromRoute(route.inner.id)

        val stopNames: MutableSet<String> = mutableSetOf()
        route.trips.forEach { trip ->
            trip.shape.coordinates.windowed(2).forEach { (start, end) ->
                renderLine(start, end, Constants.ROUTE_WEIGHT, material)
            }
        }

        route.trips.forEach { trip ->
            trip.stops.forEach { stop ->
                if (!stopNames.contains(stop.inner.name)) {
                    renderCircle(
                        stop.coordinates,
                        Constants.STOP_RADIUS,
                        getStopMaterialFromRoute(route.inner.id)
                    )
                }

                stopNames.add(stop.inner.name)
            }
        }
    }

    private fun getRouteMaterialFromRoute(route: Route): Material {
        return when (route) {
            Route.RED, Route.MATTAPAN -> Material.RED_CONCRETE
            Route.ORANGE -> Material.ORANGE_TERRACOTTA
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> Material.GREEN_CONCRETE
            Route.BLUE -> Material.BLUE_CONCRETE
            Route.COMMUTER_RAIL_FAIRMOUNT -> Material.PURPLE_CONCRETE
        }
    }

    private fun getStopMaterialFromRoute(route: Route): Material {
        return when (route) {
            Route.RED, Route.MATTAPAN, Route.COMMUTER_RAIL_FAIRMOUNT -> Material.PEARLESCENT_FROGLIGHT
            Route.ORANGE -> Material.OCHRE_FROGLIGHT
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> Material.VERDANT_FROGLIGHT
            Route.BLUE -> Material.SEA_LANTERN
        }
    }
}