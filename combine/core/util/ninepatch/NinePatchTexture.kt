package top.fifthlight.combine.util.ninepatch

import top.fifthlight.combine.paint.Canvas
import top.fifthlight.combine.paint.Color
import top.fifthlight.combine.paint.Texture
import top.fifthlight.data.*

data class NinePatchTexture(
    val texture: Texture,
    val scaleArea: IntRect,
) : Texture {
    override val size: IntSize
        get() = texture.size
    override val padding: IntPadding
        get() = texture.padding

    private val borderWidth = scaleArea.left + (texture.size.width - scaleArea.right)
    private val borderHeight = scaleArea.top + (texture.size.height - scaleArea.bottom)
    private val scaleAreaRightWidth = texture.size.width - scaleArea.right
    private val scaleAreaBottomHeight = texture.size.height - scaleArea.bottom

    private val scaleAreaTopLeftRect = IntRect(
        offset = IntOffset.ZERO,
        size = IntSize(
            width = scaleArea.left,
            height = scaleArea.top,
        ),
    )
    private val scaleAreaTopRect = IntRect(
        offset = IntOffset(
            x = scaleArea.left,
            y = 0,
        ),
        size = IntSize(
            width = scaleArea.size.width,
            height = scaleArea.top,
        ),
    )
    private val scaleAreaTopRightRect = IntRect(
        offset = IntOffset(
            x = scaleArea.right,
            y = 0,
        ),
        size = IntSize(
            width = scaleAreaRightWidth,
            height = scaleArea.top,
        )
    )
    private val scaleAreaMiddleLeftRect = IntRect(
        offset = IntOffset(
            x = 0,
            y = scaleArea.top,
        ),
        size = IntSize(
            width = scaleArea.left,
            height = scaleArea.size.height,
        )
    )
    private val scaleAreaMiddleRect = IntRect(
        offset = IntOffset(
            x = scaleArea.left,
            y = scaleArea.top,
        ),
        size = IntSize(
            width = scaleArea.size.width,
            height = scaleArea.size.height,
        )
    )
    private val scaleAreaMiddleRightRect = IntRect(
        offset = IntOffset(
            x = scaleArea.right,
            y = scaleArea.top,
        ),
        size = IntSize(
            width = scaleAreaRightWidth,
            height = scaleArea.size.height,
        )
    )

    private val scaleAreaBottomLeftRect = IntRect(
        offset = IntOffset(
            x = 0,
            y = scaleArea.bottom,
        ),
        size = IntSize(
            width = scaleArea.left,
            height = scaleAreaBottomHeight,
        )
    )
    private val scaleAreaBottomRect = IntRect(
        offset = IntOffset(
            x = scaleArea.left,
            y = scaleArea.bottom,
        ),
        size = IntSize(
            width = scaleArea.size.width,
            height = scaleAreaBottomHeight,
        )
    )
    private val scaleAreaBottomRightRect = IntRect(
        offset = IntOffset(
            x = scaleArea.right,
            y = scaleArea.bottom,
        ),
        size = IntSize(
            width = scaleAreaRightWidth,
            height = scaleAreaBottomHeight,
        )
    )

    override fun draw(
        canvas: Canvas,
        dstRect: Rect,
        tint: Color,
    ) {
        val dstScaleAreaLeft = dstRect.left + scaleArea.left
        val dstScaleAreaRight = dstRect.right - scaleAreaRightWidth
        val dstScaleAreaWidth = dstRect.size.width - borderWidth
        val dstScaleAreaTop = dstRect.top + scaleArea.top
        val dstScaleAreaBottom = dstRect.bottom - scaleAreaBottomHeight
        val dstScaleAreaHeight = dstRect.size.height - borderHeight

        // Top-left corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopLeftRect.toRect(),
            dstRect = Rect(
                offset = dstRect.offset,
                size = scaleAreaTopLeftRect.size.toSize(),
            ),
        )

        // Top edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaLeft,
                    y = dstRect.offset.y,
                ),
                size = Size(
                    width = dstScaleAreaWidth,
                    height = scaleArea.top.toFloat(),
                ),
            )
        )

        // Top-right corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopRightRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaRight,
                    y = dstRect.offset.y,
                ),
                size = scaleAreaTopRightRect.size.toSize(),
            )
        )

        // Middle-left edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleLeftRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstRect.offset.x,
                    y = dstScaleAreaTop,
                ),
                size = Size(
                    width = scaleArea.left.toFloat(),
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Middle-center (scale area)
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaLeft,
                    y = dstScaleAreaTop,
                ),
                size = Size(
                    width = dstScaleAreaWidth,
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Middle-right edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleRightRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaRight,
                    y = dstScaleAreaTop,
                ),
                size = Size(
                    width = scaleAreaRightWidth.toFloat(),
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Bottom-left corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomLeftRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstRect.offset.x,
                    y = dstScaleAreaBottom,
                ),
                size = scaleAreaBottomLeftRect.size.toSize(),
            )
        )

        // Bottom edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaLeft,
                    y = dstScaleAreaBottom,
                ),
                size = Size(
                    width = dstScaleAreaWidth,
                    height = scaleAreaBottomHeight.toFloat(),
                ),
            )
        )

        // Bottom-right corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomRightRect.toRect(),
            dstRect = Rect(
                offset = Offset(
                    x = dstScaleAreaRight,
                    y = dstScaleAreaBottom,
                ),
                size = scaleAreaBottomRightRect.size.toSize(),
            )
        )
    }

    override fun draw(
        canvas: Canvas,
        dstRect: IntRect,
        tint: Color
    ) {
        val dstScaleAreaLeft = dstRect.left + scaleArea.left
        val dstScaleAreaRight = dstRect.right - scaleAreaRightWidth
        val dstScaleAreaWidth = dstRect.size.width - borderWidth
        val dstScaleAreaTop = dstRect.top + scaleArea.top
        val dstScaleAreaBottom = dstRect.bottom - scaleAreaBottomHeight
        val dstScaleAreaHeight = dstRect.size.height - borderHeight

        // Top-left corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopLeftRect,
            dstRect = IntRect(
                offset = dstRect.offset,
                size = scaleAreaTopLeftRect.size,
            ),
        )

        // Top edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaLeft,
                    y = dstRect.offset.y,
                ),
                size = IntSize(
                    width = dstScaleAreaWidth,
                    height = scaleArea.top,
                ),
            )
        )

        // Top-right corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaTopRightRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaRight,
                    y = dstRect.offset.y,
                ),
                size = scaleAreaTopRightRect.size,
            )
        )

        // Middle-left edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleLeftRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstRect.offset.x,
                    y = dstScaleAreaTop,
                ),
                size = IntSize(
                    width = scaleArea.left,
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Middle-center (scale area)
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaLeft,
                    y = dstScaleAreaTop,
                ),
                size = IntSize(
                    width = dstScaleAreaWidth,
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Middle-right edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaMiddleRightRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaRight,
                    y = dstScaleAreaTop,
                ),
                size = IntSize(
                    width = scaleAreaRightWidth,
                    height = dstScaleAreaHeight,
                ),
            )
        )

        // Bottom-left corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomLeftRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstRect.offset.x,
                    y = dstScaleAreaBottom,
                ),
                size = scaleAreaBottomLeftRect.size,
            )
        )

        // Bottom edge
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaLeft,
                    y = dstScaleAreaBottom,
                ),
                size = IntSize(
                    width = dstScaleAreaWidth,
                    height = scaleAreaBottomHeight,
                ),
            )
        )

        // Bottom-right corner
        texture.draw(
            canvas = canvas,
            tint = tint,
            srcRect = scaleAreaBottomRightRect,
            dstRect = IntRect(
                offset = IntOffset(
                    x = dstScaleAreaRight,
                    y = dstScaleAreaBottom,
                ),
                size = scaleAreaBottomRightRect.size,
            )
        )
    }

    override fun draw(
        canvas: Canvas,
        dstRect: Rect,
        tint: Color,
        srcRect: Rect,
    ) = texture.draw(
        canvas = canvas,
        dstRect = dstRect,
        tint = tint,
        srcRect = srcRect,
    )

    override fun draw(
        canvas: Canvas,
        dstRect: IntRect,
        tint: Color,
        srcRect: IntRect,
    ) = texture.draw(
        canvas = canvas,
        dstRect = dstRect,
        tint = tint,
        srcRect = srcRect,
    )
}
