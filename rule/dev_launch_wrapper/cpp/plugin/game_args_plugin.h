#pragma once

#include <string>

#include "../context.h"
#include "../plugin.h"

class GameArgsPlugin : public Plugin {
   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
    void finalize(PluginContext& pctx) override;
};
