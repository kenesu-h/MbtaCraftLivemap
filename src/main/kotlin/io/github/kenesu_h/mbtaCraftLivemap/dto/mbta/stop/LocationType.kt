package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop

enum class LocationType(val id: Int) {
    STOP(0),
    STATION(1),
    STATION_ENTRANCE_OR_EXIT(2),
    GENERIC_NODE(3);

    companion object {
        fun fromId(id: Int): LocationType {
            val type: LocationType? = entries.find { it.id == id }
            if (type == null) {
                throw IllegalArgumentException("No valid location type found for $id.")
            }

            return type
        }
    }
}