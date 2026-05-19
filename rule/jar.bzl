"""Rules for creating JAR file."""

load("@rules_java//java:defs.bzl", "JavaInfo")
load("//rule:merge_library.bzl", "MergeLibraryInfo")

def _resource_path_entry_to_arg(entry):
    (file, path) = entry
    return ["--resource-path", path, file.path]

def _impl(ctx):
    output_jar = ctx.actions.declare_file(ctx.label.name + ".jar")

    strip_prefix = ctx.attr.resource_strip_prefix

    args = ctx.actions.args()
    args.add("--plugin", "resource")
    args.add(output_jar)

    args.add("--resource-strip")
    args.add(strip_prefix)

    if ctx.attr.resource_prefix:
        args.add("--resource-prefix")
        args.add(ctx.attr.resource_prefix)

    for from_name, to_name in ctx.attr.resource_rename.items():
        args.add("--resource-rename")
        args.add(from_name)
        args.add(to_name)

    input_file_depsets = []
    for label in ctx.attr.resources:
        files = label.files
        input_file_depsets.append(label.files)
        args.add_all(files, before_each = "--resource")

    for (label, path) in ctx.attr.resource_paths.items():
        input_file_depsets.append(label.files)
        args.add_all([(file, path) for file in label.files.to_list()], map_each = _resource_path_entry_to_arg)

    args.use_param_file("@%s", use_always = True)
    args.set_param_file_format("multiline")

    ctx.actions.run(
        inputs = depset(transitive = input_file_depsets),
        outputs = [output_jar],
        executable = ctx.executable._create_jar_executable,
        execution_requirements = {
            "supports-workers": "1",
            "supports-multiplex-workers": "1",
            "supports-multiplex-sandboxing": "1",
            "requires-worker-protocol": "proto",
        },
        arguments = [args],
        progress_message = "Creating JAR %s" % ctx.label.name,
    )

    return [
        JavaInfo(
            output_jar = output_jar,
            compile_jar = output_jar,
        ),
        MergeLibraryInfo(merge_jars = depset([output_jar])),
        DefaultInfo(files = depset([output_jar])),
    ]

jar = rule(
    implementation = _impl,
    provides = [JavaInfo, DefaultInfo, MergeLibraryInfo],
    attrs = {
        "resources": attr.label_list(
            mandatory = False,
            allow_files = True,
            default = [],
            doc = "Resource files to include in JAR",
        ),
        "resource_paths": attr.label_keyed_string_dict(
            mandatory = False,
            allow_files = True,
            default = {},
            doc = "Resource files to include in JAR, as final paths",
        ),
        "resource_strip_prefix": attr.string(
            mandatory = False,
            default = ".",
            doc = "Strip prefix from resource paths; '.' means keep basename only",
        ),
        "resource_rename": attr.string_dict(
            mandatory = False,
            default = {},
            doc = "Map file basename to new basename",
        ),
        "resource_prefix": attr.string(
            mandatory = False,
            default = "",
            doc = "Prefix to add to resource paths",
        ),
        "_create_jar_executable": attr.label(
            default = Label("@//rule/mergetool:merger"),
            executable = True,
            cfg = "exec",
        ),
    },
    doc = "Create a JAR file from files",
)
