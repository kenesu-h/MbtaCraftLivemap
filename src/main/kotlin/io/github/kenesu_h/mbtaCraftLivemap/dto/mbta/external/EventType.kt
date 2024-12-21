package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

enum class EventType(val type: String) {
    RESET("reset"),
    ADD("add"),
    UPDATE("update"),
    REMOVE("remove");

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromType(type: String): EventType {
            val eventType: EventType? = entries.find { it.type == type }
            if (eventType == null) {
                throw IllegalArgumentException("No valid event type found for $type.")
            }

            return eventType
        }
    }
}