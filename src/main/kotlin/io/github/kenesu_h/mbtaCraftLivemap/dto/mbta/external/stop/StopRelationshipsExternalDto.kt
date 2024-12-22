package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.stop

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto

data class StopRelationshipsExternalDto(
    val facilities: RelationshipExternalDto,
    @SerializedName("parent_station")
    val parentStation: RelationshipExternalDto,
    val zone: RelationshipExternalDto
)