package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.component.RouteComponent
import io.github.kenesu_h.mbtaCraftLivemap.component.StopComponent
import io.github.kenesu_h.mbtaCraftLivemap.component.VehicleComponent
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasRouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasStopDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleStopStatus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.time.ZonedDateTime

class CanvasInteractListener(
    private val originX: Int,
    private val originY: Int,
    private val originZ: Int,
    private val size: Int,
    private val direction: CanvasDirection,
    private val state: CanvasState,
) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) {
            return
        }

        val player: Player = event.player
        val block: Block? = player.getTargetBlockExact(1024)
        if (block == null || !isBlockInCanvas(block)) {
            return
        }

        val canvasPoint: Pair<Int, Int> = Pair(
            when (direction) {
                CanvasDirection.NORTH -> originZ - block.z
                CanvasDirection.SOUTH -> block.z - originZ
                CanvasDirection.EAST -> block.x - originX
                CanvasDirection.WEST -> originX - block.x
            },
            block.y - originY
        )

        val vehicles: List<CanvasVehicleDto> = state.getVehiclesAtPoint(canvasPoint)
        if (vehicles.isNotEmpty()) {
            handleVehiclesInteract(player, vehicles)
            return
        }

        val stops: List<CanvasStopDto> = state.getStopsAtPoint(canvasPoint)
        if (stops.isNotEmpty()) {
            handleStopsInteract(player, stops)
            return
        }

        val routes: List<CanvasRouteDto> = state.getRoutesAtPoint(canvasPoint)
        if (routes.isNotEmpty()) {
            handleRoutesInteract(player, routes)
            return
        }
    }

    private fun isBlockInCanvas(block: Block): Boolean {
        return when (direction) {
            CanvasDirection.NORTH -> {
                block.x == originX &&
                        block.y in originY until (originY + size) &&
                        block.z in (originZ - size + 1) until originZ + 1
            }

            CanvasDirection.SOUTH -> {
                block.x == originX &&
                        block.y in originY until (originY + size) &&
                        block.z in originZ until (originZ + size)
            }

            CanvasDirection.EAST -> {
                block.x in originX until (originX + size) &&
                        block.y in originY until (originY + size) &&
                        block.z == originZ
            }

            CanvasDirection.WEST -> {
                block.x in (originX - size + 1) until originX + 1 &&
                        block.y in originY until (originY + size) &&
                        block.z == originZ
            }
        }
    }

    private fun handleVehiclesInteract(player: Player, vehicles: List<CanvasVehicleDto>) {
        val components: MutableList<Component> = mutableListOf(Component.newline())
        components.addAll(vehicles.map { VehicleComponent(vehicle = it.inner).toTextComponent() })

        val summary = if (vehicles.size == 1) {
            "There is ${vehicles.size} total vehicle at this location."
        } else {
            "There are ${vehicles.size} total vehicles at this location."
        }
        components.add(Component.text(summary))

        components.forEach { player.sendMessage(it) }
    }

    private fun handleStopsInteract(player: Player, stops: List<CanvasStopDto>) {
        val components: MutableList<Component> = mutableListOf(Component.newline())
        components.addAll(stops.map { StopComponent(stop = it.inner).toTextComponent() })

        val summary = if (stops.size == 1) {
            "There is ${stops.size} total stop at this location."
        } else {
            "There are ${stops.size} total stops at this location."
        }
        components.add(Component.text(summary))

        components.forEach { player.sendMessage(it) }
    }

    private fun handleRoutesInteract(player: Player, routes: List<CanvasRouteDto>) {
        val components: MutableList<Component> = mutableListOf(Component.newline())
        components.addAll(routes.map { RouteComponent(route = it.inner).toTextComponent() })

        val summary = if (routes.size == 1) {
            "There is ${routes.size} total route at this location."
        } else {
            "There are ${routes.size} total routes at this location."
        }
        components.add(Component.text(summary))

        components.forEach { player.sendMessage(it) }
    }
}