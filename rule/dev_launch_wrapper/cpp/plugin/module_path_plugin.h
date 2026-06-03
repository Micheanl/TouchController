#pragma once

#include <string>
#include <vector>

#include "../context.h"
#include "../plugin.h"

class ModulePathPlugin : public Plugin {
    std::vector<std::string> paths;

   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
    void finalize(PluginContext& pctx) override;
};
