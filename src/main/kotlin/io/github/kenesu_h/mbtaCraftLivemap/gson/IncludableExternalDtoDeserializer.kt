package io.github.kenesu_h.mbtaCraftLivemap.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.route.RouteExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern.RoutePatternExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.shape.ShapeExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.stop.StopExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.trip.TripExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.vehicle.VehicleExternalDto
import java.lang.reflect.Type

class IncludableExternalDtoDeserializer : JsonDeserializer<IncludableExternalDto> {
    override fun deserialize(
        jsonElement: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): IncludableExternalDto {
        val jsonObject = jsonElement.asJsonObject

        val jsonType = jsonObject.get("type").asString
        return context.deserialize(
            jsonElement,
            when (jsonType) {
                "route" -> RouteExternalDto::class.java
                "route_pattern" -> RoutePatternExternalDto::class.java
                "shape" -> ShapeExternalDto::class.java
                "stop" -> StopExternalDto::class.java
                "trip" -> TripExternalDto::class.java
                "vehicle" -> VehicleExternalDto::class.java
                else -> throw JsonParseException("No valid includable DTO type for $jsonType")
            }
        )
    }
}