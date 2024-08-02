package nopey.ibrextras.config.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class MainScreen extends Screen {
    private final Screen parent;

    public MainScreen(Screen parent) {
        super(Text.translatable("nopey.ibrextras.config.mainscreen.title"));
        this.parent = parent;
    }

    public MinecraftClient client = MinecraftClient.getInstance();

    public ButtonWidget btnEnabled;
    public ButtonWidget btnPositioning;
    public ButtonWidget btnBack;

    @Override
    protected void init() {
        btnEnabled = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.enabled")
                        .append(Data.enabled ? Text.translatable("nopey.ibrextras.on").formatted(Formatting.GREEN) : Text.translatable("nopey.ibrextras.off").formatted(Formatting.RED)), button -> {
                    Data.enabled = !Data.enabled;
                    button.setMessage(Text.translatable("nopey.ibrextras.button.enabled")
                            .append(Data.enabled ? Text.translatable("nopey.ibrextras.on").formatted(Formatting.GREEN) : Text.translatable("nopey.ibrextras.off").formatted(Formatting.RED)));
                    Config.save();})
                .dimensions(width / 2 - 205, 50, 200, 20)
                .tooltip(Tooltip.of(Text.translatable("nopey.ibrextras.tooltip.enabled")))
                .build();

        btnPositioning = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.positioning"), button -> {
                    Screen screen = new HudScreen(this);
                    if (client.world != null) client.setScreen(screen);})
                .dimensions(width / 2 + 5, 50, 200, 20)
                .tooltip(Tooltip.of(Text.translatable("nopey.ibrextras.tooltip.positioning")))
                .build();

        btnBack = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.back"), button -> {
                    client.setScreen(parent);})
                .dimensions(width / 2 - 100, height - 70, 200, 20)
                .build();

        addDrawableChild(btnEnabled);
        addDrawableChild(btnPositioning);
        addDrawableChild(btnBack);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(client.textRenderer, this.title, width / 2, 20, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
