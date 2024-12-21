package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.BikesAllowed
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.WheelchairAccessibility

data class CanvasTripDto(
    val shape: CanvasShapeDto,
    val trip: TripDto
) {
    val id: String get() = trip.id
    val wheelchairAccessible: WheelchairAccessibility get() = trip.wheelchairAccessible
    val revenueStatus: RevenueStatus get() = trip.revenueStatus
    val name: String get() = trip.name
    val headsign: String get() = trip.headsign
    val directionId: Int get() = trip.directionId
    val blockId: String get() = trip.blockId
    val bikesAllowed: BikesAllowed get() = trip.bikesAllowed
    val geographicShape: ShapeDto get() = trip.shape
}