package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasRouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteType
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
        components.addAll(vehicles.map { formatVehicleComponent(it) })

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

    private fun formatVehicleComponent(vehicle: CanvasVehicleDto): Component {
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
                .subtext(getCurrentStatusText(currentStatus))
                .newline()
        }

        val revenueStatus: RevenueStatus? = vehicle.revenueStatus
        if (revenueStatus != null) {
            component.decoratedSubtext("Accepting customers: ", TextDecoration.BOLD)
                .subtext(getRevenueStatusText(revenueStatus))
                .newline()
        }

        val occupancyStatus: OccupancyStatus? = vehicle.occupancyStatus
        if (occupancyStatus != null) {
            component.decoratedSubtext("Occupancy: ", TextDecoration.BOLD)
                .coloredText(
                    getOccupancyStatusText(occupancyStatus),
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

        val geographicCoordinates: Pair<Double, Double>? = vehicle.geographicCoordinates
        val bearing: Int? = vehicle.bearing
        if (geographicCoordinates != null && bearing != null) {
            component.decoratedSubtext("Coordinates: ", TextDecoration.BOLD)
                .subtext("${geographicCoordinates.first}, ${geographicCoordinates.second} ")
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
                        getOccupancyStatusText(carriage.occupancyStatus),
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

    private fun getCurrentStatusText(currentStatus: VehicleStopStatus): String {
        return when (currentStatus) {
            VehicleStopStatus.INCOMING_AT -> "Arriving"
            VehicleStopStatus.STOPPED_AT -> "Standing"
            VehicleStopStatus.IN_TRANSIT_TO -> "In-transit"
        }
    }

    private fun getRevenueStatusText(revenueStatus: RevenueStatus): String {
        return when (revenueStatus) {
            RevenueStatus.REVENUE -> "Yes"
            RevenueStatus.NON_REVENUE -> "No"
        }
    }

    private fun getOccupancyStatusText(occupancyStatus: OccupancyStatus): String {
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

    private fun handleRoutesInteract(player: Player, routes: List<CanvasRouteDto>) {
        val components: MutableList<Component> = mutableListOf(Component.newline())
        components.addAll(routes.map { formatRouteComponent(it) })

        val summary = if (routes.size == 1) {
            "There is ${routes.size} total route at this location."
        } else {
            "There are ${routes.size} total routes at this location."
        }
        components.add(Component.text(summary))

        components.forEach { component ->
            player.sendMessage(component)
        }
    }

    private fun formatRouteComponent(route: CanvasRouteDto): Component {
        val component = Component.text()

        val longName: String = route.longName
        component.decoratedText("Route: ", TextDecoration.BOLD)
            .coloredText(longName, getRouteTextColor(route.id))
            .newline()

        val description: String = route.description
        component.decoratedSubtext("Description: ", TextDecoration.BOLD)
            .subtext(description)
            .newline()

        val type: RouteType = route.type
        component.decoratedSubtext("Type: ", TextDecoration.BOLD)
            .subtext(getRouteTypeText(type))
            .newline()

        val directionDestinations: List<String>? = route.directionDestinations
        val directionNames: List<String>? = route.directionNames
        if (directionDestinations != null && directionNames != null) {
            component.decoratedSubtext("Destinations:", TextDecoration.BOLD)
                .newline()
            for (i in directionDestinations.indices) {
                component.decoratedSubtext("  - ${directionNames[i]}: ", TextDecoration.BOLD)
                    .subtext(directionDestinations[i])
                    .newline()
            }
        }

        return component.build()
    }

    private fun getRouteTypeText(type: RouteType): String {
        return when (type) {
            RouteType.LIGHT_RAIL -> "Light Rail"
            RouteType.HEAVY_RAIL -> "Heavy Rail"
            RouteType.COMMUTER_RAIL -> "Commuter Rail"
            RouteType.BUS -> "Bus"
            RouteType.FERRY -> "Ferry"
        }
    }
}