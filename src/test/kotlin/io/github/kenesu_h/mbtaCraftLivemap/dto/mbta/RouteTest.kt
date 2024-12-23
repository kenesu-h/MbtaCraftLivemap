package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RouteTest {
    @ParameterizedTest(name = "fromType ")
    @MethodSource("provideArgsForFromId")
    fun `fromId returns mapped route`(id: String, expected: Route) {
        val actual = Route.fromId(id)
        assertEquals(expected, actual)
    }

    @Test
    fun `fromId throws IllegalArgumentException for invalid route`() {
        assertFailsWith<IllegalArgumentException>(
            message = "No valid event route found for Test.",
            block = {
                Route.fromId("Test")
            }
        )
    }

    companion object {
        @JvmStatic
        fun provideArgsForFromId(): List<Arguments> {
            return listOf(
                Arguments.of("Red", Route.RED),
                Arguments.of("Mattapan", Route.MATTAPAN),
                Arguments.of("Orange", Route.ORANGE),
                Arguments.of("Green-B", Route.GREEN_B),
                Arguments.of("Green-C", Route.GREEN_C),
                Arguments.of("Green-D", Route.GREEN_D),
                Arguments.of("Green-E", Route.GREEN_E),
                Arguments.of("CR-Fairmount", Route.COMMUTER_RAIL_FAIRMOUNT)
            )
        }
    }
}