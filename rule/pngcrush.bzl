"""Rules for optimizing PNG files with pngcrush."""

def pngcrush_action(actions, executable, src, output):
    """Compress a single PNG file using pngcrush.

    Args:
        actions: The actions factory.
        executable: The pngcrush executable.
        src: The input PNG file.
        output: The declared output PNG file.

    Returns:
        The output file.
    """
    args = actions.args()
    args.add(src)
    args.add(output)

    actions.run(
        inputs = [src],
        outputs = [output],
        executable = executable,
        arguments = [args],
        mnemonic = "Pngcrush",
    )

    return output

def _pngcrush_rule_impl(ctx):
    outputs = []
    for src in ctx.files.srcs:
        output = ctx.actions.declare_file(src.basename, sibling = src)
        pngcrush_action(ctx.actions, ctx.executable._pngcrush, src, output)
        outputs.append(output)
    return [DefaultInfo(files = depset(outputs))]

pngcrush_optimize = rule(
    implementation = _pngcrush_rule_impl,
    attrs = {
        "srcs": attr.label_list(
            mandatory = True,
            allow_files = [".png"],
            doc = "Input PNG files to compress",
        ),
        "_pngcrush": attr.label(
            default = "@pngcrush//:pngcrush",
            executable = True,
            cfg = "exec",
        ),
    },
    doc = "Optimize PNG files using pngcrush.",
)
