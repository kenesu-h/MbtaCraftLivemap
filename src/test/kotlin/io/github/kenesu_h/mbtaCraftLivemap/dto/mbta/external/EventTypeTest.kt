package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external

import kotlin.test.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EventTypeTest {
    @ParameterizedTest(name = "fromType ")
    @MethodSource("provideArgsForFromType")
    fun `fromType returns mapped type`(type: String, expected: EventType) {
        val actual = EventType.fromType(type)
        assertEquals(expected, actual)
    }

    @Test
    fun `fromType throws IllegalArgumentException for invalid type`() {
        assertFailsWith<IllegalArgumentException>(
            message = "No valid event type found for test.",
            block = {
                EventType.fromType("test")
            }
        )
    }

    companion object {
        @JvmStatic
        fun provideArgsForFromType(): List<Arguments> {
            return listOf(
                Arguments.of("reset", EventType.RESET),
                Arguments.of("add", EventType.ADD),
                Arguments.of("update", EventType.UPDATE),
                Arguments.of("remove", EventType.REMOVE)
            )
        }
    }
}