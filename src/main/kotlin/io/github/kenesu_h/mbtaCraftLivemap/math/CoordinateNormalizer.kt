package io.github.kenesu_h.mbtaCraftLivemap.math

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants

class CoordinateNormalizer(
    private val minLatitude: Double = Constants.MIN_LATITUDE,
    private val maxLatitude: Double = Constants.MAX_LATITUDE,
    private val minLongitude: Double = Constants.MIN_LONGITUDE,
    private val maxLongitude: Double = Constants.MAX_LONGITUDE,
    private val size: Int
) {
    fun normalize(coordinates: Pair<Double, Double>): Pair<Int, Int> {
        return Pair(
            ((coordinates.second - minLongitude) / (maxLongitude - minLongitude) * size).toInt(),
            ((coordinates.first - minLatitude) / (maxLatitude - minLatitude) * size).toInt()
        )
    }
}