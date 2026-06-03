#include "classpath_plugin.h"

#include <filesystem>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>

#ifdef _WIN32
constexpr std::string_view classpathSeparator = ";";
#else
constexpr std::string_view classpathSeparator = ":";
#endif

template <class Output>
static void split(const std::string_view& str, const std::string_view& delim, Output result) {
    size_t pos = 0;
    size_t found;
    while ((found = str.find(delim, pos)) != std::string::npos) {
        *result++ = std::string(str.substr(pos, found - pos));
        pos = found + delim.length();
    }
    if (pos < str.length()) {
        *result++ = std::string(str.substr(pos, str.length()));
    }
}

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

bool ClasspathPlugin::process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) {
    constexpr std::string_view classPathPrefix = "-Ddev.launch.classPath=";
    if (arg == "-Ddev.launch.legacyFml=true") {
        legacy_fml = true;
        return true;
    } else if (arg == "-classpath" && actx.has_next()) {
        auto value = actx.read();
        std::vector<std::string> paths;
        split(value, classpathSeparator, std::back_inserter(paths));
        std::transform(paths.begin(), paths.end(), std::back_inserter(classpath),
                       [](std::string& path) { return std::filesystem::canonical(path); });
        return true;
    } else if (arg.starts_with(classPathPrefix)) {
        auto value = arg.substr(classPathPrefix.size());
        legacy_classpath.push_back(std::filesystem::canonical(pctx.resolve_runfile(value)));
        return true;
    }
    return false;
}

void ClasspathPlugin::finalize(PluginContext& pctx) {
    if (classpath.empty()) {
        return;
    }
    if (legacy_fml) {
        std::ofstream stream;
        stream.exceptions(std::ios::failbit | std::ios::badbit);
        stream.open("legacyClasspath.txt");
        for (auto& path : classpath) {
            stream << path << std::endl;
        }
        pctx.append_jvm_flag("-DlegacyClassPath.file=legacyClasspath.txt");
        pctx.append_jvm_flag("-Djava.class.path=" +
                             join(legacy_classpath.begin(), legacy_classpath.end(), classpathSeparator));
    } else {
        pctx.append_jvm_flag("-Djava.class.path=" + join(classpath.begin(), classpath.end(), classpathSeparator));
    }
}
