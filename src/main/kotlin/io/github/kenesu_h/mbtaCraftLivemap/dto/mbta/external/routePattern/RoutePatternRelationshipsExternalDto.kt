package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto

class RoutePatternRelationshipsExternalDto(
    @SerializedName("representative_trip")
    val representativeTrip: RelationshipExternalDto,
    val route: RelationshipExternalDto
)