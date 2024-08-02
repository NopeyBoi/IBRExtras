package nopey.ibrextras.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudMixin {
    @Accessor
    Text getOverlayMessage();
    @Accessor("overlayRemaining")
    public void setOverlayRemaining(int overlayRemaining);
}

