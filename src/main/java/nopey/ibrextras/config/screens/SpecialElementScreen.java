package nopey.ibrextras.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;
import nopey.ibrextras.objects.HudElement;
import nopey.ibrextras.objects.ElementType;

public class SpecialElementScreen extends Screen {
    private final HudScreen parent;

    protected SpecialElementScreen(HudScreen parent) {
        super(Text.translatable("nopey.ibrextras.config.elementscreen.title"));
        this.parent = parent;
        client = MinecraftClient.getInstance();
    }

    private MinecraftClient client;

    public CheckboxWidget chkSpeedometer;
    public CheckboxWidget chkDpad;
    public CheckboxWidget chkPingicon;
    public CheckboxWidget chkTimer;
    public TextFieldWidget inputScale;
    public TextWidget txtScale;
    public TextWidget txtError;
    public ButtonWidget btnAdd;
    public ButtonWidget btnStop;


    @Override
    protected void init() {
        /*
        chkSpeedometer = CheckboxWidget.builder(Text.literal("Add Speedometer"), client.textRenderer)
                .pos(width / 2 - 100, height / 2 - 50)
                .checked(true)
                .build();

        chkDpad = CheckboxWidget.builder(Text.literal("Add D-pad"), client.textRenderer)
                .pos(width / 2 + 5, height / 2 - 50)
                .checked(false)
                .build();

        chkPingicon = CheckboxWidget.builder(Text.literal("Add Pingicon"), client.textRenderer)
                .pos(width / 2 - 100, height / 2 - 25)
                .checked(false)
                .build();

        chkTimer = CheckboxWidget.builder(Text.literal("Add Timer"), client.textRenderer)
                .pos(width / 2 + 5, height / 2 - 25)
                .checked(false)
                .build();
         */

        chkSpeedometer = new CheckboxWidget(width / 2 - 100, height / 2 - 50, 95, 20, Text.literal("Add Speedometer"), true);
        chkDpad = new CheckboxWidget(width / 2 + 5, height / 2 - 50, 95, 20, Text.literal("Add D-pad"), false);
        chkPingicon = new CheckboxWidget(width / 2 - 100, height / 2 - 25, 95, 20, Text.literal("Add Pingicon"), false);
        chkTimer = new CheckboxWidget(width / 2 + 5, height / 2 - 25, 95, 20, Text.literal("Add Timer"), false);

        txtError = new TextWidget(width / 2 - 100, height / 2 + 75, 200, 20, Text.empty(), client.textRenderer);
        txtError.alignCenter();
        txtError.setTextColor(0xFFFF0000);

        inputScale = new TextFieldWidget(client.textRenderer, width / 2 - 55, height / 2 + 25, 40, 20, Text.empty());
        inputScale.setText("1.0");

        txtScale = new TextWidget(width / 2 - 100, height / 2 + 25, 40, 20, Text.translatable("Scale"), client.textRenderer);
        txtScale.alignCenter();

        btnAdd = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.add"), button -> {
                    if (!isManyChecked()) {
                        HudElement hud = getTickedElement();
                        Data.hudElements.add(hud);
                        this.parent.addChild(hud);
                        this.close();
                        Config.save();
                    }
                })
                .dimensions(width / 2 - 10, height / 2 + 25, 110, 20)
                .build();

        btnStop = ButtonWidget.builder(Text.translatable("nopey.ibrextras.button.stop"), button -> this.close())
                .dimensions(width / 2 - 100, height / 2 + 50, 200, 20)
                .build();

        addDrawableChild(chkSpeedometer);
        addDrawableChild(chkDpad);
        addDrawableChild(chkPingicon);
        addDrawableChild(chkTimer);
        addDrawableChild(txtError);
        addDrawableChild(inputScale);
        addDrawableChild(txtScale);
        addDrawableChild(btnAdd);
        addDrawableChild(btnStop);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(client.textRenderer, this.title, width / 2, 20, 0xFFFFFFFF);

        if (isManyChecked()) txtError.setMessage(Text.translatable("Please check exactly one HUD Element!"));
        else if (!inputScale.getText().matches("-?\\d+(\\.\\d+)?")) txtError.setMessage(Text.translatable("Scale has to be a number!"));
        else txtError.setMessage(Text.empty());
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }

    public float getScale() {
        try {
            return Float.parseFloat(inputScale.getText());
        } catch (Exception e) {}
        return 1.0f;
    }

    public HudElement getTickedElement() {
        if (chkSpeedometer.isChecked()) return new HudElement(50, 50, 145, 145, getScale(), ElementType.SPEEDOMETER);
        if (chkDpad.isChecked()) return new HudElement(50, 50, 50, 33, getScale(), ElementType.DPAD);
        if (chkPingicon.isChecked()) return new HudElement(50, 50, 10, 8, getScale(), ElementType.PINGICON);
        if (chkTimer.isChecked()) return new HudElement(50, 50, 40, 12, getScale(), ElementType.TIMER);
        return null;
    }

    public boolean isManyChecked() {
        boolean s = this.chkSpeedometer.isChecked();
        boolean d = this.chkDpad.isChecked();
        boolean p = this.chkPingicon.isChecked();
        boolean t = this.chkTimer.isChecked();

        int result = (s ? 1 : 0) + (d ? 1 : 0) + (p ? 1 : 0) + (t ? 1 : 0);
        return !(result == 1);
    }
}
