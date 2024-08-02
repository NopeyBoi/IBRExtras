package nopey.ibrextras.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;
import nopey.ibrextras.objects.HudElement;

public class ElementScreen extends Screen {
    private final HudScreen parent;

    protected ElementScreen(HudScreen parent) {
        super(Text.translatable("nopey.ibrextras.config.elementscreen.title"));
        this.parent = parent;
        client = MinecraftClient.getInstance();
    }

    private MinecraftClient client;

    public TextFieldWidget inputName;
    public TextFieldWidget inputColorA;
    public TextFieldWidget inputColorR;
    public TextFieldWidget inputColorG;
    public TextFieldWidget inputColorB;
    public TextFieldWidget inputScale;
    public TextWidget txtScale;
    public TextWidget txtError;

    public ButtonWidget btnTextInfo;
    public ButtonWidget btnAdd;

    @Override
    protected void init() {
        inputName = new TextFieldWidget(client.textRenderer, width / 2 - 100, height / 2 - 25, 175, 20, Text.empty());
        inputName.setText("Custom Text");

        inputColorA = new TextFieldWidget(client.textRenderer, width / 2 - 100, height / 2, 40, 20, Text.empty());
        inputColorA.setEditableColor(0xFFAAAAAA);
        inputColorA.setText("255");

        inputColorR = new TextFieldWidget(client.textRenderer, width / 2 - 55, height / 2, 40, 20, Text.empty());
        inputColorR.setEditableColor(0xFFFF0000);
        inputColorR.setText("255");

        inputColorG = new TextFieldWidget(client.textRenderer, width / 2 - 10, height / 2, 40, 20, Text.empty());
        inputColorG.setEditableColor(0xFF00FF00);
        inputColorG.setText("255");

        inputColorB = new TextFieldWidget(client.textRenderer, width / 2 + 35, height / 2, 40, 20, Text.empty());
        inputColorB.setEditableColor(0xFF0000FF);
        inputColorB.setText("255");

        inputScale = new TextFieldWidget(client.textRenderer, width / 2 - 55, height / 2 + 25, 40, 20, Text.empty());
        inputScale.setText("1.0");

        txtScale = new TextWidget(width / 2 - 100, height / 2 + 25, 40, 20, Text.translatable("Scale"), client.textRenderer);
        txtScale.alignCenter();

        txtError = new TextWidget(width / 2 - 100, height / 2 + 50, 200, 20, Text.empty(), client.textRenderer);
        txtError.alignCenter();
        txtError.setTextColor(0xFFFF0000);

        btnTextInfo = ButtonWidget.builder(Text.translatable("?"), button -> {})
                .tooltip(Tooltip.of(Text.translatable("nopey.ibrextras.tooltip.textinfo")))
                .dimensions(width / 2 + 80, height / 2 - 25, 20, 20)
                .build();

        btnAdd = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.add"), button -> {
                    if (isNumeric()) {
                        HudElement hud = new HudElement(Text.literal(inputName.getText()), 50, 50, getColor(), getScale());
                        Data.hudElements.add(hud);
                        this.parent.addChild(hud);
                        this.close();
                        Config.save();
                    }
                })
                .dimensions(width / 2 - 10, height / 2 + 25, 110, 20)
                .build();

        addDrawableChild(inputName);
        addDrawableChild(inputColorA);
        addDrawableChild(inputColorR);
        addDrawableChild(inputColorG);
        addDrawableChild(inputColorB);
        addDrawableChild(inputScale);
        addDrawableChild(txtScale);
        addDrawableChild(txtError);
        addDrawableChild(btnTextInfo);
        addDrawableChild(btnAdd);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(client.textRenderer, this.title, width / 2, 20, 0xFFFFFFFF);
        try {
            context.fill(width / 2 + 80, height / 2, width / 2 + 100, height / 2 + 20, getColor());
        } catch (NumberFormatException e) {
            context.fill(width / 2 + 80, height / 2, width / 2 + 100, height / 2 + 20, 0xFF000000);
        }
        context.drawHorizontalLine(width / 2 + 79, width / 2 + 100, height / 2 - 1, 0xFFFFFFFF);
        context.drawVerticalLine(width / 2 + 79, height / 2 - 1, height / 2 + 20, 0xFFFFFFFF);
        context.drawHorizontalLine(width / 2 + 79, width / 2 + 100, height / 2 + 20, 0xFFFFFFFF);
        context.drawVerticalLine(width / 2 + 100, height / 2 - 1, height / 2 + 20, 0xFFFFFFFF);

        if (!isNumeric()) txtError.setMessage(Text.translatable("The colors and scale have to be numbers!"));
        else txtError.setMessage(Text.empty());
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    public int getColor() {
        try {
            int alpha = Integer.parseInt(inputColorA.getText());
            int red = Integer.parseInt(inputColorR.getText());
            int green = Integer.parseInt(inputColorG.getText());
            int blue = Integer.parseInt(inputColorB.getText());
            return (int) Long.parseLong(String.format("%02X%02X%02X%02X", alpha, red, green, blue), 16);
        } catch (Exception e) {}
        return 0x00000000;
    }

    public float getScale() {
        try {
            return Float.parseFloat(inputScale.getText());
        } catch (Exception e) {}
        return 1.0f;
    }

    public boolean isNumeric() {
        boolean a = inputColorA.getText().matches("-?\\d+(\\.\\d+)?");
        boolean r = inputColorR.getText().matches("-?\\d+(\\.\\d+)?");
        boolean g = inputColorG.getText().matches("-?\\d+(\\.\\d+)?");
        boolean b = inputColorB.getText().matches("-?\\d+(\\.\\d+)?");
        boolean s = inputScale.getText().matches("-?\\d+(\\.\\d+)?");
        return a && r && g && b && s;
    }
}
