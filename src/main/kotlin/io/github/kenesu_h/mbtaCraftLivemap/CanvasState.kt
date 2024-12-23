package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.util.CircleHelper
import io.github.kenesu_h.mbtaCraftLivemap.util.CoordinateNormalizer
import io.github.kenesu_h.mbtaCraftLivemap.util.LineHelper
import java.util.logging.Logger

class CanvasState(
    size: Int,
    private val logger: Logger
) {
    private val normalizer = CoordinateNormalizer(size = size)
    private var routes: List<CanvasRouteDto> = emptyList()
    private var vehicles: List<CanvasVehicleDto> = emptyList()

    fun getRoutesAtPoint(point: Pair<Int, Int>): List<CanvasRouteDto> {
        return routes.filter { route ->
            route.trips.any { trip ->
                trip.shape.coordinates.windowed(2).any { (start, end) ->
                    LineHelper.isPointNearLine(point, start, end, Constants.ROUTE_WEIGHT)
                }
            }
        }
    }

    fun getStopsAtPoint(point: Pair<Int, Int>): List<CanvasStopDto> {
        val stopNames: MutableSet<String> = mutableSetOf()

        return routes.fold(mutableListOf()) { accRoutes, nextRoute ->
            accRoutes.addAll(
                nextRoute.trips.fold(mutableListOf()) { accTrips, nextTrip ->
                    accTrips.addAll(
                        nextTrip.stops.filter { stop ->
                            val canAdd = !stopNames.contains(stop.inner.name) && CircleHelper.isPointInCircle(
                                Pair(
                                    point.first - stop.coordinates.first,
                                    point.second - stop.coordinates.second
                                ),
                                Constants.STOP_RADIUS
                            )

                            stopNames.add(stop.inner.name)
                            canAdd
                        }
                    )
                    accTrips
                }
            )
            accRoutes
        }
    }

    fun getVehiclesAtPoint(point: Pair<Int, Int>): List<CanvasVehicleDto> {
        val filtered: MutableList<CanvasVehicleDto> = mutableListOf()
        for (vehicle: CanvasVehicleDto in vehicles) {
            val coordinates: Pair<Int, Int>? = vehicle.coordinates
            if (coordinates == null) {
                logger.info("Filtered out vehicle ID ${vehicle.inner.id} since it does not have coordinates.")
                continue
            }

            if (
                CircleHelper.isPointInCircle(
                    Pair(point.first - coordinates.first, point.second - coordinates.second),
                    Constants.VEHICLE_RADIUS
                )
            ) {
                filtered.add(vehicle)
            }
        }

        return filtered
    }

    fun getRoutes(): List<CanvasRouteDto> {
        return routes
    }

    fun updateRoutes(newRoutes: List<RouteDto>) {
        routes = newRoutes.map { newRoute ->
            CanvasRouteDto(
                trips = newRoute.trips.map { newTrip ->
                    val newShape: ShapeDto = newTrip.shape
                    CanvasTripDto(
                        shape = CanvasShapeDto(
                            coordinates = newShape.coordinates.map { newCoordinates ->
                                normalizer.normalize(newCoordinates)
                            },
                            inner = newTrip.shape
                        ),
                        stops = newTrip.stops.map { newStop ->
                            CanvasStopDto(
                                coordinates = normalizer.normalize(newStop.coordinates),
                                inner = newStop
                            )
                        },
                        inner = newTrip
                    )
                },
                inner = newRoute
            )
        }
    }

    fun getVehicles(): List<CanvasVehicleDto> {
        return vehicles
    }

    fun updateVehicles(newVehicles: List<VehicleDto>) {
        vehicles = newVehicles.map {
            var coordinates: Pair<Int, Int>? = null
            if (it.coordinates != null) {
                coordinates = normalizer.normalize(it.coordinates)
            }

            CanvasVehicleDto(
                coordinates = coordinates,
                inner = it
            )
        }
    }
}