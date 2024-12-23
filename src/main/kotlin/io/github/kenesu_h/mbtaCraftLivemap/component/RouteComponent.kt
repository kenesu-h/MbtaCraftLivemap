package io.github.kenesu_h.mbtaCraftLivemap.component

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.extension.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration

class RouteComponent(val route: RouteDto) : BaseComponent() {
    override fun toTextComponent(): TextComponent {
        val component = Component.text()

        val longName: String = route.longName
        component.decoratedText("Route: ", TextDecoration.BOLD)
            .coloredText(longName, getRouteTextColor(route.id))
            .newline()

        val description: String = route.description
        component.decoratedSubtext("Description: ", TextDecoration.BOLD)
            .subtext(description)
            .newline()

        val type: TransportationType = route.type
        component.decoratedSubtext("Type: ", TextDecoration.BOLD)
            .subtext(getTransportationTypeText(type))
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
}