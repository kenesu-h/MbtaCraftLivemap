package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route

enum class RouteType(val id: Int) {
    LIGHT_RAIL(0),
    HEAVY_RAIL(1),
    COMMUTER_RAIL(2),
    BUS(3),
    FERRY(4);

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromId(id: Int): RouteType {
            val routeType: RouteType? = entries.find { it.id == id }
            if (routeType == null) {
                throw IllegalArgumentException("No valid route type found for $id")
            }

            return routeType
        }
    }
}