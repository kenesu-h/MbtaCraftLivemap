package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

data class ShapeDto(
    val id: String,
    val coordinates: List<Pair<Double, Double>>
)