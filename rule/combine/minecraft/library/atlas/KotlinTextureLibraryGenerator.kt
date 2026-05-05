package top.fifthlight.combine.resources.atlas

import com.squareup.kotlinpoet.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import top.fifthlight.bazel.worker.api.Worker
import top.fifthlight.combine.resources.Metadata
import top.fifthlight.combine.resources.NinePatch
import top.fifthlight.data.IntOffset
import top.fifthlight.data.IntSize
import java.io.PrintWriter
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Serializable
private data class AtlasMetadata(
    val width: Int,
    val height: Int,
    val textures: Map<String, PlacedTexture>,
)

@Serializable
private data class PlacedTexture(
    val identifier: String,
    val position: IntOffset,
    val size: IntSize,
    val ninePatch: NinePatch?,
)

fun main(vararg args: String) = object : Worker() {
    override fun handleRequest(
        out: PrintWriter,
        sandboxDir: Path?,
        vararg args: String
    ): Int {
        if (args.size < 6) {
            out.println("Usage: KotlinTextureLibraryGenerator <output_file> <package> <class_name> <prefix> <namespace> <atlas_metadata> [--texture <identifier> <png file> <manifest json>]...")
            return 1
        }

        val outputFile = sandboxDir?.resolve(Path.of(args[0])) ?: Path.of(args[0])
        val packageName = args[1]
        val className = args[2]
        val prefix = args[3]
        val namespace = args[4]
        val atlasMetadataPath = sandboxDir?.resolve(Path.of(args[5])) ?: Path.of(args[5])

        val atlasMetadata = Json.decodeFromString<AtlasMetadata>(atlasMetadataPath.readText())

        val classSpecBuilder = TypeSpec.objectBuilder(className + "Impl")
            .addSuperinterface(ClassName(packageName, className))
            .addAnnotation(
                AnnotationSpec.builder(ClassName("top.fifthlight.mergetools.api", "ActualImpl"))
                    .addMember("$packageName.$className::class")
                    .build()
            )
            .addFunction(
                FunSpec.builder("of")
                    .addAnnotation(JvmStatic::class)
                    .addAnnotation(
                        AnnotationSpec.builder(ClassName("top.fifthlight.mergetools.api", "ActualConstructor")).build()
                    )
                    .returns(ClassName(packageName, className))
                    .addCode("return %LImpl", className)
                    .build()
            )

        classSpecBuilder.addProperty(
            PropertySpec.builder("atlasTexture", ClassName("top.fifthlight.combine.paint", "Texture"))
                .initializer(
                    "TextureFactory.create(%S, %S, %L, %L, IntPadding.ZERO)",
                    namespace,
                    "textures/gui/$prefix/atlas.png",
                    atlasMetadata.width,
                    atlasMetadata.height,
                )
                .build()
        )
        classSpecBuilder.addProperty(
            PropertySpec.builder("atlas", ClassName("top.fifthlight.combine.util.atlas", "AtlasTexture"))
                .initializer("AtlasTexture(atlasTexture)")
                .build()
        )

        for ((_, placed) in atlasMetadata.textures) {
            val propertySpec = if (placed.ninePatch != null) {
                PropertySpec
                    .builder(placed.identifier, ClassName("top.fifthlight.combine.paint", "Texture"))
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer(
                        """%T(
                          |    texture = atlas.createPart(
                          |        IntOffset(%L, %L),
                          |        IntSize(%L, %L),
                          |        IntPadding(%L, %L, %L, %L),
                          |    ),
                          |    scaleArea = IntRect(
                          |        offset = IntOffset(%L, %L),
                          |        size = IntSize(%L, %L),
                          |    ),
                          |)
                        """.trimMargin(),
                        ClassName("top.fifthlight.combine.util.ninepatch", "NinePatchTexture"),
                        placed.position.x, placed.position.y,
                        placed.size.width, placed.size.height,
                        placed.ninePatch.padding.left, placed.ninePatch.padding.top,
                        placed.ninePatch.padding.right, placed.ninePatch.padding.bottom,
                        placed.ninePatch.scaleArea.offset.x, placed.ninePatch.scaleArea.offset.y,
                        placed.ninePatch.scaleArea.size.width, placed.ninePatch.scaleArea.size.height,
                    )
                    .build()
            } else {
                PropertySpec
                    .builder(placed.identifier, ClassName("top.fifthlight.combine.paint", "Texture"))
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer(
                        "atlas.createPart(IntOffset(%L, %L), IntSize(%L, %L))",
                        placed.position.x, placed.position.y,
                        placed.size.width, placed.size.height,
                    )
                    .build()
            }
            classSpecBuilder.addProperty(propertySpec)
        }

        var i = 6
        while (i < args.size) {
            if (args.size - i < 4) {
                out.println("Bad texture entry")
                return 1
            }

            when (args[i]) {
                "--texture" -> {
                    val identifier = args[i + 1]
                    var metadataPath = Path.of(args[i + 3])
                    if (sandboxDir != null) {
                        metadataPath = sandboxDir.resolve(metadataPath)
                    }
                    val metadata = Json.decodeFromString<Metadata>(metadataPath.readText())
                    classSpecBuilder.addProperty(
                        PropertySpec.builder(
                            identifier,
                            ClassName("top.fifthlight.combine.paint", "BackgroundTexture")
                        ).addModifiers(KModifier.OVERRIDE).initializer(
                            "BackgroundTextureFactory.create(%S, %S, %L, %L)",
                            namespace,
                            "textures/gui/$prefix/$identifier.png",
                            metadata.size.width,
                            metadata.size.height,
                        ).build()
                    )
                    i += 4
                }

                else -> {
                    out.println("Bad entry: ${args[i]}")
                    return 1
                }
            }
        }

        val file = FileSpec
            .builder(packageName, className)
            .addAnnotation(
                AnnotationSpec
                    .builder(Suppress::class)
                    .addMember("%S", "RedundantVisibilityModifier")
                    .build()
            )
            .addImport("top.fifthlight.combine.paint", "BackgroundTextureFactory")
            .addImport("top.fifthlight.combine.paint", "TextureFactory")
            .addImport("top.fifthlight.combine.util.atlas", "AtlasTexture")
            .addImport("top.fifthlight.combine.util.ninepatch", "NinePatchTexture")
            .addImport("top.fifthlight.data", "IntOffset")
            .addImport("top.fifthlight.data", "IntPadding")
            .addImport("top.fifthlight.data", "IntRect")
            .addImport("top.fifthlight.data", "IntSize")
            .addType(classSpecBuilder.build())
            .build()
        outputFile.writeText(buildString { file.writeTo(this) })
        return 0
    }
}.run(*args)
