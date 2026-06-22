load("@bazel_lib//lib:paths.bzl", "to_rlocation_path")

def _class_version_test_impl(ctx):
    jars = ctx.files.jars
    if not jars:
        fail("class_version_test 'jars' must not be empty")

    flag = "--class-major"
    value = str(ctx.attr.class_major)

    checker = ctx.executable._checker

    is_windows = ctx.target_platform_has_constraint(
        ctx.attr._windows_constraint[platform_common.ConstraintValueInfo],
    )

    if is_windows:
        test_suffix = "-test.bat"
        template = ctx.file._launcher_bat
    else:
        test_suffix = "-test.sh"
        template = ctx.file._launcher_sh

    jar_rlocations = " ".join(["\"%s\"" % to_rlocation_path(ctx, jar) for jar in jars])

    test_bin = ctx.actions.declare_file(ctx.label.name + test_suffix)
    ctx.actions.expand_template(
        template = template,
        output = test_bin,
        substitutions = {
            "TEMPLATED_checker": to_rlocation_path(ctx, checker),
            "TEMPLATED_flag": flag,
            "TEMPLATED_value": value,
            "TEMPLATED_jars": jar_rlocations,
        },
        is_executable = True,
    )

    runfiles = ctx.runfiles(files = jars)
    runfiles = runfiles.merge_all([
        ctx.attr._checker[DefaultInfo].default_runfiles,
        ctx.attr._bash_runfiles[DefaultInfo].default_runfiles,
    ])

    return DefaultInfo(
        executable = test_bin,
        files = depset([test_bin]),
        runfiles = runfiles,
    )

class_version_test = rule(
    implementation = _class_version_test_impl,
    test = True,
    attrs = {
        "jars": attr.label_list(
            mandatory = True,
            allow_files = [".jar"],
            doc = "JAR files whose .class entries are checked.",
        ),
        "class_major": attr.int(
            mandatory = True,
            doc = "Maximum class-file major version (e.g. 52, 65).",
        ),
        "_checker": attr.label(
            default = Label("//rule/class_version_checker"),
            executable = True,
            cfg = "exec",
        ),
        "_launcher_sh": attr.label(
            default = Label("//rule/class_version_checker:launcher.sh.tpl"),
            allow_single_file = True,
        ),
        "_launcher_bat": attr.label(
            default = Label("//rule/class_version_checker:launcher.bat.tpl"),
            allow_single_file = True,
        ),
        "_bash_runfiles": attr.label(
            default = "@rules_shell//shell/runfiles",
        ),
        "_windows_constraint": attr.label(
            default = "@platforms//os:windows",
        ),
    },
    doc = "Test rule that fails if any .class file in the given JAR(s) exceeds the configured maximum version.",
)
