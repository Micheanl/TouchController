package top.fifthlight.combine.backend.minecraft.render.v1_21_1.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Accessor("bufferSource")
    MultiBufferSource.BufferSource getBufferSource();

    @Accessor("sprites")
    GuiSpriteManager getSprites();
}
