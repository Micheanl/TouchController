package top.fifthlight.touchcontroller.common.platform.provider

import org.slf4j.LoggerFactory
import top.fifthlight.touchcontroller.common.gal.window.GlfwPlatform
import top.fifthlight.touchcontroller.common.gal.window.PlatformWindowProvider
import top.fifthlight.touchcontroller.common.platform.Platform
import top.fifthlight.touchcontroller.common.platform.android.AndroidPlatform
import top.fifthlight.touchcontroller.common.platform.provider.PlatformProvider.isAndroid
import top.fifthlight.touchcontroller.common.platform.provider.PlatformProvider.systemArch
import top.fifthlight.touchcontroller.common.platform.provider.PlatformProvider.systemName
import top.fifthlight.touchcontroller.common.platform.wayland.WaylandPlatform
import top.fifthlight.touchcontroller.common.platform.win32.Win32Platform
import java.nio.file.Path
import java.nio.file.attribute.DosFileAttributeView
import java.nio.file.attribute.PosixFileAttributeView
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.fileAttributesView

internal object NativeLibraryLoader {
    private val logger = LoggerFactory.getLogger(NativeLibraryLoader::class.java)

    data class NativeLibraryInfo(
        val modContainerPath: String,
        val extractPrefix: String,
        val extractSuffix: String,
        val readOnlySetter: (Path) -> Unit = {},
        val removeAfterLoaded: Boolean,
        val platformFactory: () -> Platform,
    )

    private fun windowsReadOnlySetter(path: Path) {
        val attributeView = path.fileAttributesView<DosFileAttributeView>()
        attributeView.setReadOnly(true)
    }

    private fun posixReadOnlySetter(path: Path) {
        val attributeView = path.fileAttributesView<PosixFileAttributeView>()
        // 500
        attributeView.setPermissions(
            setOf(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_EXECUTE
            )
        )
    }

    fun probeNativeLibraryInfo(): NativeLibraryInfo? {
        if ((systemName.startsWith("Linux", ignoreCase = true) && isAndroid) ||
            systemName.contains("Android", ignoreCase = true)
        ) {
            logger.info("Android detected")

            val socketName = System.getenv("TOUCH_CONTROLLER_PROXY_SOCKET")?.takeIf { it.isNotEmpty() }
            if (socketName == null) {
                logger.info("No TOUCH_CONTROLLER_PROXY_SOCKET environment set, TouchController will not be loaded")
                return null
            }

            val target = when (systemArch) {
                "x86_32", "x86", "i386", "i486", "i586", "i686" -> "android_x86_32"
                "amd64", "x86_64" -> "android_x86_64"
                "armeabi", "armeabi-v7a", "armhf", "arm", "armel" -> "android_armv7"
                "arm64", "aarch64" -> "android_aarch64"
                else -> null
            } ?: run {
                logger.warn("Unsupported Android arch")
                return null
            }
            logger.info("Target: $target")

            val libraryName = "proxy_server_android"

            return NativeLibraryInfo(
                modContainerPath = "${libraryName}_${target}/lib${libraryName}.so",
                extractPrefix = "lib$libraryName",
                extractSuffix = ".so",
                readOnlySetter = ::posixReadOnlySetter,
                removeAfterLoaded = true,
                platformFactory = { AndroidPlatform(socketName) },
            )
        }

        when (val platform = PlatformWindowProvider.platform) {
            is GlfwPlatform.Win32 -> {
                val target = when (systemArch) {
                    "x86_32", "x86", "i386", "i486", "i586", "i686" -> "windows_x86_32"
                    "amd64", "x86_64" -> "windows_x86_64"
                    "arm64", "aarch64" -> "windows_aarch64"
                    else -> null
                } ?: run {
                    logger.warn("Unsupported Windows arch: $systemArch")
                    return null
                }
                val systemVersion = System.getProperty("os.version")
                val majorVersion = systemVersion.substringBefore(".").toIntOrNull()
                val isLegacy = majorVersion == null || majorVersion < 10
                logger.info("Target: $target, legacy: $isLegacy")
                val libraryName = if (isLegacy) {
                    "proxy_server_windows_legacy"
                } else {
                    "proxy_server_windows"
                }

                return NativeLibraryInfo(
                    modContainerPath = "${libraryName}_${target}/lib${libraryName}.dll",
                    extractPrefix = "lib$libraryName",
                    extractSuffix = ".dll",
                    readOnlySetter = ::windowsReadOnlySetter,
                    removeAfterLoaded = false,
                    platformFactory = { Win32Platform(platform.nativeWindow) },
                )
            }

            is GlfwPlatform.Wayland, GlfwPlatform.X11 -> {
                val target = when (systemArch) {
                    "amd64", "x86_64" -> "linux_x86_64"
                    "armv8", "arm64", "aarch64" -> "linux_aarch64"
                    else -> null
                } ?: run {
                    logger.warn("Unsupported Linux arch: $systemArch")
                    return null
                }
                val libraryName = when (platform) {
                    is GlfwPlatform.Wayland -> "proxy_server_wayland"
                    is GlfwPlatform.X11 -> {
                        logger.warn("X11 is not supported for now")
                        return null
                    }

                    else -> throw AssertionError()
                }
                // TODO: detect musl, and use musl libraries
                logger.info("Target: $target")

                return NativeLibraryInfo(
                    modContainerPath = "${libraryName}_${target}/lib${libraryName}.so",
                    extractPrefix = "lib$libraryName",
                    extractSuffix = ".so",
                    readOnlySetter = ::posixReadOnlySetter,
                    removeAfterLoaded = true,
                    platformFactory = { WaylandPlatform(platform.nativeWindow) },
                )
            }

            GlfwPlatform.Cocoa -> {
                logger.warn("macOS is not supported for now")
                return null
            }

            GlfwPlatform.Unknown -> {
                logger.warn("Unsupported system: $systemName")
                return null
            }
        }
    }
}
