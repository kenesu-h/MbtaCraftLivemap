package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

enum class TransportationType(val id: Int) {
    LIGHT_RAIL(0),
    HEAVY_RAIL(1),
    COMMUTER_RAIL(2),
    BUS(3),
    FERRY(4);

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromId(id: Int): TransportationType {
            val transportationType: TransportationType? = entries.find { it.id == id }
            if (transportationType == null) {
                throw IllegalArgumentException("No valid transportation type found for $id")
            }

            return transportationType
        }
    }
}