package io.github.kenesu_h.mbtaCraftLivemap.util

class LineHelper {
    companion object {
        fun isPointNearLine(
            point: Pair<Int, Int>,
            lineStart: Pair<Int, Int>,
            lineEnd: Pair<Int, Int>,
            lineWeight: Int
        ): Boolean {
            val lineVector: Pair<Int, Int> = Pair(
                lineEnd.first - lineStart.first,
                lineEnd.second - lineStart.second
            )
            val lengthSquared: Int = (lineVector.first * lineVector.first) + (lineVector.second * lineVector.second)

            val pointToStart: Pair<Int, Int> = Pair(
                point.first - lineStart.first,
                point.second - lineStart.second
            )
            if (lengthSquared == 0) {
                return CircleHelper.isPointInCircle(pointToStart, lineWeight)
            }

            // Calculate projection scalar `t` onto the line vector
            val t: Double = (
                    (pointToStart.first * lineVector.first) + (pointToStart.second * lineVector.second)
                    ).toDouble() / lengthSquared
            val clampedT: Double = t.coerceIn(0.0, 1.0)

            // Nearest point on the line segment
            val nearest: Pair<Double, Double> = Pair(
                lineStart.first + (clampedT * lineVector.first),
                lineStart.second + (clampedT * lineVector.second)
            )

            return CircleHelper.isPointInCircle(
                Pair(
                    point.first - nearest.first,
                    point.second - nearest.second
                ),
                lineWeight.toDouble()
            )
        }
    }
}