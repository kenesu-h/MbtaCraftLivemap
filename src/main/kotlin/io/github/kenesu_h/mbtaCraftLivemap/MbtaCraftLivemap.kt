package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.PluginConfigDto
import io.github.kenesu_h.mbtaCraftLivemap.exception.MissingApiKeyException
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.World.Environment
import java.util.concurrent.Executors

class MbtaCraftLivemap : JavaPlugin() {
    private lateinit var pluginConfig: PluginConfigDto
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var state: CanvasState
    private lateinit var renderer: CanvasRenderer

    override fun onEnable() {
        saveDefaultConfig()

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

        val consumer = EventConsumer(apiKey = pluginConfig.apiKey, logger = logger)
        executor.submit { consumer.consume() }

        state = CanvasState(size = pluginConfig.size, logger = logger)

        server.pluginManager.registerEvents(
            CanvasInteractListener(
                originX = pluginConfig.originX,
                originY = pluginConfig.originY,
                originZ = pluginConfig.originZ,
                size = pluginConfig.size,
                direction = pluginConfig.direction,
                state = state,
            ),
            this
        )

        renderer = CanvasRenderer(
            world = world,
            originX = pluginConfig.originX,
            originY = pluginConfig.originY,
            originZ = pluginConfig.originZ,
            size = pluginConfig.size,
            direction = pluginConfig.direction,
            logger = logger
        )

        server.scheduler.runTaskTimer(
            this,
            Runnable {
                state.updateVehicles(consumer.getVehicles())
                renderer.render(state.vehicles)
            },
            0L,
            20L  // 20 ticks = 1 second
        )
    }

    override fun onDisable() {
        executor.shutdown()
    }
}
