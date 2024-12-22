package io.github.kenesu_h.mbtaCraftLivemap.renderer

import io.github.kenesu_h.mbtaCraftLivemap.math.CircleHelper
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.Lightable

abstract class BaseRenderer(
    private val world: World,
    private val originX: Int,
    private val originY: Int,
    private val originZ: Int,
    private val size: Int,
    private val direction: CanvasDirection,
) {
    abstract fun render()

    protected fun renderRectangle(
        width: Int,
        height: Int,
        material: Material,
        isLit: Boolean? = null
    ) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                setBlock(Pair(x, y), material, isLit)
            }
        }
    }

    protected fun renderCircle(
        coordinates: Pair<Int, Int>,
        radius: Int,
        material: Material,
        isLit: Boolean? = null
    ) {
        val range: IntRange = -radius..radius
        for (dx in range) {
            for (dy in range) {
                if (!CircleHelper.isPointInCircle(Pair(dx, dy), radius)) {
                    continue
                }

                setBlock(
                    Pair(coordinates.first + dx, coordinates.second + dy),
                    material,
                    isLit
                )
            }
        }
    }

    protected fun renderLine(
        start: Pair<Int, Int>,
        end: Pair<Int, Int>,
        weight: Int,
        material: Material,
        isLit: Boolean? = null
    ) {
        val lineVector: Pair<Int, Int> = Pair(
            end.first - start.first,
            end.second - start.second
        )

        val steps: Int = maxOf(lineVector.first, lineVector.second)
        val stepX = lineVector.first.toDouble() / steps
        val stepY = lineVector.second.toDouble() / steps

        var x = start.first.toDouble()
        var y = start.second.toDouble()

        val range: IntRange = -weight..weight
        for (i in 0..steps) {
            for (dx in range) {
                for (dy in range) {
                    if (
                        !CircleHelper.isPointInCircle(Pair(dx, dy), weight)
                    ) {
                        continue
                    }

                    setBlock(
                        Pair((x + dx).toInt(), (y + dy).toInt()),
                        material,
                        isLit
                    )
                }
            }

            x += stepX
            y += stepY
        }
    }

    private fun setBlock(
        coordinates: Pair<Int, Int>,
        material: Material,
        isLit: Boolean? = null
    ) {
        val blockX: Int = when (direction) {
            CanvasDirection.EAST -> originX + coordinates.first
            CanvasDirection.WEST -> originX - coordinates.first
            else -> originX
        }
        val blockY: Int = originY + coordinates.second
        val blockZ: Int = when (direction) {
            CanvasDirection.NORTH -> originZ - coordinates.first
            CanvasDirection.SOUTH -> originZ + coordinates.first
            else -> originZ
        }

        val block = world.getBlockAt(blockX, blockY, blockZ)
        block.type = material

        val blockData = block.blockData
        if (isLit != null && blockData is Lightable) {
            blockData.isLit = true
            block.blockData = blockData
        }
    }
}