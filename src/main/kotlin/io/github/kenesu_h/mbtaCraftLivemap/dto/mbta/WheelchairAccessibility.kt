package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

enum class WheelchairAccessibility(val id: Int) {
    NO_INFORMATION(0),
    ACCESSIBLE(1),
    INACCESSIBLE(2);

    companion object {
        fun fromId(id: Int): WheelchairAccessibility {
            val accessibility: WheelchairAccessibility? = entries.find { it.id == id }
            if (accessibility == null) {
                throw IllegalArgumentException("No valid wheelchair accessibility found for $id.")
            }

            return accessibility
        }
    }
}