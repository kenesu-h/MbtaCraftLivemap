package io.github.kenesu_h.mbtaCraftLivemap.event

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.EventExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.EventType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.gson.ZonedDateTimeAdapter
import java.io.BufferedReader
import java.time.ZonedDateTime

class EventReader(private val reader: BufferedReader) {
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter()).create()

    private var eventType: EventType? = null
    private var eventData: String = ""

    fun readEvent(): EventExternalDto? {
        while (true) {
            val line: String? = reader.readLine()

            val event: EventExternalDto? = processLine(line ?: "")
            if (event != null) {
                return event
            }

            if (line == null) {
                break
            }
        }

        return null
    }

    private fun processLine(line: String): EventExternalDto? {
        when {
            line.startsWith("event:") -> {
                val externalType: String = line.removePrefix("event:").trim()
                eventType = EventType.fromType(externalType)
            }

            line.startsWith("data:") -> {
                val externalData: String = line.removePrefix("data:").trim()
                eventData += externalData
            }

            line.isEmpty() -> {
                val event: EventExternalDto? = eventType?.let { readEventData(it, eventData) }

                eventType = null
                eventData = ""

                if (event != null) {
                    return event
                }
            }
        }

        return null
    }

    private fun readEventData(type: EventType, data: String): EventExternalDto {
        val vehicles: List<VehicleExternalDto> = when (type) {
            EventType.RESET -> {
                gson.fromJson(data, Array<VehicleExternalDto>::class.java).toList()
            }

            else -> {
                listOf(gson.fromJson(data, VehicleExternalDto::class.java))
            }
        }

        return EventExternalDto(
            type = type,
            vehicles = vehicles
        )
    }
}