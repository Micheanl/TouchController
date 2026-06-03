#pragma once

#include <string>
#include <vector>

#include "../context.h"
#include "../plugin.h"

class ClasspathPlugin : public Plugin {
    std::vector<std::string> classpath;
    std::vector<std::string> legacy_classpath;
    bool legacy_fml = false;

   public:
    bool process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) override;
    void finalize(PluginContext& ctx) override;
};
