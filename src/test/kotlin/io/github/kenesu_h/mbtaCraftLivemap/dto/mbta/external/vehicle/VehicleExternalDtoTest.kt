package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipDataExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.relationship.RelationshipLinksExternalDto

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals


val vehicle = VehicleExternalDto(
    relationships = VehicleRelationshipsExternalDto(
        trip = RelationshipExternalDto(
            links = RelationshipLinksExternalDto(
                self = "",
                related = ""
            ),
            data = RelationshipDataExternalDto(
                type = "",
                id = ""
            ),
        ),
        stop = RelationshipExternalDto(
            links = RelationshipLinksExternalDto(
                self = "",
                related = ""
            ),
            data = RelationshipDataExternalDto(
                type = "",
                id = ""
            ),
        ),
        route = RelationshipExternalDto(
            links = RelationshipLinksExternalDto(
                self = "",
                related = ""
            ),
            data = RelationshipDataExternalDto(
                type = "",
                id = "Red"
            ),
        ),
    ),
    links = VehicleLinksExternalDto(
        self = ""
    ),
    id = "",
    attributes = VehicleAttributesExternalDto(
        occupancyStatus = OccupancyStatus.MANY_SEATS_AVAILABLE,
        speed = 0.0,
        latitude = 0.0,
        bearing = 0,
        longitude = 0.0,
        label = "",
        revenueStatus = RevenueStatus.REVENUE,
        updatedAt = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        currentStatus = VehicleStopStatus.STOPPED_AT,
        currentStopSequence = 0,
        directionId = 0,
        carriages = emptyList()
    ),
)
val vehicleDto = VehicleDto(
    id = "",
    occupancyStatus = OccupancyStatus.MANY_SEATS_AVAILABLE,
    speed = 0.0,
    latitude = 0.0,
    bearing = 0,
    longitude = 0.0,
    label = "",
    revenueStatus = RevenueStatus.REVENUE,
    updatedAt = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
    currentStatus = VehicleStopStatus.STOPPED_AT,
    currentStopSequence = 0,
    directionId = 0,
    carriages = emptyList(),
    route = Route.RED
)

val vehicleAttributesWithoutCarriages = vehicle.attributes!!.copy(carriages = null)
val vehicleWithoutCarriages = vehicle.copy(attributes = vehicleAttributesWithoutCarriages)
val vehicleDtoWithoutCarriages = vehicleDto.copy(carriages = emptyList())

val vehicleWithoutRelationships = vehicle.copy(relationships = null)
val vehicleDtoWithoutRelationships = vehicleDto.copy(route = null)


class VehicleExternalDtoTest {

    @ParameterizedTest(name = "toVehicleDto")
    @MethodSource("provideArgsForToVehicleDto")
    fun `toVehicleDto returns VehicleDto`(vehicle: VehicleExternalDto, expected: VehicleDto) {
        val actual = vehicle.toVehicleDto()
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun provideArgsForToVehicleDto(): List<Arguments> {
            return listOf(
                Arguments.of(vehicle, vehicleDto),
                Arguments.of(vehicleWithoutCarriages, vehicleDtoWithoutCarriages),
                Arguments.of(vehicleWithoutRelationships, vehicleDtoWithoutRelationships)
            )
        }
    }
}