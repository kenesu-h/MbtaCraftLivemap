package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.trip

enum class BikesAllowed(val id: Int) {
    NO_INFORMATION(0),
    ALLOWED(1),
    NOT_ALLOWED(2);

    companion object {
        fun fromId(id: Int): BikesAllowed {
            val allowed: BikesAllowed? = entries.find { it.id == id }
            if (allowed == null) {
                throw IllegalArgumentException("No valid bike allowed status found for $id.")
            }

            return allowed
        }
    }
}