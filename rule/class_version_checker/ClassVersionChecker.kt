package top.fifthlight.classversionchecker

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import java.io.DataInputStream
import java.io.InputStream
import java.nio.file.Path
import java.util.jar.JarInputStream
import kotlin.io.path.inputStream
import kotlin.system.exitProcess

class ClassVersionChecker : CliktCommand() {
    init {
        context {
            exitProcess = ::exitProcess
        }
    }

    val classMajor: Int by option().int().required().help("Maximum class major version")
    val files: List<Path> by argument().path().multiple(required = true).help("JARs to process")

    override fun run() {
        var totalViolations = 0
        for (file in files) {
            totalViolations += checkJar(file, classMajor)
        }
        if (totalViolations > 0) {
            currentContext.exitProcess(1)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = ClassVersionChecker().main(args)
    }
}

private const val MAGIC_CLASS_FILE: Int = 0xCAFEBABE.toInt()

private fun readMajorVersion(stream: InputStream): Int? = DataInputStream(stream).let { stream ->
    if (stream.readInt() != MAGIC_CLASS_FILE) {
        null
    } else {
        stream.readUnsignedShort() // Minor version
        val majorVersion = stream.readUnsignedShort()
        majorVersion
    }
}

private fun checkJar(jarPath: Path, maxMajor: Int): Int {
    var violations = 0
    val jarName = jarPath.fileName.toString()
    jarPath.inputStream().let(::JarInputStream).use { stream ->
        var entry = stream.nextJarEntry
        while (entry != null) {
            if (!entry.isDirectory && entry.name.endsWith(".class")) {
                val major = readMajorVersion(stream) ?: error("Bad class file: $jarName:${entry.name}")
                if (major > maxMajor) {
                    println("$jarName:${entry.name}: major version $major exceeds maximum $maxMajor")
                    violations++
                }
            }
            stream.closeEntry()
            entry = stream.nextJarEntry
        }
    }
    return violations
}

