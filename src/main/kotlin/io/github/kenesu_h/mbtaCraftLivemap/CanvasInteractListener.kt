package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
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
    private var state: CanvasState,
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

        val canvasX: Int = when (direction) {
            CanvasDirection.NORTH -> originZ - block.z
            CanvasDirection.SOUTH -> block.z - originZ
            CanvasDirection.EAST -> block.x - originX
            CanvasDirection.WEST -> originX - block.x
        }
        val canvasY: Int = block.y - originY

        val vehicles: List<CanvasVehicleDto> = state.getVehiclesAtPoint(canvasX, canvasY)
        if (vehicles.isEmpty()) {
            return
        }

        val components: MutableList<Component> = mutableListOf(Component.newline())
        components.addAll(vehicles.map { formatVehicleAsComponent(it) })

        val summary = if (vehicles.size == 1) {
            "There is ${vehicles.size} total vehicle at this location."
        } else {
            "There are ${vehicles.size} total vehicles at this location."
        }
        components.add(Component.text(summary))

        components.forEach { component ->
            player.sendMessage(component)
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

    private fun formatVehicleAsComponent(vehicle: CanvasVehicleDto): Component {
        val component = Component.text()

        val id: String = vehicle.id
        component.decoratedText("Vehicle ID: ", TextDecoration.BOLD)
            .text(id)
            .newline()

        val route: Route? = vehicle.route
        val direction: Int? = vehicle.directionId
        if (route != null && direction != null) {
            component.decoratedSubtext("Route: ", TextDecoration.BOLD)
                .coloredText("${route.id} ", getRouteTextColor(route))
                .subtext("(")
                .decoratedSubtext("Direction: ", TextDecoration.BOLD)
                .subtext(direction.toString())
                .subtext(")")
                .newline()
        }

        val currentStatus: VehicleStopStatus? = vehicle.currentStatus
        if (currentStatus != null) {
            component.decoratedSubtext("Status: ", TextDecoration.BOLD)
                .subtext(formatCurrentStatus(currentStatus))
                .newline()
        }

        val revenueStatus: RevenueStatus? = vehicle.revenueStatus
        if (revenueStatus != null) {
            component.decoratedSubtext("Accepting customers: ", TextDecoration.BOLD)
                .subtext(formatRevenueStatus(revenueStatus))
                .newline()
        }

        val occupancyStatus: OccupancyStatus? = vehicle.occupancyStatus
        if (occupancyStatus != null) {
            component.decoratedSubtext("Occupancy: ", TextDecoration.BOLD)
                .coloredText(
                    formatOccupancyStatus(occupancyStatus),
                    getOccupancyStatusTextColor(occupancyStatus)
                )
                .newline()
        }

        val speed: Double? = vehicle.speed
        if (speed != null) {
            component.decoratedSubtext("Speed: ", TextDecoration.BOLD)
                .subtext("$speed m/s")
                .newline()
        }

        val latitude: Double? = vehicle.latitude
        val longitude: Double? = vehicle.longitude
        val bearing: Int? = vehicle.bearing
        if (latitude != null && longitude != null && bearing != null) {
            component.decoratedSubtext("Coordinates: ", TextDecoration.BOLD)
                .subtext("$latitude, $longitude ")
                .subtext("(")
                .decoratedSubtext("Bearing: ", TextDecoration.BOLD)
                .subtext("$bearing°")
                .subtext(")")
                .newline()
        }

        val carriages: List<CarriageDto> = vehicle.carriages
        if (carriages.isNotEmpty()) {
            component.decoratedSubtext("Carriages:", TextDecoration.BOLD)
                .newline()
            carriages.forEachIndexed { i, carriage ->
                component.decoratedSubtext("  - Carriage ${i + 1}:", TextDecoration.BOLD)
                    .newline()
                    .decoratedSubtext("    - Occupancy: ", TextDecoration.BOLD)
                    .coloredText(
                        formatOccupancyStatus(carriage.occupancyStatus),
                        getOccupancyStatusTextColor(carriage.occupancyStatus)
                    )
                    .newline()
            }
        }

        val updatedAt: ZonedDateTime? = vehicle.updatedAt
        if (updatedAt != null) {
            component.decoratedSubtext("Updated at: ", TextDecoration.BOLD)
                .subtext(updatedAt.toString())
                .newline()
        }

        return component.build()
    }

    private fun getRouteTextColor(route: Route): NamedTextColor {
        return when (route) {
            Route.RED, Route.MATTAPAN -> NamedTextColor.RED
            Route.ORANGE -> NamedTextColor.GOLD
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> NamedTextColor.GREEN
            Route.BLUE -> NamedTextColor.BLUE
        }
    }

    private fun formatCurrentStatus(currentStatus: VehicleStopStatus): String {
        return when (currentStatus) {
            VehicleStopStatus.INCOMING_AT -> "Arriving"
            VehicleStopStatus.STOPPED_AT -> "Standing"
            VehicleStopStatus.IN_TRANSIT_TO -> "In-transit"
        }
    }

    private fun formatRevenueStatus(revenueStatus: RevenueStatus): String {
        return when (revenueStatus) {
            RevenueStatus.REVENUE -> "Yes"
            RevenueStatus.NON_REVENUE -> "No"
        }
    }

    private fun formatOccupancyStatus(occupancyStatus: OccupancyStatus): String {
        return when (occupancyStatus) {
            OccupancyStatus.MANY_SEATS_AVAILABLE -> "Many seats available"
            OccupancyStatus.FEW_SEATS_AVAILABLE -> "Few seats available"
            OccupancyStatus.STANDING_ROOM_ONLY -> "Standing room only"
            OccupancyStatus.CRUSHED_STANDING_ROOM_ONLY -> "Crushed standing room only"
            OccupancyStatus.FULL -> "Full"
            OccupancyStatus.NOT_ACCEPTING_PASSENGERS -> "Not accepting passengers"
            OccupancyStatus.NO_DATA_AVAILABLE -> "No data available"
        }
    }

    private fun getOccupancyStatusTextColor(occupancyStatus: OccupancyStatus): NamedTextColor {
        return when (occupancyStatus) {
            OccupancyStatus.MANY_SEATS_AVAILABLE -> NamedTextColor.GREEN
            OccupancyStatus.FEW_SEATS_AVAILABLE -> NamedTextColor.YELLOW
            OccupancyStatus.STANDING_ROOM_ONLY -> NamedTextColor.GOLD
            OccupancyStatus.CRUSHED_STANDING_ROOM_ONLY -> NamedTextColor.RED
            OccupancyStatus.FULL -> NamedTextColor.DARK_RED
            OccupancyStatus.NOT_ACCEPTING_PASSENGERS -> NamedTextColor.GRAY
            OccupancyStatus.NO_DATA_AVAILABLE -> NamedTextColor.GRAY
        }
    }
}