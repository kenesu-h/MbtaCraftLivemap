package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.PluginConfigDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.event.EventConsumer
import io.github.kenesu_h.mbtaCraftLivemap.exception.MissingApiKeyException
import io.github.kenesu_h.mbtaCraftLivemap.renderer.CanvasRenderer
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.World
import org.bukkit.Bukkit
import org.bukkit.World.Environment
import java.util.concurrent.Executors

class MbtaCraftLivemap : JavaPlugin() {
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var pluginConfig: PluginConfigDto
    private lateinit var state: CanvasState

    override fun onEnable() {
        saveDefaultConfig()

        try {
            pluginConfig = PluginConfigDto.fromFileConfig(config, logger)
        } catch (e: MissingApiKeyException) {
            logger.severe(e.message)
            server.pluginManager.disablePlugin(this)
            return
        }

        val apiKey: String = pluginConfig.apiKey
        val environment: Environment = pluginConfig.environment
        val originX: Int = pluginConfig.originX
        val originY: Int = pluginConfig.originY
        val originZ: Int = pluginConfig.originZ
        val size: Int = pluginConfig.size
        val direction: CanvasDirection = pluginConfig.direction

        val world: World? = Bukkit.getWorlds().find { it.environment == environment }
        if (world == null) {
            logger.severe("No world with environment $environment found!")
            server.pluginManager.disablePlugin(this)
            return
        }

        val consumer = EventConsumer(apiKey = apiKey, logger = logger)
        executor.submit { consumer.consume() }

        state = CanvasState(size = size, logger = logger)

        val routes: List<RouteDto> = ApiService(apiKey = apiKey).getRoutes()
        state.updateRoutes(routes)

        server.pluginManager.registerEvents(
            CanvasInteractListener(
                originX = originX,
                originY = originY,
                originZ = originZ,
                size = size,
                direction = direction,
                state = state,
            ),
            this
        )

        server.scheduler.runTaskTimer(
            this,
            Runnable {
                state.updateVehicles(consumer.getVehicles())

                CanvasRenderer(
                    world = world,
                    originX = originX,
                    originY = originY,
                    originZ = originZ,
                    size = size,
                    direction = direction,
                    routes = state.getRoutes(),
                    vehicles = state.getVehicles(),
                    logger = logger
                ).render()
            },
            0L,
            20L  // 20 ticks = 1 second
        )
    }

    override fun onDisable() {
        executor.shutdown()
    }
}
