#pragma once

#include <string>

#include "../context.h"
#include "../plugin.h"

class FMLPlugin : public Plugin {
    bool has_fml = false;

   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
    void finalize(PluginContext& pctx) override;
};
