package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.trip

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipsExternalDto

data class TripRelationshipsExternalDto(
    val route: RelationshipExternalDto,
    @SerializedName("route_pattern")
    val routePattern: RelationshipExternalDto,
    val service: RelationshipExternalDto,
    val shape: RelationshipExternalDto,
    val stops: RelationshipsExternalDto
)