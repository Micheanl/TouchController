"""Rules for creating unified multi-version multi-loader JAR files."""

load("@rules_java//java/common:java_info.bzl", "JavaInfo")

def _unified_jij_impl(ctx):
    output_jar = ctx.actions.declare_file(ctx.label.name + ".jar")

    args = ctx.actions.args()
    args.add("--plugin", "manifest")
    args.add("--plugin", "services")
    args.add("--plugin", "resource")
    args.add("--plugin", "jar_in_jar")
    args.add("--plugin", "fabric_jij")
    args.add("--plugin", "neoforge_jij")
    args.add(output_jar)

    input_files = []
    template_files = []

    input = ctx.file.input
    args.add(input)
    input_files.append(input)

    args.add("--jij-base-path")
    args.add("META-INF/jarjar/")

    dep_files = []
    for target, key in ctx.attr.deps.items():
        (group_id, artifact_id, version, mod_type) = key.split(":", 3)
        id = "%s_%s" % (group_id.replace(".", "_"), artifact_id.replace("-", "_"))
        jar = target.files.to_list()[0]
        dep_files.append(jar)
        args.add("--jar-in-jar")
        args.add(id)
        args.add(jar)

        args.add("--jij-fabric")
        args.add(id)
        args.add("=" if mod_type == "=" else version)

        args.add("--jij-neoforge")
        args.add(id)
        args.add(key)

    resource_files = []
    for resource, strip in ctx.attr.resources.items():
        args.add("--resource-strip")
        args.add(strip)
        for file in resource.files.to_list():
            resource_files.append(file)
            args.add("--resource")
            args.add(file)

    args.use_param_file("@%s")
    args.set_param_file_format("multiline")

    ctx.actions.run(
        inputs = depset(input_files + dep_files + template_files + resource_files),
        outputs = [output_jar],
        executable = ctx.executable._merger,
        arguments = [args],
        progress_message = "Creating unified JAR %s" % ctx.label.name,
    )

    return [
        JavaInfo(
            output_jar = output_jar,
            compile_jar = output_jar,
        ),
        DefaultInfo(files = depset([output_jar])),
    ]

unified_jij = rule(
    implementation = _unified_jij_impl,
    attrs = {
        "input": attr.label(
            allow_single_file = [".jar"],
            doc = "Input JAR",
        ),
        "deps": attr.label_keyed_string_dict(
            mandatory = True,
            allow_files = [".jar"],
            doc = "JARs to embed: label -> groupId:artifactId:version:fmlModType",
        ),
        "resources": attr.label_keyed_string_dict(
            mandatory = False,
            allow_files = True,
            default = {},
            doc = "Resource files to include; key=label, value=strip prefix ('.' for basename only)",
        ),
        "_merger": attr.label(
            default = Label("@//rule/mergetool:merger"),
            executable = True,
            cfg = "exec",
        ),
    },
    doc = "Create NeoForge & Fabric JiJ JAR",
)
