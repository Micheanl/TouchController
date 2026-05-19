"""Rules for generating mod information JAR files."""

load("@bazel_skylib//rules:expand_template.bzl", "expand_template")
load("//rule:jar.bzl", "jar")

def _mod_info_jar_impl(name, visibility, fabric, neoforge, substitutions):
    resource_paths = {}
    if fabric:
        fabric_name = name + "_fabric_expanded"
        expand_template(
            name = fabric_name,
            template = fabric,
            substitutions = substitutions,
            out = "%s/fabric.mod.json" % name,
        )
        resource_paths[":" + fabric_name] = "fabric.mod.json"
    if neoforge:
        neoforge_name = name + "_neoforge_expanded"
        mod_id = substitutions.get("${mod_id}", None)
        if mod_id:
            substitutions = substitutions | {"${mod_id}": mod_id.replace("-", "_")}
        expand_template(
            name = neoforge_name,
            template = neoforge,
            substitutions = substitutions,
            out = "%s/neoforge.mods.toml" % name,
        )
        resource_paths[":" + neoforge_name] = "META-INF/neoforge.mods.toml"

    jar(
        name = name,
        visibility = visibility,
        resource_paths = resource_paths,
    )

mod_info_jar = macro(
    attrs = {
        "fabric": attr.label(
            mandatory = False,
            allow_single_file = [".json"],
            doc = "Input fabric.mod.json file",
        ),
        "neoforge": attr.label(
            mandatory = False,
            allow_single_file = [".toml"],
            doc = "Input neoforge.mods.toml file",
        ),
        "substitutions": attr.string_dict(
            mandatory = True,
            configurable = False,
            doc = "A dictionary mapping strings to their substitutions.",
        ),
    },
    implementation = _mod_info_jar_impl,
)
