package io.github.kenesu_h.mbtaCraftLivemap.component

import io.github.kenesu_h.mbtaCraftLivemap.*
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.CarriageDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.RevenueStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.OccupancyStatus
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.vehicle.VehicleStopStatus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import java.time.ZonedDateTime

class VehicleComponent(val vehicle: VehicleDto) : BaseComponent() {
    override fun toTextComponent(): TextComponent {
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

        val coordinates: Pair<Double, Double>? = vehicle.coordinates
        val bearing: Int? = vehicle.bearing
        if (coordinates != null && bearing != null) {
            component.decoratedSubtext("Coordinates: ", TextDecoration.BOLD)
                .subtext("${coordinates.first}, ${coordinates.second} ")
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
}