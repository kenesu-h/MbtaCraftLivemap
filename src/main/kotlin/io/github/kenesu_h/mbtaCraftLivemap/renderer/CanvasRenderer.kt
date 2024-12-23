package io.github.kenesu_h.mbtaCraftLivemap.renderer

import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasDirection
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasRouteDto
import io.github.kenesu_h.mbtaCraftLivemap.dto.canvas.CanvasVehicleDto
import io.github.kenesu_h.mbtaCraftLivemap.constant.CanvasConstant
import org.bukkit.World
import java.util.logging.Logger

class CanvasRenderer(
    private val world: World,
    private val originX: Int,
    private val originY: Int,
    private val originZ: Int,
    private val size: Int,
    private val direction: CanvasDirection,
    private val routes: List<CanvasRouteDto>,
    private val vehicles: List<CanvasVehicleDto>,
    private val logger: Logger
) : BaseRenderer(
    world, originX, originY, originZ, size, direction
) {
    override fun render() {
        renderRectangle(size, size, CanvasConstant.BACKGROUND_MATERIAL)

        routes.forEach {
            RouteRenderer(
                world = world,
                originX = originX,
                originY = originY,
                originZ = originZ,
                size = size,
                direction = direction,
                route = it
            ).render()
        }

        vehicles.forEach {
            VehicleRenderer(
                world = world,
                originX = originX,
                originY = originY,
                originZ = originZ,
                size = size,
                direction = direction,
                vehicle = it,
                logger = logger,
            ).render()
        }
    }
}