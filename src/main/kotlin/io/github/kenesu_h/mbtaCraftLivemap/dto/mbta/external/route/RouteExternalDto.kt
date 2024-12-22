package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.route

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto

data class RouteExternalDto(
    val type: String = "route",
    val relationships: RouteRelationshipsExternalDto,
    val links: LinksExternalDto,
    val id: String,
    val attributes: RouteAttributesExternalDto
) : IncludableExternalDto {
    fun toRouteDto(trips: List<TripDto>): RouteDto {
        return RouteDto(
            id = Route.fromId(id),
            color = attributes.color,
            directionDestinations = attributes.directionDestinations,
            fareClass = attributes.fareClass,
            directionNames = attributes.directionNames,
            sortOrder = attributes.sortOrder,
            shortName = attributes.shortName,
            longName = attributes.longName,
            textColor = attributes.textColor,
            type = TransportationType.fromId(attributes.type),
            description = attributes.description,
            trips = trips
        )
    }
}