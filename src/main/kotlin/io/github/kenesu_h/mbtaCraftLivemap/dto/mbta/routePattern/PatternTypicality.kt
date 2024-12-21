package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.routePattern

enum class PatternTypicality(val id: Int) {
    NOT_DEFINED(0),
    TYPICAL(1),
    DEVIATION_FROM_REGULAR_ROUTE(2),
    HIGHLY_ATYPICAL(3),
    DIVERSION_FROM_NORMAL_SERVICE(4),
    CANONICAL(5);

    companion object {
        fun fromId(id: Int): PatternTypicality {
            val typicality: PatternTypicality? = entries.find { it.id == id }
            if (typicality == null) {
                throw IllegalArgumentException("No valid typicality found for $id.")
            }

            return typicality
        }
    }
}