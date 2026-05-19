load("//blazerod:properties.bzl", "blazerod_version", "home_page", "issue_tracker", "license", "sources_page")
load("//rule:mod_info_jar.bzl", "mod_info_jar")

def _model_info_jar_impl(name, visibility, substitutions):
    predefined_substitutions = {
        "${version}": blazerod_version,
        "${license}": license,
        "${home_page}": home_page,
        "${sources_page}": sources_page,
        "${issue_tracker}": issue_tracker,
    }
    predefined_substitutions.update(substitutions)
    mod_info_jar(
        name = name,
        visibility = visibility,
        fabric = "//blazerod/model:resources/fabric.mod.json",
        neoforge = "//blazerod/model:resources/META-INF/neoforge.mods.toml",
        substitutions = predefined_substitutions,
    )

model_info_jar = macro(
    attrs = {
        "substitutions": attr.string_dict(
            mandatory = True,
            configurable = False,
            doc = "A dictionary mapping strings to their substitutions.",
        ),
    },
    implementation = _model_info_jar_impl,
)
