package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleStopStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.CarriageExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.EventExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.EventType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipDataExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleAttributesExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.LinksExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleRelationshipsExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.event.EventReader
import java.io.BufferedReader
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals


// This event was modified to only have one vehicle
// This vehicle was originally a bus, but modified to be on the red line for the sake of example
val resetEvent = EventExternalDto(
    type = EventType.RESET,
    vehicles = listOf(
        VehicleExternalDto(
            relationships = VehicleRelationshipsExternalDto(
                trip = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "trip",
                        id = "66960589"
                    ),
                ),
                stop = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "stop",
                        id = "5426"
                    ),
                ),
                route = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "route",
                        id = "Red"
                    ),
                ),
            ),
            links = LinksExternalDto(
                self = "/vehicles/y1454"
            ),
            id = "y1454",
            attributes = VehicleAttributesExternalDto(
                occupancyStatus = OccupancyStatus.MANY_SEATS_AVAILABLE,
                speed = null,
                latitude = 42.42807404,
                bearing = 304,
                longitude = -71.05154898,
                label = "1454",
                revenueStatus = RevenueStatus.REVENUE,
                updatedAt = ZonedDateTime.of(2024, 12, 18, 13, 1, 33, 0, ZoneOffset.ofHours(-5)),
                currentStatus = VehicleStopStatus.IN_TRANSIT_TO,
                currentStopSequence = 29,
                directionId = 0,
                carriages = emptyList()
            )
        )
    )
)

val updateEvent = EventExternalDto(
    type = EventType.UPDATE,
    vehicles = listOf(
        VehicleExternalDto(
            relationships = VehicleRelationshipsExternalDto(
                trip = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "trip",
                        id = "67373481"
                    ),
                ),
                stop = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "stop",
                        id = "70155"
                    ),
                ),
                route = RelationshipExternalDto(
                    links = null,
                    data = RelationshipDataExternalDto(
                        type = "route",
                        id = "Green-E"
                    ),
                ),
            ),
            links = LinksExternalDto(
                self = "/vehicles/G-10073"
            ),
            id = "G-10073",
            attributes = VehicleAttributesExternalDto(
                occupancyStatus = null,
                speed = null,
                latitude = 42.35018,
                bearing = 240,
                longitude = -71.0771,
                label = "3631-3851",
                revenueStatus = RevenueStatus.REVENUE,
                updatedAt = ZonedDateTime.of(2024, 12, 18, 13, 1, 30, 0, ZoneOffset.ofHours(-5)),
                currentStatus = VehicleStopStatus.STOPPED_AT,
                currentStopSequence = 110,
                directionId = 0,
                carriages = listOf(
                    CarriageExternalDto(
                        occupancyStatus = OccupancyStatus.NO_DATA_AVAILABLE,
                        occupancyPercentage = null,
                        label = "3631"
                    ),
                    CarriageExternalDto(
                        occupancyStatus = OccupancyStatus.NO_DATA_AVAILABLE,
                        occupancyPercentage = null,
                        label = "3851"
                    ),
                )
            )
        )
    )
)

class EventReaderTest {

    @Test
    fun `readEvent returns an event`() {
        val data = """
            event: reset
            data: [{"attributes":{"bearing":304,"carriages":[],"current_status":"IN_TRANSIT_TO","current_stop_sequence":29,"direction_id":0,"label":"1454","latitude":42.42807404,"longitude":-71.05154898,"occupancy_status":"MANY_SEATS_AVAILABLE","revenue":"REVENUE","speed":null,"updated_at":"2024-12-18T13:01:33-05:00"},"id":"y1454","links":{"self":"/vehicles/y1454"},"relationships":{"route":{"data":{"id":"Red","type":"route"}},"stop":{"data":{"id":"5426","type":"stop"}},"trip":{"data":{"id":"66960589","type":"trip"}}}, "type": "vehicle"}]
            
        """.trimIndent()

        val reader = EventReader(BufferedReader(data.reader()))

        val actual: EventExternalDto? = reader.readEvent()
        assertEquals(resetEvent, actual)
    }

    @Test
    fun `readEvent returns multiple events in sequence`() {
        val data = """
            event: reset
            data: [{"attributes":{"bearing":304,"carriages":[],"current_status":"IN_TRANSIT_TO","current_stop_sequence":29,"direction_id":0,"label":"1454","latitude":42.42807404,"longitude":-71.05154898,"occupancy_status":"MANY_SEATS_AVAILABLE","revenue":"REVENUE","speed":null,"updated_at":"2024-12-18T13:01:33-05:00"},"id":"y1454","links":{"self":"/vehicles/y1454"},"relationships":{"route":{"data":{"id":"Red","type":"route"}},"stop":{"data":{"id":"5426","type":"stop"}},"trip":{"data":{"id":"66960589","type":"trip"}}}, "type": "vehicle"}]
            
            event: update
            data: {"attributes":{"bearing":240,"carriages":[{"label":"3631","occupancy_percentage":null,"occupancy_status":"NO_DATA_AVAILABLE"},{"label":"3851","occupancy_percentage":null,"occupancy_status":"NO_DATA_AVAILABLE"}],"current_status":"STOPPED_AT","current_stop_sequence":110,"direction_id":0,"label":"3631-3851","latitude":42.35018,"longitude":-71.0771,"occupancy_status":null,"revenue":"REVENUE","speed":null,"updated_at":"2024-12-18T13:01:30-05:00"},"id":"G-10073","links":{"self":"/vehicles/G-10073"},"relationships":{"route":{"data":{"id":"Green-E","type":"route"}},"stop":{"data":{"id":"70155","type":"stop"}},"trip":{"data":{"id":"67373481","type":"trip"}}},"type":"vehicle"}

        """.trimIndent()

        val reader = EventReader(BufferedReader(data.reader()))

        val first: EventExternalDto? = reader.readEvent()
        assertEquals(resetEvent, first)

        val second: EventExternalDto? = reader.readEvent()
        assertEquals(updateEvent, second)
    }

    @Test
    fun `readEvent returns null when there is no data`() {
        val reader = EventReader(BufferedReader("".reader()))

        val actual: EventExternalDto? = reader.readEvent()
        assertEquals(null, actual)
    }
}