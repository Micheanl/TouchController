package top.fifthlight.blazesdl;

import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.neoforgespi.earlywindow.GraphicsBootstrapper;
import org.lwjgl.sdl.SDLMessageBox;
import org.lwjgl.sdl.SDL_MessageBoxButtonData;
import org.lwjgl.sdl.SDL_MessageBoxData;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class EarlyWindowHandler implements GraphicsBootstrapper {
    public String name() {
        return "BlazeSDL";
    }

    public void bootstrap(String[] args) {
        if (FMLConfig.getBoolConfigValue(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL)) {
            try (var stack = MemoryStack.stackPush()) {
                var buttons = SDL_MessageBoxButtonData.calloc(2, stack);
                buttons.get(0)
                        .flags(SDLMessageBox.SDL_MESSAGEBOX_BUTTON_ESCAPEKEY_DEFAULT)
                        .buttonID(0)
                        .text(stack.UTF8("Cancel"));
                buttons.get(1)
                        .flags(SDLMessageBox.SDL_MESSAGEBOX_BUTTON_RETURNKEY_DEFAULT)
                        .buttonID(1)
                        .text(stack.UTF8("Disable"));

                var data = SDL_MessageBoxData.calloc(stack)
                        .flags(SDLMessageBox.SDL_MESSAGEBOX_WARNING)
                        .window(0L)
                        .title(stack.UTF8("BlazeSDL Question"))
                        .message(stack.UTF8("""
                                BlazeSDL detected that NeoForge Early Window is enabled.
                                This feature is incompatible with BlazeSDL and will likely cause Minecraft to crash during startup.
                                Disable Early Window in the configuration file now?"""))
                        .buttons(buttons);

                var buttonId = stack.mallocInt(1);
                if (SDLMessageBox.SDL_ShowMessageBox(data, buttonId) && buttonId.get(0) == 1) {
                    FMLConfig.updateConfig(FMLConfig.ConfigValue.EARLY_WINDOW_CONTROL, false);
                } else {
                    SDLMessageBox.SDL_ShowSimpleMessageBox(
                            SDLMessageBox.SDL_MESSAGEBOX_ERROR,
                            stack.UTF8("BlazeSDL Message"),
                            stack.UTF8("""
                                    Continue with Early Window enabled.
                                    NeoForge will now continue startup, and Minecraft is expected to crash.
                                    Do not report the crash to BlazeSDL and NeoForge!
                                    """),
                            0L
                    );
                }
            }
        }
    }
}
