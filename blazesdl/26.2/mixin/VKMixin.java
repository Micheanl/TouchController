package top.fifthlight.blazesdl.mixin;

import org.jspecify.annotations.NonNull;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLVulkan;
import org.lwjgl.system.FunctionProvider;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.vulkan.VK;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.fifthlight.blazesdl.SDLError;

import java.nio.ByteBuffer;

@Mixin(VK.class)
public abstract class VKMixin {
    @Shadow
    public static void create(FunctionProvider functionProvider) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(method = "create()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/vulkan/VK;create(Lorg/lwjgl/system/FunctionProvider;)V"))
    private static void onCreate(FunctionProvider functionProvider) {
        if (!(functionProvider instanceof SharedLibrary library)) {
            throw new IllegalArgumentException("functionProvider should be SharedLibrary");
        }

        if (SDLInit.SDL_WasInit(SDLInit.SDL_INIT_VIDEO) == 0) {
            SDLInit.SDL_Init(SDLInit.SDL_INIT_VIDEO);
        }

        if (!SDLVulkan.SDL_Vulkan_LoadLibrary(library.getPath())) {
            throw SDLError.handleError("SDL_Vulkan_LoadLibrary");
        }
        // Type: vkGetInstanceProcAddr(VkInstance instance, const char* pName)
        var vkGetInstanceProcAddr = SDLVulkan.SDL_Vulkan_GetVkGetInstanceProcAddr();
        if (vkGetInstanceProcAddr == 0L) {
            throw SDLError.handleError("SDL_Vulkan_GetVkGetInstanceProcAddr");
        }

        create(new FunctionProvider() {
            @Override
            public long getFunctionAddress(CharSequence name) {
                if (name.equals("vkGetInstanceProcAddr")) {
                    return vkGetInstanceProcAddr;
                }
                return 0L;
            }

            @Override
            public long getFunctionAddress(@NonNull ByteBuffer name) {
                return getFunctionAddress(name.asCharBuffer().toString());
            }
        });
    }
}
