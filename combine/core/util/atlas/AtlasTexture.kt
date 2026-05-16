package top.fifthlight.combine.core.util.atlas

import top.fifthlight.combine.core.paint.Canvas
import top.fifthlight.combine.core.paint.Color
import top.fifthlight.combine.core.paint.Texture
import top.fifthlight.data.*

data class AtlasTexture(
    val texture: Texture,
) {
    inner class Part(
        private val offset: IntOffset,
        override val size: IntSize,
        override val padding: IntPadding = IntPadding.ZERO,
    ) : Texture {
        private val srcIntRect = IntRect(
            offset = offset,
            size = size,
        )
        private val srcFloatRect = srcIntRect.toRect()

        override fun draw(
            canvas: Canvas,
            dstRect: Rect,
            tint: Color,
            srcRect: Rect,
        ) {
            this@AtlasTexture.texture.draw(
                canvas = canvas,
                dstRect = dstRect,
                tint = tint,
                srcRect = Rect(
                    offset = srcRect.offset + srcFloatRect.offset,
                    size = srcRect.size,
                )
            )
        }

        override fun draw(canvas: Canvas, dstRect: IntRect, tint: Color, srcRect: IntRect) {
            this@AtlasTexture.texture.draw(
                canvas = canvas,
                dstRect = dstRect,
                tint = tint,
                srcRect = IntRect(
                    offset = srcRect.offset + srcIntRect.offset,
                    size = srcRect.size,
                )
            )
        }

        override fun draw(canvas: Canvas, dstRect: IntRect, tint: Color) {
            this@AtlasTexture.texture.draw(
                canvas = canvas,
                dstRect = dstRect,
                tint = tint,
                srcRect = srcIntRect,
            )
        }
    }

    fun createPart(
        offset: IntOffset,
        size: IntSize,
        padding: IntPadding = IntPadding.ZERO,
    ) = Part(offset, size, padding)
}
