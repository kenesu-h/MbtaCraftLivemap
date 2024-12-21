package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.routePattern

data class RoutePatternDto(
    val id: String,
    val typicality: PatternTypicality,
    val timeDesc: String?,
    val sortOrder: Int,
    val name: String,
    val directionId: Int,
    val canonical: Boolean
)