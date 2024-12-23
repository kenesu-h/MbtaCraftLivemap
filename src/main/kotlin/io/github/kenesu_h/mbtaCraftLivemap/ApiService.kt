package io.github.kenesu_h.mbtaCraftLivemap

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.Constants
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.IncludableExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.response.ExtendedResponseExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.route.RouteExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern.RoutePatternExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.routePattern.RoutePatternRelationshipsExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.shape.ShapeExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.stop.StopExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.external.trip.TripExternalDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.Route
import io.github.kenesu_h.mbtaCraftLivemap.dto.mbta.route.RouteDto
import io.github.kenesu_h.mbtaCraftLivemap.gson.IncludableExternalDtoDeserializer
import io.github.kenesu_h.mbtaCraftLivemap.gson.ZonedDateTimeAdapter
import java.net.URI
import java.net.URL
import java.time.ZonedDateTime
import java.util.*
import javax.net.ssl.HttpsURLConnection

class ApiService(val apiKey: String) {
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
            .registerTypeAdapter(IncludableExternalDto::class.java, IncludableExternalDtoDeserializer())
            .create()

    fun getRoutes(): List<RouteDto> {
        val routeResponse = queryRoutes()

        val routes: List<RouteExternalDto> = routeResponse.data
            .fold(mutableListOf()) { acc: MutableList<RouteExternalDto>,
                                     next: RouteExternalDto ->
                acc.add(next)
                acc
            }

        val routePatterns: Map<String, RoutePatternExternalDto> = routeResponse.included
            .fold(hashMapOf()) { acc: HashMap<String, RoutePatternExternalDto>,
                                 next: IncludableExternalDto ->
                next as RoutePatternExternalDto  // Route patterns are the only thing we included
                acc[next.id] = next
                acc
            }

        val routesToTripIds: Map<Route, Set<String>> = routePatterns.values
            .fold(EnumMap(Route::class.java)) { acc: EnumMap<Route, MutableSet<String>>,
                                                next: RoutePatternExternalDto ->
                val relationships: RoutePatternRelationshipsExternalDto = next.relationships

                // Route relationships populate `data`
                val route = Route.fromId(relationships.route.data!!.id)
                if (!acc.containsKey(route)) {
                    acc[route] = mutableSetOf()
                }

                // Representative trip relationships populate `data`
                acc[route]!!.add(relationships.representativeTrip.data!!.id)
                acc
            }

        val tripIds: List<String> = routesToTripIds.values
            .fold(mutableListOf()) { acc, next ->
                acc.addAll(next)
                acc
            }

        val tripResponse = queryTrips(tripIds)

        val trips: Map<String, TripExternalDto> = tripResponse.data
            .fold(hashMapOf()) { acc: HashMap<String, TripExternalDto>,
                                 next: TripExternalDto ->
                acc[next.id] = next
                acc
            }

        val shapes: HashMap<String, ShapeExternalDto> = hashMapOf()
        val stops: HashMap<String, StopExternalDto> = hashMapOf()
        tripResponse.included.forEach {
            when (it) {
                is ShapeExternalDto -> shapes[it.id] = it
                is StopExternalDto -> stops[it.id] = it
            }
        }

        val routeDtos: List<RouteDto> = routes.map { route ->
            val routeTripIds: Set<String> = routesToTripIds[Route.fromId(route.id)]!!.toSet()
            route.toRouteDto(
                routeTripIds.map { tripId ->
                    val trip: TripExternalDto = trips[tripId]!!
                    // Shape relationships populate `data`
                    val tripShape: ShapeExternalDto = shapes[trip.relationships.shape.data!!.id]!!
                    val tripStops: List<StopExternalDto> = trip.relationships.stops.data.map { stopData ->
                        stops[stopData.id]!!
                    }

                    trip.toTripDto(
                        tripShape.toShapeDto(),
                        tripStops.map { stop -> stop.toStopDto() }
                    )
                }
            )
        }

        return routeDtos
    }

    private fun getUrlWithParams(url: String, params: Map<String, String>): String {
        val query: String = params.entries.joinToString("&") { "${it.key}=${it.value}" }
        return "$url?$query"
    }

    private fun queryRoutes(): ExtendedResponseExternalDto<RouteExternalDto> {
        val url = URI.create(
            getUrlWithParams(
                "${Constants.API_URL}/routes",
                mapOf(
                    "filter[id]" to Route.entries.joinToString(",") { it.id },
                    "include" to "route_patterns",
                )
            )
        ).toURL()
        val connection: HttpsURLConnection = openConnection(url)

        val typeToken = object : TypeToken<ExtendedResponseExternalDto<RouteExternalDto>>() {}.type
        val response: ExtendedResponseExternalDto<RouteExternalDto>
        connection.inputStream.bufferedReader().use {
            try {
                val data: String = it.readText()
                response = gson.fromJson(data, typeToken)
            } finally {
                connection.disconnect()
            }
        }

        return response
    }

    private fun queryTrips(tripIds: List<String>): ExtendedResponseExternalDto<TripExternalDto> {
        val url = URI.create(
            getUrlWithParams(
                "${Constants.API_URL}/trips",
                mapOf(
                    "filter[id]" to tripIds.joinToString(","),
                    "include" to "shape,stops",
                )
            )
        ).toURL()
        val connection: HttpsURLConnection = openConnection(url)

        val typeToken = object : TypeToken<ExtendedResponseExternalDto<TripExternalDto>>() {}.type
        val response: ExtendedResponseExternalDto<TripExternalDto>
        connection.inputStream.bufferedReader().use {
            try {
                val data: String = it.readText()
                response = gson.fromJson(data, typeToken)
            } finally {
                connection.disconnect()
            }
        }

        return response
    }

    private fun openConnection(url: URL): HttpsURLConnection {
        return (url.openConnection() as HttpsURLConnection).also {
            it.requestMethod = "GET"
            it.setRequestProperty("accept", "application/json")
            it.setRequestProperty("x-api-key", apiKey)
            it.connectTimeout = 5000  // 5 seconds
            it.readTimeout = 5000
            it.doInput = true
        }
    }
}