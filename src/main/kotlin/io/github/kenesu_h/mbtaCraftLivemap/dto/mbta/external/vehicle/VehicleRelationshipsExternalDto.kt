package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto

data class VehicleRelationshipsExternalDto(
    val trip: RelationshipExternalDto,
    val stop: RelationshipExternalDto,
    val route: RelationshipExternalDto
)