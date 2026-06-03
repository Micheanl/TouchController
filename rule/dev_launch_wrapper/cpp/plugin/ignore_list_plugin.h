#pragma once

#include <string>

#include "../context.h"
#include "../plugin.h"

class IgnoreListPlugin : public Plugin {
   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
};
