package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus

data class CarriageDto(
    val occupancyStatus: OccupancyStatus,
    val occupancyPercentage: Int?,
    val label: String
)