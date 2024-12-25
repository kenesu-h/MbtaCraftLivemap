package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

data class CanvasVisibilityDto(
    var routes: Boolean = true,
    var stops: Boolean = true,
    var vehicles: Boolean = true
)