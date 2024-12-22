package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.WheelchairAccessibility

data class StopDto(
    val id: String,
    val onStreet: String?,
    val locationType: LocationType,
    val name: String,
    val coordinates: Pair<Double, Double>,
    val vehicleType: TransportationType?,
    val atStreet: String?,
    val wheelchairBoarding: WheelchairAccessibility,
    val address: String?,
    val platformName: String?,
    val platformCode: String?,
    val municipality: String?,
    val description: String?
)