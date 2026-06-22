load("@rules_kotlin//kotlin:core.bzl", "kt_javac_options", "kt_kotlinc_options")

_KOTLIN_JVM_TARGET = {
    8: "1.8",
    9: "9",
    10: "10",
    11: "11",
    12: "12",
    13: "13",
    14: "14",
    15: "15",
    16: "16",
    17: "17",
    18: "18",
    19: "19",
    20: "20",
    21: "21",
    22: "22",
    23: "23",
    24: "24",
    25: "25",
}

_JAVA_RELEASE = {
    8: "8",
    11: "11",
    17: "17",
    21: "21",
    25: "25",
}

def _kt_jvm_kotlinc_options_set_impl(
        name,
        visibility,
        versions,
        x_lambdas = "",
        x_optin = [],
        x_jdk_release = True,
        **kwargs):
    """Generates `kt_kotlinc_options` targets named `<name>_jvm_<ver>` for each Java version."""
    for ver in versions:
        jvm_target = _KOTLIN_JVM_TARGET[ver]
        args = dict(kwargs)
        args["name"] = "%s_jvm_%d" % (name, ver)
        args["jvm_target"] = jvm_target
        args["visibility"] = visibility
        if x_jdk_release:
            args["x_jdk_release"] = jvm_target
        if x_lambdas:
            args["x_lambdas"] = x_lambdas
        if x_optin:
            args["x_optin"] = x_optin
        kt_kotlinc_options(**args)

kt_jvm_kotlinc_options_set = macro(
    implementation = _kt_jvm_kotlinc_options_set_impl,
    attrs = {
        "versions": attr.int_list(mandatory = True, configurable = False),
        "x_lambdas": attr.string(default = "", configurable = False),
        "x_optin": attr.string_list(default = [], configurable = False),
        "x_jdk_release": attr.bool(default = True, configurable = False),
    },
)

def _kt_jvm_javac_options_set_impl(name, visibility, versions, **kwargs):
    for ver in versions:
        args = dict(kwargs)
        args["name"] = "%s_jvm_%d" % (name, ver)
        args["release"] = _JAVA_RELEASE[ver]
        args["visibility"] = visibility
        kt_javac_options(**args)

kt_jvm_javac_options_set = macro(
    implementation = _kt_jvm_javac_options_set_impl,
    attrs = {
        "versions": attr.int_list(mandatory = True, configurable = False),
    },
)
