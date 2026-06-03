#include "module_path_plugin.h"

#include <filesystem>
#include <sstream>
#include <string_view>

#ifdef _WIN32
constexpr std::string_view modulePathSeparator = ";";
#else
constexpr std::string_view modulePathSeparator = ":";
#endif

template <class Iter>
static std::string join(Iter begin, Iter end, const std::string_view& delim) {
    std::ostringstream result;
    while (begin != end) {
        result << *begin++;
        if (begin != end) {
            result << delim;
        }
    }
    return result.str();
}

bool ModulePathPlugin::process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) {
    if (arg != "START_MODULEPATH") {
        return false;
    }

    while (actx.has_next()) {
        auto path = actx.read();
        if (path == "END_MODULEPATH") {
            break;
        }
        paths.push_back(std::filesystem::canonical(pctx.resolve_runfile(path)));
    }
    return true;
}

void ModulePathPlugin::finalize(PluginContext& pctx) {
    if (paths.empty()) {
        return;
    }

    pctx.append_jvm_flag("--module-path=" + join(paths.begin(), paths.end(), modulePathSeparator));
}
