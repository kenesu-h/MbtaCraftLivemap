package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.route

import com.google.gson.annotations.SerializedName

data class RouteAttributesExternalDto(
    val color: String,
    @SerializedName("direction_destinations")
    val directionDestinations: List<String>?,
    @SerializedName("fare_class")
    val fareClass: String,
    @SerializedName("direction_names")
    val directionNames: List<String>?,
    @SerializedName("sort_order")
    val sortOrder: Int,
    @SerializedName("short_name")
    val shortName: String,
    @SerializedName("long_name")
    val longName: String,
    @SerializedName("text_color")
    val textColor: String,
    val type: Int,
    val description: String
)