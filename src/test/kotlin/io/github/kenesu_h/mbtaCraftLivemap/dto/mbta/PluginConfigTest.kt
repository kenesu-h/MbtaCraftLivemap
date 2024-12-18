package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.exception.MissingApiKeyException
import io.mockk.every
import io.mockk.mockk
import org.bukkit.World.Environment
import org.bukkit.configuration.file.FileConfiguration
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

val fileConfig = mockk<FileConfiguration>().also {
    every { it.getString("api.key") } returns "test"
    every { it.getString("canvas.environment") } returns "NORMAL"
    every { it.getInt("canvas.origin.x") } returns 0
    every { it.getInt("canvas.origin.y") } returns 0
    every { it.getInt("canvas.origin.z") } returns 0
    every { it.getInt("canvas.size") } returns 0
    every { it.getString("canvas.direction") } returns "NORTH"
}

val logger = mockk<Logger>(relaxed = true)

val pluginConfig = PluginConfigDto(
    apiKey = "test",
    environment = Environment.NORMAL,
    originX = 0,
    originY = 0,
    originZ = 0,
    size = 0,
    direction = CanvasDirection.NORTH
)

class PluginConfigTest {
    @Test
    fun `fromFileConfig returns PluginConfigDto`() {
        val actual = PluginConfigDto.fromFileConfig(fileConfig, logger)
        assertEquals(pluginConfig, actual)
    }

    @Test
    fun `fromFileConfig throws MissingApiKeyException when there is no API key`() {
        val configWithoutApiKey = mockk<FileConfiguration>().also {
            every { it.getString("api.key") } returns ""
        }

        assertFailsWith<MissingApiKeyException>(
            message = "No API key provided!",
            block = {
                PluginConfigDto.fromFileConfig(configWithoutApiKey, logger)
            }
        )
    }

    @ParameterizedTest(name = "fromFileConfigDefaultEnvironment")
    @MethodSource("provideArgsForFromFileConfigDefault")
    fun `fromFileConfig defaults to NORMAL when environment does not exist or is invalid`(
        environment: String?
    ) {
        val fileConfigWithoutEnvironment = mockk<FileConfiguration>().also {
            every { it.getString("api.key") } returns "test"
            every { it.getString("canvas.environment") } returns environment
            every { it.getInt("canvas.origin.x") } returns 0
            every { it.getInt("canvas.origin.y") } returns 0
            every { it.getInt("canvas.origin.z") } returns 0
            every { it.getInt("canvas.size") } returns 0
            every { it.getString("canvas.direction") } returns "NORTH"
        }

        val actual = PluginConfigDto.fromFileConfig(fileConfigWithoutEnvironment, logger)
        assertEquals(pluginConfig, actual)
    }

    @ParameterizedTest(name = "fromFileConfigDefaultDirection")
    @MethodSource("provideArgsForFromFileConfigDefault")
    fun `fromFileConfig defaults to NORTH when direction does not exist or is invalid`(
        direction: String?
    ) {
        val fileConfigWithoutDirection = mockk<FileConfiguration>().also {
            every { it.getString("api.key") } returns "test"
            every { it.getString("canvas.environment") } returns "NORMAL"
            every { it.getInt("canvas.origin.x") } returns 0
            every { it.getInt("canvas.origin.y") } returns 0
            every { it.getInt("canvas.origin.z") } returns 0
            every { it.getInt("canvas.size") } returns 0
            every { it.getString("canvas.direction") } returns direction
        }

        val actual = PluginConfigDto.fromFileConfig(fileConfigWithoutDirection, logger)
        assertEquals(pluginConfig, actual)
    }

    companion object {
        @JvmStatic
        fun provideArgsForFromFileConfigDefault(): List<Arguments> {
            return listOf(
                Arguments.of(null),
                Arguments.of("TEST")
            )
        }
    }
}