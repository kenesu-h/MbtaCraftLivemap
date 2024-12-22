package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.stop

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.WheelchairAccessibility
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.LocationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.StopDto

data class StopExternalDto(
    val type: String = "stop",
    val relationships: StopRelationshipsExternalDto,
    val links: LinksExternalDto,
    val id: String,
    val attributes: StopAttributesExternalDto
) : IncludableExternalDto {
    fun toStopDto(): StopDto {
        val locationType = LocationType.fromId(attributes.locationType)
        val coordinates: Pair<Double, Double> = Pair(attributes.latitude, attributes.longitude)

        var vehicleType: TransportationType? = null
        if (attributes.vehicleType != null) {
            vehicleType = TransportationType.fromId(attributes.vehicleType)
        }

        val wheelchairBoarding = WheelchairAccessibility.fromId(attributes.wheelchairBoarding)

        return StopDto(
            id = id,
            onStreet = attributes.onStreet,
            locationType = locationType,
            name = attributes.name,
            coordinates = coordinates,
            vehicleType = vehicleType,
            atStreet = attributes.atStreet,
            wheelchairBoarding = wheelchairBoarding,
            address = attributes.address,
            platformName = attributes.platformName,
            platformCode = attributes.platformCode,
            municipality = attributes.municipality,
            description = attributes.description,
        )
    }
}