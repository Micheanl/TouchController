load("@rules_java//java/common:java_info.bzl", "JavaInfo")

def _jar_import_impl(ctx):
    classes_jar = ctx.file.jar
    runtime_deps = [dep[JavaInfo] for dep in ctx.attr.runtime_deps]
    return [
        JavaInfo(
            output_jar = classes_jar,
            compile_jar = classes_jar,
            runtime_deps = runtime_deps,
        ),
        DefaultInfo(files = depset([classes_jar])),
    ]

jar_import = rule(
    implementation = _jar_import_impl,
    attrs = {
        "jar": attr.label(
            allow_single_file = [".jar"],
            mandatory = True,
            doc = "Primary classes JAR",
        ),
        "runtime_deps": attr.label_list(
            providers = [JavaInfo],
            mandatory = False,
            default = [],
            doc = "Runtime dependencies.",
        ),
    },
    doc = "Creates a JavaInfo with a classes JAR and runtime_deps JavaInfo",
)
