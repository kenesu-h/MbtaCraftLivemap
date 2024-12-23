package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto

data class CanvasShapeDto(
    val coordinates: List<Pair<Int, Int>>,
    val inner: ShapeDto
)