package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto

data class VehicleExternalDto(
    val type: String = "vehicle",
    val relationships: VehicleRelationshipsExternalDto?,
    val links: LinksExternalDto?,
    val id: String,
    val attributes: VehicleAttributesExternalDto?
) : IncludableExternalDto {
    fun toVehicleDto(): VehicleDto {
        var route: Route? = null
        if (relationships != null) {
            // Route relationships populate `data`
            route = Route.fromId(relationships.route.data!!.id)
        }

        var coordinates: Pair<Double, Double>? = null
        if (attributes?.latitude != null && attributes.longitude != null) {
            coordinates = Pair(attributes.latitude, attributes.longitude)
        }

        return VehicleDto(
            id = id,
            occupancyStatus = attributes?.occupancyStatus,
            speed = attributes?.speed,
            coordinates = coordinates,
            bearing = attributes?.bearing,
            label = attributes?.label,
            revenueStatus = attributes?.revenueStatus,
            updatedAt = attributes?.updatedAt,
            currentStatus = attributes?.currentStatus,
            currentStopSequence = attributes?.currentStopSequence,
            directionId = attributes?.directionId,
            carriages = attributes?.getCarriageDtos() ?: emptyList(),
            route = route
        )
    }
}