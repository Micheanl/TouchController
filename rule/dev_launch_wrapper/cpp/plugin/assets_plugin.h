#pragma once

#include <filesystem>
#include <string>

#include "../context.h"
#include "../plugin.h"

class AssetsPlugin : public Plugin {
    std::filesystem::path assets_version;

   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
    void finalize(PluginContext& pctx) override;
};
