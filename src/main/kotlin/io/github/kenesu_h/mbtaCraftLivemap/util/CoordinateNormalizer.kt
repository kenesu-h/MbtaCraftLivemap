package io.github.kenesu_h.mbtaCraftLivemap.util

import io.github.kenesu_h.mbtaCraftLivemap.constant.CanvasConstant

class CoordinateNormalizer(
    private val minLatitude: Double = CanvasConstant.MIN_LATITUDE,
    private val maxLatitude: Double = CanvasConstant.MAX_LATITUDE,
    private val minLongitude: Double = CanvasConstant.MIN_LONGITUDE,
    private val maxLongitude: Double = CanvasConstant.MAX_LONGITUDE,
    private val size: Int
) {
    fun normalize(coordinates: Pair<Double, Double>): Pair<Int, Int> {
        return Pair(
            ((coordinates.second - minLongitude) / (maxLongitude - minLongitude) * size).toInt(),
            ((coordinates.first - minLatitude) / (maxLatitude - minLatitude) * size).toInt()
        )
    }
}