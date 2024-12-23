package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.StopDto

data class CanvasStopDto(
    val coordinates: Pair<Int, Int>,
    val inner: StopDto
)