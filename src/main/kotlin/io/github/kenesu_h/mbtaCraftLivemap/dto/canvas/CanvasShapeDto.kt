package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto

data class CanvasShapeDto(
    val coordinates: List<Pair<Int, Int>>,
    val shape: ShapeDto
) {
    val id: String get() = shape.id
    val geographicCoordinates: List<Pair<Double, Double>> get() = shape.coordinates
}