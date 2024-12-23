package io.github.kenesu_h.mbtaCraftLivemap.component

import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.TransportationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.WheelchairAccessibility
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.LocationType
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.stop.StopDto
import io.github.kenesu_h.mbtaCraftLivemap.extension.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration

class StopComponent(val stop: StopDto) : BaseComponent() {
    override fun toTextComponent(): TextComponent {
        val component = Component.text()

        val name: String = stop.name
        component.decoratedText("Stop: ", TextDecoration.BOLD)
            .text(name)
            .newline()

        val locationType: LocationType = stop.locationType
        component.decoratedSubtext("Location Type: ", TextDecoration.BOLD)
            .subtext(getLocationTypeText(locationType))
            .newline()

        val address: String? = stop.address
        if (address != null) {
            component.decoratedSubtext("Address: ", TextDecoration.BOLD)
                .subtext(address)
                .newline()
        }

        val municipality: String? = stop.municipality
        if (municipality != null) {
            component.decoratedSubtext("Municipality: ", TextDecoration.BOLD)
                .subtext(municipality)
                .newline()
        }

        val coordinates: Pair<Double, Double> = stop.coordinates
        component.decoratedSubtext("Coordinates: ", TextDecoration.BOLD)
            .subtext("${coordinates.first}, ${coordinates.second}")
            .newline()

        val vehicleType: TransportationType? = stop.vehicleType
        if (vehicleType != null) {
            component.decoratedSubtext("Vehicle Type: ", TextDecoration.BOLD)
                .subtext(getTransportationTypeText(vehicleType))
                .newline()
        }

        val wheelchairBoarding: WheelchairAccessibility = stop.wheelchairBoarding
        component.decoratedSubtext("Wheelchair Boarding: ", TextDecoration.BOLD)
            .subtext(getWheelchairAccessibilityText(wheelchairBoarding))
            .newline()

        return component.build()
    }

    private fun getLocationTypeText(locationType: LocationType): String {
        return when (locationType) {
            LocationType.STOP -> "Stop"
            LocationType.STATION -> "Station"
            LocationType.STATION_ENTRANCE_OR_EXIT -> "Station Entrance/Exit"
            LocationType.GENERIC_NODE -> "Generic Node"
        }
    }

    private fun getWheelchairAccessibilityText(wheelchairAccessibility: WheelchairAccessibility): String {
        return when (wheelchairAccessibility) {
            WheelchairAccessibility.NO_INFORMATION -> "No information"
            WheelchairAccessibility.ACCESSIBLE -> "Accessible"
            WheelchairAccessibility.INACCESSIBLE -> "Inaccessible"
        }
    }
}