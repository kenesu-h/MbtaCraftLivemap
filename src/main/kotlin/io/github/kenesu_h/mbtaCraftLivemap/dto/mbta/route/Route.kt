package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route

enum class Route(val id: String) {
    RED("Red"),
    MATTAPAN("Mattapan"),
    ORANGE("Orange"),
    GREEN_B("Green-B"),
    GREEN_C("Green-C"),
    GREEN_D("Green-D"),
    GREEN_E("Green-E"),
    BLUE("Blue");

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromId(id: String): Route {
            val route: Route? = entries.find { it.id == id }
            if (route == null) {
                throw IllegalArgumentException("No valid route found for $id.")
            }

            return route
        }
    }
}