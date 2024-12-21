package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship

data class RelationshipsExternalDto(
    val links: RelationshipLinksExternalDto?,
    val data: List<RelationshipDataExternalDto>
)