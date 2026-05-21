package top.fifthlight.combine.resources.altas

import kotlinx.serialization.json.Json
import org.lwjgl.stb.STBRPContext
import org.lwjgl.stb.STBRPNode
import org.lwjgl.stb.STBRPRect
import org.lwjgl.stb.STBRectPack
import org.lwjgl.system.MemoryStack
import top.fifthlight.bazel.worker.api.Worker
import top.fifthlight.combine.resources.Metadata
import top.fifthlight.combine.resources.NinePatch
import top.fifthlight.combine.resources.NinePatchMetadata
import top.fifthlight.data.IntOffset
import top.fifthlight.data.IntSize
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.PrintWriter
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.readText
import kotlin.io.path.writeText

private data class Texture(
    val identifier: String,
    val ninePatch: NinePatch?,
    val image: BufferedImage,
) {
    val size: IntSize
        get() = IntSize(
            image.width,
            image.height,
        )

    fun place(position: IntOffset) = PlacedTexture(
        identifier = identifier,
        position = position,
        size = size,
        ninePatch = ninePatch,
    )
}

fun main(vararg args: String) = object : Worker() {
    override fun handleRequest(
        out: PrintWriter,
        sandboxDir: Path?,
        vararg args: String
    ): Int {
        if (args.size < 2) {
            out.println("Usage: TextureAtlasGenerator <atlas_output> <metadata_output> --width <width> --height <height> [--texture <identifier> <png file> <manifest json>] [--ninepatch <identifier> <png file> <manifest json>]...")
            return 1
        }

        val atlasOutput = sandboxDir?.resolve(Path.of(args[0])) ?: Path.of(args[0])
        val metadataOutput = sandboxDir?.resolve(Path.of(args[1])) ?: Path.of(args[1])

        var atlasWidth = 512
        var atlasHeight = 512
        val placedTextures = hashMapOf<String, PlacedTexture>()

        val textures = mutableListOf<Texture>()

        var i = 2
        while (i < args.size) {
            when (val type = args[i]) {
                "--width" -> {
                    if (i + 1 >= args.size) {
                        out.println("--width requires an argument")
                        return 1
                    }
                    atlasWidth = args[i + 1].toInt()
                    i += 2
                }

                "--height" -> {
                    if (i + 1 >= args.size) {
                        out.println("--height requires an argument")
                        return 1
                    }
                    atlasHeight = args[i + 1].toInt()
                    i += 2
                }

                "--texture" -> {
                    if (i + 3 >= args.size) {
                        out.println("$type requires 3 arguments")
                        return 1
                    }

                    val identifier = args[i + 1]
                    val pngFile = sandboxDir?.resolve(Path.of(args[i + 2])) ?: Path.of(args[i + 2])
                    val manifestFile = sandboxDir?.resolve(Path.of(args[i + 3])) ?: Path.of(args[i + 3])
                    val manifest = Json.decodeFromString<Metadata>(manifestFile.readText())
                    if (!manifest.background) {
                        val image = ImageIO.read(pngFile.toFile())
                        textures += Texture(
                            identifier = identifier,
                            ninePatch = null,
                            image = image,
                        )
                    }
                    i += 4
                }

                "--ninepatch" -> {
                    if (i + 3 >= args.size) {
                        out.println("--ninepatch requires 3 arguments")
                        return 1
                    }

                    val identifier = args[i + 1]
                    val pngFile = sandboxDir?.resolve(Path.of(args[i + 2])) ?: Path.of(args[i + 2])
                    val manifestFile = sandboxDir?.resolve(Path.of(args[i + 3])) ?: Path.of(args[i + 3])
                    val manifest = Json.decodeFromString<NinePatchMetadata>(manifestFile.readText())
                    val image = ImageIO.read(pngFile.toFile())
                    textures += Texture(
                        identifier = identifier,
                        ninePatch = manifest.ninePatch,
                        image = image,
                    )
                    i += 4
                }

                else -> {
                    out.println("Bad entry: $type")
                    return 1
                }
            }
        }

        textures.sortByDescending { texture ->
            texture.size.width * texture.size.height
        }

        val outputImage = BufferedImage(atlasWidth, atlasHeight, TYPE_INT_ARGB)

        MemoryStack.stackPush().use { stack ->
            val context = STBRPContext.malloc(stack)
            val nodes = STBRPNode.malloc(atlasWidth, stack)
            STBRectPack.stbrp_init_target(context, atlasWidth, atlasHeight, nodes)

            val rectangles = STBRPRect.malloc(textures.size, stack)
            for ((index, texture) in textures.withIndex()) {
                rectangles[index].set(index, texture.size.width, texture.size.height, 0, 0, false)
            }

            STBRectPack.stbrp_pack_rects(context, rectangles)

            val outputGraphics = outputImage.createGraphics()
            for ((index, rect) in rectangles.withIndex()) {
                val texture = textures[index]

                if (!rect.was_packed()) {
                    outputGraphics.dispose()
                    out.println(
                        """Failed to pack texture '${texture.identifier}'
                            |(size: ${texture.size.width}x${texture.size.height}) into atlas
                            |(size: ${atlasWidth}x${atlasHeight}).
                            |The texture does not fit or the atlas is too small.""".trimMargin(),
                    )
                    return 1
                }

                val position = IntOffset(
                    x = rect.x(),
                    y = rect.y(),
                )

                if (position.x < 0 || position.y < 0 ||
                    position.x + texture.size.width > atlasWidth ||
                    position.y + texture.size.height > atlasHeight
                ) {
                    outputGraphics.dispose()
                    out.println(
                        """Texture '${texture.identifier}' placed at invalid position: $position
                            |with size: ${texture.size} in atlas: ${atlasWidth}x${atlasHeight}""".trimMargin(),
                    )
                    return 1
                }

                placedTextures[texture.identifier] = texture.place(position)
                outputGraphics.drawImage(texture.image, position.x, position.y, null)
            }

            outputGraphics.dispose()
        }

        ImageIO.write(outputImage, "png", atlasOutput.toFile())

        metadataOutput.writeText(
            Json.encodeToString(
                AtlasMetadata(
                    width = atlasWidth,
                    height = atlasHeight,
                    textures = placedTextures,
                ),
            ),
        )
        return 0
    }
}.run(*args)
