package io.github.kenesu_h.mbtaCraftLivemap.dto.mbta

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.exception.MissingApiKeyException
import org.bukkit.World.Environment
import org.bukkit.configuration.file.FileConfiguration
import java.util.logging.Logger

data class PluginConfigDto(
    val apiKey: String,
    val environment: Environment,
    val originX: Int,
    val originY: Int,
    val originZ: Int,
    val size: Int,
    val direction: CanvasDirection,
) {

    companion object {
        @JvmStatic
        @Throws(MissingApiKeyException::class)
        fun fromFileConfig(config: FileConfiguration, logger: Logger): PluginConfigDto {
            return PluginConfigDto(
                apiKey = getApiKey(config),
                environment = getEnvironment(config, logger),
                originX = config.getInt("canvas.origin.x"),
                originY = config.getInt("canvas.origin.y"),
                originZ = config.getInt("canvas.origin.z"),
                size = config.getInt("canvas.size"),
                direction = getDirection(config, logger),
            )
        }

        @Throws(MissingApiKeyException::class)
        private fun getApiKey(config: FileConfiguration): String {
            val apiKey: String? = config.getString("api.key")
            if (apiKey.isNullOrEmpty()) {
                throw MissingApiKeyException("No API key provided!")
            }

            return apiKey
        }

        private fun getEnvironment(config: FileConfiguration, logger: Logger): Environment {
            val environment: String? = config.getString("canvas.environment")
            if (environment.isNullOrEmpty()) {
                logger.warning("No environment provided, defaulting to NORMAL.")
                return Environment.NORMAL
            }

            try {
                return Environment.valueOf(environment)
            } catch (e: IllegalArgumentException) {
                logger.warning("Invalid environment ${e.message} provided, defaulting to NORMAL.")
                return Environment.NORMAL
            }
        }

        private fun getDirection(config: FileConfiguration, logger: Logger): CanvasDirection {
            val direction: String? = config.getString("canvas.direction")
            if (direction.isNullOrEmpty()) {
                logger.warning("No direction provided, defaulting to NORTH.")
                return CanvasDirection.NORTH
            }

            try {
                return CanvasDirection.valueOf(direction)
            } catch (e: IllegalArgumentException) {
                logger.warning("Invalid direction ${e.message} provided, defaulting to NORTH.")
                return CanvasDirection.NORTH
            }
        }
    }
}