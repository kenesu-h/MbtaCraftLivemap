package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto

data class CanvasVehicleDto(
    val coordinates: Pair<Int, Int>?,
    val inner: VehicleDto
)