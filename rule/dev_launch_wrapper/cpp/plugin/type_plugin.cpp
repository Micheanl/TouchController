#include "type_plugin.h"

#include <fstream>
#include <string_view>

static void write_file(const std::string& path, const std::string_view& content) {
    std::ofstream stream;
    stream.exceptions(std::ios::failbit | std::ios::badbit);
    stream.open(path);
    stream << content;
}

bool TypePlugin::process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) {
    constexpr std::string_view prefix = "-Ddev.launch.type=";
    if (arg.rfind(prefix, 0) == 0) {
        type = arg.substr(prefix.size());
        return true;
    }
    return false;
}

void TypePlugin::finalize(PluginContext& pctx) {
    if (type == "server") {
        if (!std::filesystem::exists("server.properties")) {
            write_file("server.properties", "online-mode=false\n");
        }
        write_file("eula.txt", "eula=true\n");
        pctx.append_program_arg("--nogui");
    } else if (type.empty() || type == "client") {
        write_file("allowed_symlinks.txt", "[regex].*\n");
    }
}
