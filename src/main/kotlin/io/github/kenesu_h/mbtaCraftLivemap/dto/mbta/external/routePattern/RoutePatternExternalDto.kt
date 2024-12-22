package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.routePattern.PatternTypicality
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.routePattern.RoutePatternDto

data class RoutePatternExternalDto(
    val type: String = "route_pattern",
    val relationships: RoutePatternRelationshipsExternalDto,
    val links: LinksExternalDto,
    val id: String,
    val attributes: RoutePatternAttributesExternalDto
) : IncludableExternalDto {
    fun toRoutePatternDto(): RoutePatternDto {
        return RoutePatternDto(
            id = id,
            typicality = PatternTypicality.fromId(attributes.typicality),
            timeDesc = attributes.timeDesc,
            sortOrder = attributes.sortOrder,
            name = attributes.name,
            directionId = attributes.directionId,
            canonical = attributes.canonical
        )
    }
}