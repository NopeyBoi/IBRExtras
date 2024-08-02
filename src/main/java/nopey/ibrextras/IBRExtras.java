package nopey.ibrextras;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.vehicle.BoatEntity;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;
import nopey.ibrextras.hud.HudRenderer;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IBRExtras implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ibrextras");
	public static HudRenderer hudRenderer;

	@Override
	public void onInitializeClient() {
		MinecraftClient client = MinecraftClient.getInstance();
		hudRenderer = new HudRenderer();

		Data.keyEnable = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"nopey.ibrextras.key.enable",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_K,
				"nopey.ibrextras.category"
		));

		ClientLifecycleEvents.CLIENT_STARTED.register(sender -> Config.load());

		ClientTickEvents.END_WORLD_TICK.register(world -> {
			if (client.player == null) return;
			if (client.player.getVehicle() instanceof BoatEntity) {
				BoatEntity boat = (BoatEntity) client.player.getVehicle();
				Data.isRidingBoat = true;
				hudRenderer.update(client, boat);
			} else Data.isRidingBoat = false;
		});

		ClientTickEvents.END_CLIENT_TICK.register(c -> {
			while (Data.keyEnable.wasPressed()) Data.enabled = !Data.enabled;
		});

		HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> hudRenderer.render(drawContext, tickDelta)));

		WorldRenderEvents.END.register(context -> {
			if (client.player.getVehicle() instanceof BoatEntity) {
				BoatEntity boat = (BoatEntity) client.player.getVehicle();
			}
		});
	}
}