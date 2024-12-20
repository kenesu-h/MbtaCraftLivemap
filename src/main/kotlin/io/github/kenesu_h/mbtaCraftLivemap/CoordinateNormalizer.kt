package io.github.kenesu_h.mbtaCraftLivemap

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.Constants

class CoordinateNormalizer(
    private val minLatitude: Double = Constants.MIN_LATITUDE,
    private val maxLatitude: Double = Constants.MAX_LATITUDE,
    private val minLongitude: Double = Constants.MIN_LONGITUDE,
    private val maxLongitude: Double = Constants.MAX_LONGITUDE,
    private val size: Int
) {
    fun normalizeLatitude(latitude: Double): Int {
        return ((latitude - minLatitude) / (maxLatitude - minLatitude) * size).toInt()
    }

    fun normalizeLongitude(longitude: Double): Int {
        return ((longitude - minLongitude) / (maxLongitude - minLongitude) * size).toInt()
    }
}