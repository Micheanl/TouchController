#pragma once

#include <string>

#include "context.h"

class ArgReader;
class DevLaunchContext;

class Plugin {
   public:
    virtual ~Plugin() = default;

    virtual bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) = 0;

    virtual void finalize(PluginContext& pctx) {}
};
