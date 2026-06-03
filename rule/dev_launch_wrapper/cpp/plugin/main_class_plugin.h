#pragma once
#include "../context.h"
#include "../plugin.h"

class MainClassPlugin : public Plugin {
   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
};
