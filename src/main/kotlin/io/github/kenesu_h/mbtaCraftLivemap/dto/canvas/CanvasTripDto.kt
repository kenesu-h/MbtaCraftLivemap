package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto

data class CanvasTripDto(
    val shape: CanvasShapeDto,
    val stops: List<CanvasStopDto>,
    val inner: TripDto
)