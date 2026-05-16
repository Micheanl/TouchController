package top.fifthlight.combine.backend.minecraft.render.v1_21_1

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.resources.ResourceLocation
import top.fifthlight.combine.core.paint.BackgroundTexture
import top.fifthlight.combine.core.paint.Canvas
import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Texture
import top.fifthlight.data.*
import top.fifthlight.mergetools.api.ActualConstructor
import top.fifthlight.mergetools.api.ActualImpl

@ActualImpl(Texture::class)
data class TextureImpl(
    val resourceLocation: ResourceLocation,
    val sprite: Boolean,
    override val size: IntSize,
    override val padding: IntPadding = IntPadding.ZERO,
) : Texture {
    companion object : Texture.Factory {
        @ActualConstructor
        @JvmStatic
        override fun create(
            namespace: String,
            id: String,
            width: Int,
            height: Int,
            padding: IntPadding,
        ): Texture = TextureImpl(
            resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, id),
            sprite = false,
            size = IntSize(width, height),
            padding = padding,
        )

        @ActualConstructor
        @JvmStatic
        override fun createSprite(
            namespace: String,
            id: String,
            width: Int,
            height: Int,
            padding: IntPadding,
        ): Texture = TextureImpl(
            resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, id),
            sprite = true,
            size = IntSize(width, height),
            padding = padding,
        )
    }

    override fun draw(
        canvas: Canvas,
        dstRect: Rect,
        tint: Color,
        srcRect: Rect,
    ) {
        val canvasImpl = canvas as CanvasImpl
        if (sprite) {
            canvasImpl.drawSprite(
                spriteId = resourceLocation,
                dstRect = dstRect,
                srcRect = srcRect,
                tint = tint,
            )
        } else {
            canvas.drawTexture(
                resourceLocation = resourceLocation,
                dstRect = dstRect,
                uvRect = srcRect / size.toSize(),
                tint = tint,
            )
        }
    }

    override fun draw(
        canvas: Canvas,
        dstRect: IntRect,
        tint: Color,
    ) {
        val guiGraphics = (canvas as CanvasImpl).guiGraphics
        RenderSystem.enableBlend()
        guiGraphics.setColor(tint.rFloat, tint.gFloat, tint.bFloat, tint.aFloat)
        if (sprite) {
            guiGraphics.blitSprite(
                resourceLocation,
                dstRect.offset.x,
                dstRect.offset.y,
                dstRect.size.width,
                dstRect.size.height,
            )
        } else {
            guiGraphics.blit(
                resourceLocation,
                dstRect.offset.x,
                dstRect.offset.y,
                0f,
                0f,
                dstRect.size.width,
                dstRect.size.height,
                size.width,
                size.height,
            )
        }
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
    }
}

@ActualImpl(BackgroundTexture::class)
data class BackgroundTextureImpl(
    val resourceLocation: ResourceLocation,
    override val size: IntSize,
) : BackgroundTexture {
    companion object : BackgroundTexture.Factory {
        @ActualConstructor
        @JvmStatic
        override fun create(
            namespace: String,
            id: String,
            width: Int,
            height: Int,
        ): BackgroundTexture = BackgroundTextureImpl(
            resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, id),
            size = IntSize(width, height),
        )
    }

    override fun draw(
        canvas: Canvas,
        dstRect: Rect,
        tint: Color,
        scale: Float,
    ) {
        val canvasImpl = canvas as CanvasImpl
        canvasImpl.drawTexture(
            resourceLocation = resourceLocation,
            dstRect = dstRect,
            uvRect = Rect(
                offset = Offset.ZERO,
                size = Size(
                    dstRect.size.width / size.width / scale,
                    dstRect.size.height / size.height / scale,
                ),
            ),
            tint = tint,
        )
    }
}
