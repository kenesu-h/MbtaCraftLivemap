package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto

data class EventExternalDto(
    val type: EventType,
    val vehicles: List<VehicleExternalDto>
)