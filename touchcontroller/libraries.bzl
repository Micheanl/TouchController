load("@rules_jvm_external//:defs.bzl", "artifact")

def _library(coordinate):
    [group, artifact_id, version] = coordinate.split(":")
    return struct(
        name = (group + "_" + artifact_id).replace(".", "_").replace("-", "_").lower(),
        label = artifact(coordinate),
        version = version,
    )

_libraries = [
    _library("androidx.compose.runtime:runtime-saveable-desktop:1.10.0"),
    _library("androidx.savedstate:savedstate-desktop:1.3.3"),
    _library("androidx.savedstate:savedstate-compose-desktop:1.3.3"),
    _library("androidx.lifecycle:lifecycle-common-jvm:2.9.4"),
    _library("androidx.lifecycle:lifecycle-runtime-compose-desktop:2.9.4"),
    _library("org.jetbrains.kotlinx:kotlinx-collections-immutable-jvm:0.4.0"),
    _library("cafe.adriel.voyager:voyager-core-desktop:1.1.0-beta03"),
    _library("cafe.adriel.voyager:voyager-navigator-desktop:1.1.0-beta03"),
    _library("cafe.adriel.voyager:voyager-screenmodel-desktop:1.1.0-beta03"),
]

touchcontroller_fabric_libraries = {lib.label: (lib.name + ":" + lib.version) for lib in _libraries}
touchcontroller_unified_deps = {lib.name: lib.label for lib in _libraries}
touchcontroller_unified_neoforge = {lib.name: ["common"] for lib in _libraries}
touchcontroller_unified_fabric = {lib.name: lib.version for lib in _libraries}
