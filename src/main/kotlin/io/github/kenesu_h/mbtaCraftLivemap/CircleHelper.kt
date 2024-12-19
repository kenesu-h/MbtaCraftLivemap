package io.github.kenesu_h.mbtaCraftLivemap

class CircleHelper {
    companion object {
        fun isPointInCircle(x: Int, y: Int, radius: Int): Boolean {
            return (x * x) + (y * y) <= (radius * radius)
        }
    }
}