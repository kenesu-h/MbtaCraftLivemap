package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.PluginConfigDto
import io.github.kenesu_h.mbtaCraftLivemap.exception.MissingApiKeyException
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.World.Environment
import java.net.URI
import java.util.concurrent.Executors

class MbtaCraftLivemap : JavaPlugin() {
    private val executor = Executors.newSingleThreadExecutor()

    override fun onEnable() {
        saveDefaultConfig()

        val pluginConfig: PluginConfigDto
        try {
            pluginConfig = PluginConfigDto.fromFileConfig(config, logger)
        } catch (e: MissingApiKeyException) {
            logger.severe(e.message)
            server.pluginManager.disablePlugin(this)
            return
        }

        val environment: Environment = pluginConfig.environment
        val world: World? = Bukkit.getWorlds().find { it.environment == environment }
        if (world == null) {
            logger.severe("No world with environment $environment found!")
            server.pluginManager.disablePlugin(this)
            return
        }

        val apiUrl = URI.create("${Constants.API_URL}/vehicles").toURL()
        val consumer = EventConsumer(pluginConfig.apiKey, apiUrl, logger)
        executor.submit { consumer.consume() }

        server.scheduler.runTaskTimer(this, Runnable {
            val vehicles = consumer.getVehicles()
        }, 0L, 20L)  // 20 ticks = 1 second
    }

    override fun onDisable() {
        executor.shutdown()
    }
}
