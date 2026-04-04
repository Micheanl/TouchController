"""Rules for generating mod information JAR files."""

load("@bazel_skylib//rules:expand_template.bzl", "expand_template")
load("//rule:merge_library.bzl", "java_merge_library")

def _mod_info_jar_impl(name, visibility, fabric, neoforge, resource_strip_prefix, substitutions):
    resources = []
    if fabric:
        fabric_name = name + "_fabric_expanded"
        expand_template(
            name = fabric_name,
            template = fabric,
            substitutions = substitutions,
            out = "%s/fabric.mod.json" % name,
        )
        resources.append(fabric_name)
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
        resources.append(neoforge_name)

    java_merge_library(
        name = name,
        visibility = visibility,
        resources = resources,
        resource_strip_prefix = resource_strip_prefix + "/" + name,
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
        "resource_strip_prefix": attr.string(
            mandatory = True,
        ),
        "substitutions": attr.string_dict(
            mandatory = True,
            configurable = False,
            doc = "A dictionary mapping strings to their substitutions.",
        ),
    },
    implementation = _mod_info_jar_impl,
)
