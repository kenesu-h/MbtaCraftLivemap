package io.github.kenesu_h.mbtaCraftLivemap.util

class CircleHelper {
    companion object {
        fun isPointInCircle(point: Pair<Int, Int>, radius: Int): Boolean {
            return (point.first * point.first) + (point.second * point.second) <= (radius * radius)
        }

        fun isPointInCircle(point: Pair<Double, Double>, radius: Double): Boolean {
            return (point.first * point.first) + (point.second * point.second) <= (radius * radius)
        }
    }
}