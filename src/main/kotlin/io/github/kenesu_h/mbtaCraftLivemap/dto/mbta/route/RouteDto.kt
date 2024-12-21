package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto

data class RouteDto(
    val id: Route,
    val color: String,
    val directionDestinations: List<String>?,
    val fareClass: String,
    val directionNames: List<String>?,
    val sortOrder: Int,
    val shortName: String,
    val longName: String,
    val textColor: String,
    val type: RouteType,
    val description: String,
    val trips: List<TripDto>
)