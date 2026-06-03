#include "game_args_plugin.h"

#include <filesystem>
#include <string>
#include <string_view>

bool GameArgsPlugin::process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) {
    constexpr std::string_view versionPrefix = "-Ddev.launch.version=";
    if (arg.starts_with(versionPrefix)) {
        pctx.attributes["version"] = arg.substr(versionPrefix.size());
        return true;
    } else {
        return false;
    }
}

void GameArgsPlugin::finalize(PluginContext& pctx) {
    pctx.append_program_args({
        "--gameDir",
        std::filesystem::current_path(),
    });
    auto version = pctx.attributes["version"];
    if (!version.empty()) {
        pctx.append_program_args({"--version", version});
    }
}
