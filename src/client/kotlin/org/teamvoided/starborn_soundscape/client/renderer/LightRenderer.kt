package org.teamvoided.starborn_soundscape.client.renderer

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f
import org.teamvoided.starborn_soundscape.entity.SpotLightEntity
import org.teamvoided.starborn_soundscape.util.toVec3d
import java.awt.Color
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.floor

class LightRenderer(context: EntityRendererFactory.Context?) :
    EntityRenderer<SpotLightEntity>(context) {

    override fun render(
        entity: SpotLightEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        val cubes = entity.dataTracker.get(SpotLightEntity.InnerCubes)
        val colour: Int = entity.dataTracker.get(SpotLightEntity.OuterColour)
        val thickness = entity.dataTracker.get(SpotLightEntity.OuterThickness)
        val innerColour: Int = entity.dataTracker.get(SpotLightEntity.InterColour)
        val tempcol: Color = Color(colour)
        val outerRed = (tempcol.red / 255f)
        val outerBlue = tempcol.blue / 255f
        val outerGreen = tempcol.green / 255f
        val opacity: Float = entity.dataTracker.get(SpotLightEntity.Opacity)
        val buffer = vertexConsumers.getBuffer(RenderLayer.getLightning())
        val thicknessMod = entity.dataTracker.get(SpotLightEntity.EndSize)

        renderEnds(entity, matrices, buffer, thickness * 2f, outerRed, outerGreen, outerBlue, opacity)

        //val pos = entity.dataTracker.get(SpotLightEntity.OriginPos).toVec3d()
        val targetPos = entity.dataTracker.get(SpotLightEntity.Length)
        val repeats = floor(entity.dataTracker.get(SpotLightEntity.Length)).toInt()
        repeat(repeats + 1){
            renderColumn(entity, matrices, buffer, (thickness * 2), outerRed, outerGreen, outerBlue, opacity, it.toDouble(), repeats, thicknessMod)
        }

        for (i in 1..<cubes) {
            val tempColour = (if (i > (cubes / 2)) colour else innerColour)
            val tcol: Color = Color(tempColour)
            val tempRed = tcol.red / 255f
            val tempBlue = tcol.blue / 255f
            val tempGreen = tcol.green / 255f
            val tempThickness = (thickness * ((i.toFloat() / cubes)*2))
            val tempThicknessMod = (thicknessMod * ((i.toFloat() / cubes)))
            //renderEnds(entity, matrices, buffer, tempThickness, tempRed, tempGreen, tempBlue, opacity)

            repeat(repeats + 1){
                renderColumn(entity, matrices, buffer, tempThickness, tempRed, tempGreen, tempBlue, opacity, it.toDouble(), repeats, tempThicknessMod)
            }
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    fun renderEnds(
        entity: SpotLightEntity,
        matrices: MatrixStack,
        buffer: VertexConsumer,
        thickness: Float,
        red: Float,
        green: Float,
        blue: Float,
        opacity: Float
    ) {
        matrices.push()
        RenderSystem.disableCull()
        val pos = entity.eyePos
//        val targetPos = entity.dataTracker.get(SpotLightEntity.TargetPos).toVec3d()
        val distance = entity.dataTracker.get(SpotLightEntity.Length)
        matrices.translate(-(thickness * 0.5), 0.0, -(thickness * 0.5))

//        val vec3d: Vec3d = targetPos
//        val vec3d2: Vec3d = pos
//        var vec3d3 = vec3d.subtract(vec3d2)
//        vec3d3 = vec3d3.normalize()
        val n = entity.yaw
        val o = entity.pitch
        matrices.rotateAround(
            Axis.Y_POSITIVE.rotationDegrees(o),
            (thickness * 0.5f),
            0f,
            (thickness * 0.5f)
        )
        matrices.rotateAround(
            Axis.X_POSITIVE.rotationDegrees(n),
            (thickness * 0.5f),
            0f,
            (thickness * 0.5f)
        )

        val modifiedDistance =
            distance.toFloat() + (if (entity.dataTracker.get(SpotLightEntity.MaxOuterThickness) > 1f) 1f else 0f)

        val a = thickness/2
        val b = (pos.y - entity.y).toFloat()

        buffer.xyz(matrices.peek(), Vector3f(0f, b, 0f)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness, b, 0f)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness, b, thickness)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f, b, thickness)).color(red, green, blue, opacity)
        val thicknessMod = entity.dataTracker.get(SpotLightEntity.EndSize)
//        buffer.xyz(matrices.peek(), Vector3f(0f - thicknessMod, modifiedDistance, 0f - thicknessMod)).color(red, green, blue, opacity)
//        buffer.xyz(matrices.peek(), Vector3f(thickness + thicknessMod, modifiedDistance, 0f - thicknessMod)).color(red, green, blue, opacity)
//        buffer.xyz(matrices.peek(), Vector3f(thickness + thicknessMod, modifiedDistance, thickness + thicknessMod))
//            .color(red, green, blue, opacity)
//        buffer.xyz(matrices.peek(), Vector3f(0f - thicknessMod, modifiedDistance, thickness + thicknessMod)).color(red, green, blue, opacity)


        matrices.pop()
    }

    fun renderColumn(
        entity: SpotLightEntity,
        matrices: MatrixStack,
        buffer: VertexConsumer,
        thickness: Float,
        red: Float,
        green: Float,
        blue: Float,
        opacity: Float,
        iteration: Double,
        repeats: Int,
        thicknessModifier: Float,
    ) {
        matrices.push()
        RenderSystem.disableCull()
        val pos = entity.eyePos
        //val targetPos = entity.dataTracker.get(SpotLightEntity.TargetPos).toVec3d()
        val distance = entity.dataTracker.get(SpotLightEntity.Length)
        matrices.translate(-(thickness * 0.5), (pos.y - entity.pos.y), -(thickness * 0.5))

        //val vec3d: Vec3d = targetPos
        val vec3d2: Vec3d = pos
        //var vec3d3 = vec3d.subtract(vec3d2)
        //vec3d3 = vec3d3.normalize()
        val n = entity.yaw
        val o = entity.pitch
        matrices.rotateAround(
            Axis.Y_POSITIVE.rotationDegrees(o),
            (thickness * 0.5f),
            0f,
            (thickness * 0.5f)
        )
        matrices.rotateAround(
            Axis.X_POSITIVE.rotationDegrees(n),
            (thickness * 0.5f),
            0f,
            (thickness * 0.5f)
        )

        val modifiedDistance =
            distance.toFloat() + (if (entity.dataTracker.get(SpotLightEntity.MaxOuterThickness) > 1f) 1f else 0f)

        var distanceToLessThenOne = modifiedDistance
        while (distanceToLessThenOne > 1) {
            distanceToLessThenOne -= 1
        }

        val a = thickness / 2
        val b = iteration.toFloat()
        val maxHeight = b + (if (iteration.toInt() == repeats) modifiedDistance - b else 1f)
        val silly = (if (iteration.toInt() == repeats) modifiedDistance - b else 1f)
        val thicknessMod = thicknessModifier
        val startThicknessModifier = (1 - ((modifiedDistance - b) / repeats)) * thicknessMod
        val endThicknessModifier = (1 - ((modifiedDistance - maxHeight) / repeats)) * thicknessMod


        buffer.xyz(matrices.peek(), Vector3f(0f - endThicknessModifier, maxHeight, 0f - endThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - startThicknessModifier, b, 0f - startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - startThicknessModifier, b, thickness + startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - endThicknessModifier, maxHeight, thickness + endThicknessModifier)).color(red, green, blue, opacity)

        buffer.xyz(matrices.peek(), Vector3f(thickness + endThicknessModifier, maxHeight, 0f - endThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + startThicknessModifier, b, 0f - startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - startThicknessModifier, b, 0f - startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - endThicknessModifier, maxHeight, 0f - endThicknessModifier)).color(red, green, blue, opacity)

        buffer.xyz(matrices.peek(), Vector3f(thickness + endThicknessModifier, maxHeight, thickness + endThicknessModifier))
            .color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + startThicknessModifier, b, thickness + startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + startThicknessModifier, b, 0f - startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + endThicknessModifier, maxHeight, 0f - endThicknessModifier)).color(red, green, blue, opacity)

        buffer.xyz(matrices.peek(), Vector3f(0f - endThicknessModifier, maxHeight, thickness + endThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(0f - startThicknessModifier, b, thickness + startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + startThicknessModifier, b, thickness + startThicknessModifier)).color(red, green, blue, opacity)
        buffer.xyz(matrices.peek(), Vector3f(thickness + endThicknessModifier, maxHeight, thickness + endThicknessModifier)).color(red, green, blue, opacity)

        matrices.pop()
    }

    override fun shouldRender(entity: SpotLightEntity?, frustum: Frustum?, x: Double, y: Double, z: Double): Boolean {
        return true
    }


    override fun getTexture(entity: SpotLightEntity): Identifier {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE
    }

}