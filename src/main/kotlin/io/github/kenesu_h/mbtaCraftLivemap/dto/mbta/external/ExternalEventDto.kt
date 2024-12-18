package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto

data class ExternalEventDto(
    val type: ExternalEventType,
    val vehicles: List<VehicleExternalDto>
)