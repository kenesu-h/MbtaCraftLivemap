package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

data class CarriageDto(
    val occupancyStatus: OccupancyStatus,
    val occupancyPercentage: Int?,
    val label: String
)