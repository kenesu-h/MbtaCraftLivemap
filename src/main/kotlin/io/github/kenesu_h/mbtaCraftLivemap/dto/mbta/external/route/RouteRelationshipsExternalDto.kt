package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.route

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipsExternalDto

data class RouteRelationshipsExternalDto(
    val agency: RelationshipExternalDto,
    val line: RelationshipExternalDto,
    @SerializedName("route_patterns")
    val routePatterns: RelationshipsExternalDto
)