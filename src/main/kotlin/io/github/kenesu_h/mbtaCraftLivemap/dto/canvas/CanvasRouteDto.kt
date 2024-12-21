package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto

class CanvasRouteDto(
    val trips: List<CanvasTripDto>,
    val route: RouteDto
) {
    val id: Route get() = route.id
    val color: String get() = route.color
    val directionDestinations: List<String>? get() = route.directionDestinations
    val fareClass: String get() = route.fareClass
    val directionNames: List<String>? get() = route.directionNames
    val sortOrder: Int get() = route.sortOrder
    val shortName: String get() = route.shortName
    val longName: String get() = route.longName
    val textColor: String get() = route.textColor
    val type: RouteType get() = route.type
    val description: String get() = route.description
    val geographicTrips: List<TripDto> = route.trips
}