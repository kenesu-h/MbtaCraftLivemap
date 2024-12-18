package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.CarriageDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.OccupancyStatus
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CarriageExternalDtoTest {

    @Test
    fun `toCarriageDto returns CarriageDto`() {
        val expected = CarriageDto(
            occupancyStatus = OccupancyStatus.MANY_SEATS_AVAILABLE,
            occupancyPercentage = 10,
            label = "Test"
        )

        val actual: CarriageDto = CarriageExternalDto(
            occupancyStatus = OccupancyStatus.MANY_SEATS_AVAILABLE,
            occupancyPercentage = 10,
            label = "Test"
        ).toCarriageDto()

        assertEquals(expected, actual)
    }
}