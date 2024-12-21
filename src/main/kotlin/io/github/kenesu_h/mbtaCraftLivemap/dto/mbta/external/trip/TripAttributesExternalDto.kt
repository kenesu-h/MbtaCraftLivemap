package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.trip

import com.google.gson.annotations.SerializedName
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus

data class TripAttributesExternalDto(
    @SerializedName("wheelchair_accessible")
    val wheelchairAccessible: Int,
    @SerializedName("revenue")
    val revenueStatus: RevenueStatus,
    val name: String,
    val headsign: String,
    @SerializedName("direction_id")
    val directionId: Int,
    @SerializedName("block_id")
    val blockId: String,
    @SerializedName("bikes_allowed")
    val bikesAllowed: Int
)