package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.trip

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.WheelchairAccessibility
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.StopDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.BikesAllowed
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip.TripDto

data class TripExternalDto(
    val type: String = "trip",
    val relationships: TripRelationshipsExternalDto,
    val links: LinksExternalDto,
    val id: String,
    val attributes: TripAttributesExternalDto
) : IncludableExternalDto {
    fun toTripDto(shape: ShapeDto, stops: List<StopDto>): TripDto {
        return TripDto(
            id = id,
            wheelchairAccessible = WheelchairAccessibility.fromId(attributes.wheelchairAccessible),
            revenueStatus = attributes.revenueStatus,
            name = attributes.name,
            headsign = attributes.headsign,
            directionId = attributes.directionId,
            blockId = attributes.blockId,
            bikesAllowed = BikesAllowed.fromId(attributes.bikesAllowed),
            shape = shape,
            stops = stops
        )
    }
}