package nopey.ibrextras.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nopey.ibrextras.config.Data;

public class HudRenderer {
    public void render(DrawContext context, float tickDelta) {
        Data.displayedSpeedVal = MathHelper.lerp(tickDelta, Data.displayedSpeedVal, Data.speedVal);
        Data.displayedInertiaVal = MathHelper.lerp(tickDelta, Data.displayedInertiaVal, Data.oldInertiaVal - Data.inertiaVal);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (Data.isRidingBoat && Data.enabled) {
            for (int i = 0; i < Data.hudElements.size(); i++) {
                Data.hudElements.get(i).render(context, tickDelta);
            }
        }

        RenderSystem.disableBlend();
    }

    public void update(MinecraftClient client, BoatEntity boat) {
        Vec3d velocity = boat.getVelocity().multiply(1, 0, 1);
        Data.oldSpeedVal = Data.speedVal;
        Data.speedVal = velocity.length() * 20d;

        Data.gVal = (Data.speedVal - Data.oldSpeedVal) * 2.040816327d;

        Data.angleVal = Math.toDegrees(Math.acos(velocity.dotProduct(boat.getRotationVector()) / velocity.length() * boat.getRotationVector().length()));
        if (Double.isNaN(Data.angleVal)) Data.angleVal = 0;

        Data.pingVal = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid()).getLatency();

        Data.fpsVal = client.getCurrentFps();

        Data.oldInertiaVal = Data.inertiaVal;
        Data.inertiaVal = boat.getRotationClient().length() * 20d;
    }
}
