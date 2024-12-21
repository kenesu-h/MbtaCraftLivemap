package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern

import com.google.gson.annotations.SerializedName

data class RoutePatternAttributesExternalDto(
    val typicality: Int,
    @SerializedName("time_desc")
    val timeDesc: String?,
    @SerializedName("sort_order")
    val sortOrder: Int,
    val name: String,
    val directionId: Int,
    val canonical: Boolean
)