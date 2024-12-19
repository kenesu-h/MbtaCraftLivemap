package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.*
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

        val builder = StringBuilder()
        vehicles.forEach { vehicle ->
            builder.appendLine("§r")
            builder.appendLine(formatVehicle(vehicle))
        }

        builder.appendLine("§r")
        if (vehicles.size == 1) {
            builder.appendLine("There is ${vehicles.size} total vehicle at this location.")
        } else {
            builder.appendLine("There are ${vehicles.size} total vehicles at this location.")
        }

        player.sendMessage(builder.toString())
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

    // TODO: Figure out a way to abstract out the formatting
    private fun formatVehicle(vehicle: CanvasVehicleDto): String {
        val builder = StringBuilder()

        val id: String = vehicle.id
        builder.appendLine("§LVehicle ID:§r $id§7")

        val route: Route? = vehicle.route
        val direction: Int? = vehicle.directionId
        if (route != null && direction != null) {
            builder.appendLine("§LRoute:§r§7 ${formatRoute(route)}§7 (§LDirection:§r§7 $direction)")
        }

        val currentStatus: VehicleStopStatus? = vehicle.currentStatus
        if (currentStatus != null) {
            builder.appendLine("§LStatus:§r§7 ${formatCurrentStatus(currentStatus)}")
        }

        val revenueStatus: RevenueStatus? = vehicle.revenueStatus
        if (revenueStatus != null) {
            builder.appendLine("§LAccepting customers:§r§7 ${formatRevenueStatus(revenueStatus)}")
        }

        val occupancyStatus: OccupancyStatus? = vehicle.occupancyStatus
        if (occupancyStatus != null) {
            builder.appendLine("§LOccupancy:§r§7 ${formatOccupancyStatus(occupancyStatus)}")
        }

        val speed: Double? = vehicle.speed
        if (speed != null) {
            builder.appendLine("§LSpeed:§r§7 $speed m/s")
        }

        val latitude: Double? = vehicle.latitude
        val longitude: Double? = vehicle.longitude
        val bearing: Int? = vehicle.bearing
        if (latitude != null && longitude != null && bearing != null) {
            builder.appendLine("§LCoordinates:§r§7 $latitude, $longitude (§LBearing:§r§7 $bearing°)")
        }

        val carriages: List<CarriageDto> = vehicle.carriages
        if (carriages.isNotEmpty()) {
            builder.appendLine("§LCarriages:§r§7")
            carriages.forEachIndexed { i, carriage ->
                builder.appendLine("  - §LCarriage ${i + 1}:§r§7")
                builder.appendLine("    - §LOccupancy:§r§7 ${formatOccupancyStatus(carriage.occupancyStatus)}")
            }
        }

        val updatedAt: ZonedDateTime? = vehicle.updatedAt
        if (updatedAt != null) {
            builder.appendLine("§LUpdated at:§r§7 $updatedAt")
        }

        return builder.toString()
    }

    private fun formatRoute(route: Route): String {
        val prefix: String = when (route) {
            Route.RED, Route.MATTAPAN -> "§C"
            Route.ORANGE -> "§6"
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> "§A"
            Route.BLUE -> "§9"
        }

        return "$prefix${route.id}§r"
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
}