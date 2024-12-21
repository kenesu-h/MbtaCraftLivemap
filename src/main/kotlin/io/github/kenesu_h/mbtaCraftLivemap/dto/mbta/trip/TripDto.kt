package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto

data class TripDto(
    val id: String,
    val wheelchairAccessible: WheelchairAccessibility,
    val revenueStatus: RevenueStatus,
    val name: String,
    val headsign: String,
    val directionId: Int,
    val blockId: String,
    val bikesAllowed: BikesAllowed,
    val shape: ShapeDto
)