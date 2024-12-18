package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.VehicleDto

data class VehicleExternalDto(
    val type: String = "vehicle",
    val relationships: VehicleRelationshipsExternalDto?,
    val links: VehicleLinksExternalDto?,
    val id: String,
    val attributes: VehicleAttributesExternalDto?
) {
    fun toVehicleDto(): VehicleDto {
        var route: Route? = null
        if (relationships != null) {
            route = Route.fromId(relationships.route.data.id)
        }

        return VehicleDto(
            id = id,
            occupancyStatus = attributes?.occupancyStatus,
            speed = attributes?.speed,
            latitude = attributes?.latitude,
            bearing = attributes?.bearing,
            longitude = attributes?.longitude,
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