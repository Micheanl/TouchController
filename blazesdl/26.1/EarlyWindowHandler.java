package top.fifthlight.blazesdl;

import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class EarlyWindowHandler implements GraphicsBootstrapper {
    public String name() {
        return "BlazeSDL";
    }

    public void bootstrap(String[] args) {
        if (FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL)) {
            var result = TinyFileDialogs.tinyfd_messageBox(
                    "BlazeSDL Question",
                    """
                            BlazeSDL detected that NeoForge Early Window is enabled.
                            This feature is incompatible with BlazeSDL and will likely cause Minecraft to crash during startup.
                            Disable Early Window in the configuration file now?""",
                    "yesno",
                    "warning",
                    1
            );
            if (result == 1) {
                FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL, false);
            } else {
                TinyFileDialogs.tinyfd_messageBox(
                        "BlazeSDL Message",
                        """
                                Continue with Early Window enabled.
                                NeoForge will now continue startup, and Minecraft is expected to crash.
                                Do not report the crash to BlazeSDL and NeoForge!
                                """,
                        "ok",
                        "error",
                        0
                );
            }
        }
    }
}
