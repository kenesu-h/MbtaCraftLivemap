package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.ExternalEventType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.ExternalEventDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto
import java.net.URI
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger
import javax.net.ssl.HttpsURLConnection
import kotlin.math.absoluteValue

class EventConsumer(
    private val apiKey: String,
    private val logger: Logger
) {
    private val vehicles = ConcurrentHashMap<String, VehicleDto>()

    fun consume() {
        val url = URI.create("${Constants.API_URL}/vehicles").toURL()
        val connection = (url.openConnection() as HttpsURLConnection).also {
            it.requestMethod = "GET"
            it.setRequestProperty("accept", "text/event-stream")
            it.setRequestProperty("x-api-key", apiKey)
            it.connectTimeout = 5000  // 5 seconds
            it.readTimeout = 0
            it.doInput = true
        }

        logger.info("Opened connection to MBTA API.")

        connection.inputStream.bufferedReader().use { bufferedReader ->
            val reader = EventReader(bufferedReader)

            while (true) {
                try {
                    val event: ExternalEventDto = reader.readEvent() ?: break
                    consumeEvent(event)
                } catch (e: Exception) {
                    logger.severe(e.message ?: "An unknown exception occurred while reading events.")
                    break
                }
            }
        }
    }

    private fun consumeEvent(event: ExternalEventDto) {
        val vehicleDtos: MutableList<VehicleDto> = emptyList<VehicleDto>().toMutableList()
        for (vehicle: VehicleExternalDto in event.vehicles) {
            try {
                vehicleDtos.add(vehicle.toVehicleDto())
            } catch (e: IllegalArgumentException) {
                // Skip the event, our data types likely don't support some of its values
                // We could log this as a warning, but it would be _incredibly_ noisy
                continue
            }
        }

        val numVehicles: Int = vehicles.size
        when (event.type) {
            ExternalEventType.RESET -> {
                vehicles.clear()
                vehicleDtos.forEach { vehicles[it.id] = it }
            }

            ExternalEventType.ADD, ExternalEventType.UPDATE -> {
                vehicleDtos.forEach { vehicles[it.id] = it }
            }

            ExternalEventType.REMOVE -> {
                vehicleDtos.forEach { vehicles.remove(it.id) }
            }
        }

        val newVehicles: Int = vehicles.size - numVehicles
        when {
            newVehicles > 0 -> {
                logger.info("Now tracking $newVehicles new vehicles.")
            }

            newVehicles < 0 -> {
                logger.info("Now tracking ${newVehicles.absoluteValue} less vehicles.")
            }
        }
    }

    fun getVehicles(): List<VehicleDto> {
        return vehicles.values.toList()
    }
}