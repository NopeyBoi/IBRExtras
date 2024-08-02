package nopey.ibrextras.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;
import nopey.ibrextras.objects.HudElement;

public class HudScreen extends Screen {
    private final Screen parent;

    protected HudScreen(Screen parent) {
        super(Text.translatable("nopey.ibrextras.config.hudscreen.title"));
        this.parent = parent;
        client = MinecraftClient.getInstance();
    }

    public MinecraftClient client;

    public ButtonWidget btnAdd;
    public ButtonWidget btnAddSpecial;

    @Override
    protected void init() {
        btnAdd = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.addcustom"), button -> client.setScreen(new ElementScreen(this)))
                .dimensions(width / 2 - 100, height / 2 - 15, 200, 20)
                .build();

        btnAddSpecial = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.addspecial"), button -> client.setScreen(new SpecialElementScreen(this)))
                .dimensions(width / 2 - 100, height / 2 + 15, 200, 20)
                .build();

        addDrawableChild(btnAdd);
        addDrawableChild(btnAddSpecial);

        for (int i = 0; i < Data.hudElements.size(); i++) {
            addDrawableChild(Data.hudElements.get(i));
            addDrawableChild(Data.hudElements.get(i).btnDelete);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(client.textRenderer, this.title, width / 2, 20, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        client.setScreen(parent);
        Config.save();
    }

    public void addChild(HudElement hudElement) {
        addDrawableChild(hudElement);
    }
}
