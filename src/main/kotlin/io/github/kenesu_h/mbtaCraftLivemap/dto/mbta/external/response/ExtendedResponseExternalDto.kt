package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.response

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto

class ExtendedResponseExternalDto<T>(
    val data: List<T>,
    val included: List<IncludableExternalDto>
)