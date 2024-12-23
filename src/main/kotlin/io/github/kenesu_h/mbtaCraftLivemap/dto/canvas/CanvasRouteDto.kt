package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto

data class CanvasRouteDto(
    val trips: List<CanvasTripDto>,
    val inner: RouteDto
)