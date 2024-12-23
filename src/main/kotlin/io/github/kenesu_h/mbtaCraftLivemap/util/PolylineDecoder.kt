package io.github.kenesu_h.mbtaCraftLivemap.util

class PolylineDecoder {
    companion object {
        fun decode(polyline: String): List<Pair<Double, Double>> {
            val coordinates: MutableList<Pair<Double, Double>> = mutableListOf()

            var i = 0
            fun decodeNext(): Int {
                var result = 0
                var shift = 0
                var b: Int

                do {
                    b = polyline[i++].code - 63
                    result = result or ((b and 0x1F) shl shift)
                    shift += 5
                } while (b >= 0x20)

                var delta: Int = result shr 1
                if ((result and 1) != 0) {
                    delta = -(result shr 1)
                }

                return delta
            }

            var latitude = 0
            var longitude = 0

            while (i < polyline.length) {
                latitude += decodeNext()
                longitude += decodeNext()
                coordinates.add(Pair(latitude / 1e5, longitude / 1e5))
            }

            return coordinates
        }
    }
}