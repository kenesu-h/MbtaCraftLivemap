package io.github.kenesu_h.mbtaCraftLivemap.component

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor

abstract class BaseComponent {
    abstract fun toTextComponent(): TextComponent

    protected fun getRouteTextColor(route: Route): NamedTextColor {
        return when (route) {
            Route.RED, Route.MATTAPAN -> NamedTextColor.RED
            Route.ORANGE -> NamedTextColor.GOLD
            Route.GREEN_B, Route.GREEN_C, Route.GREEN_D, Route.GREEN_E -> NamedTextColor.GREEN
            Route.BLUE -> NamedTextColor.BLUE
        }
    }

    protected fun getTransportationTypeText(type: TransportationType): String {
        return when (type) {
            TransportationType.LIGHT_RAIL -> "Light Rail"
            TransportationType.HEAVY_RAIL -> "Heavy Rail"
            TransportationType.COMMUTER_RAIL -> "Commuter Rail"
            TransportationType.BUS -> "Bus"
            TransportationType.FERRY -> "Ferry"
        }
    }
}