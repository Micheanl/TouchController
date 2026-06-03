#include "main_class_plugin.h"

bool MainClassPlugin::process_arg(const std::string& arg, PluginContext& pctx, ArgumentContext& actx) {
    if (!arg.empty() && arg[0] != '-' && pctx.main_class.empty()) {
        pctx.main_class = arg;
        return true;
    } else {
        return false;
    }
}
