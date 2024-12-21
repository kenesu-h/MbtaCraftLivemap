package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.response

class ExtendedResponseExternalDto<T, U>(
    val data: List<T>,
    val included: List<U>
)