package io.github.kenesu_h.mbtaCraftLivemap

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.ExternalEventDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.ExternalEventType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto
import java.io.BufferedReader
import java.time.ZonedDateTime

class EventReader(private val reader: BufferedReader) {
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter()).create()

    private var eventType: ExternalEventType? = null
    private var eventData: String = ""

    fun readEvent(): ExternalEventDto? {
        while (true) {
            val line: String? = reader.readLine()

            val event: ExternalEventDto? = processLine(line ?: "")
            if (event != null) {
                return event
            }

            if (line == null) {
                break
            }
        }

        return null
    }

    private fun processLine(line: String): ExternalEventDto? {
        when {
            line.startsWith("event:") -> {
                val externalType: String = line.removePrefix("event:").trim()
                eventType = ExternalEventType.fromType(externalType)
            }

            line.startsWith("data:") -> {
                val externalData: String = line.removePrefix("data:").trim()
                eventData += externalData
            }

            line.isEmpty() -> {
                val event: ExternalEventDto? = eventType?.let { readEventData(it, eventData) }

                eventType = null
                eventData = ""

                if (event != null) {
                    return event
                }
            }
        }

        return null
    }

    private fun readEventData(type: ExternalEventType, data: String): ExternalEventDto {
        val vehicles: List<VehicleExternalDto> = when (type) {
            ExternalEventType.RESET -> {
                gson.fromJson(data, Array<VehicleExternalDto>::class.java).toList()
            }

            else -> {
                listOf(gson.fromJson(data, VehicleExternalDto::class.java))
            }
        }

        return ExternalEventDto(
            type = type,
            vehicles = vehicles
        )
    }
}