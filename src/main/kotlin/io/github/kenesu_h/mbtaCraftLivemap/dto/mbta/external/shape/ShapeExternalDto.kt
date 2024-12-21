package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.shape

import io.github.kenesu_h.mbtaCraftLivemap.PolylineDecoder
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.ShapeDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto

data class ShapeExternalDto(
    val type: String = "shape",
    val links: LinksExternalDto,
    val id: String,
    val attributes: ShapeAttributesExternalDto
) {
    fun toShapeDto(): ShapeDto {
        return ShapeDto(
            id = id,
            coordinates = PolylineDecoder.decode(attributes.polyline)
        )
    }
}