package io.github.kenesu_h.mbtaCraftLivemap.dto.canvas

enum class CanvasDirection(
    val offsetX: Int,
    val offsetZ: Int
) {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0)
}