package nopey.ibrextras.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import nopey.ibrextras.config.Config;
import nopey.ibrextras.config.Data;
import nopey.ibrextras.config.screens.HudScreen;
import nopey.ibrextras.mixin.InGameHudMixin;
import nopey.ibrextras.utils.RenderUtils;
import org.joml.Matrix4f;


@Environment(EnvType.CLIENT)
public class HudElement extends ClickableWidget {

    public HudElement(Text message, int x, int y, int color, float scale) {
        super(x, y, 0, (int) (8 * scale), message);
        client = MinecraftClient.getInstance();
        this.setWidth((int) (client.textRenderer.getWidth(getFilteredText()) * scale));
        this.enabled = true;
        this.color = color;
        this.scale = scale;
        this.type = ElementType.TEXT;
        //this.initKeybind();
        this.initButton();
    }

    public HudElement(int x, int y, int width, int height, float scale, ElementType type) {
        super(x, y, (int) (width * scale), (int) (height * scale), Text.literal("NONE"));
        client = MinecraftClient.getInstance();
        this.enabled = true;
        this.color = 0xFFFFFFFF;
        this.scale = scale;
        this.type = type;
        this.initialWidth = width;
        this.initialHeight = height;
        //this.initKeybind();
        this.initButton();
    }

    public static final Identifier WIDGETS_TEXTURE = new Identifier("ibrextras", "textures/widgets.png");

    private boolean timerMatch;

    public int color;
    public final float scale;
    public final ElementType type;
    public int initialWidth;
    public int initialHeight;

    public MinecraftClient client;

    public int lastMouseX;
    public int lastMouseY;
    public int lastX;
    public int lastY;
    public boolean enabled;
    public boolean isDragging;
    public ButtonWidget btnDelete;
    public KeyBinding keyBind;

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        int w = client.getWindow().getScaledWidth() / 2;
        int h = client.getWindow().getScaledHeight();

        if (client.currentScreen instanceof HudScreen) {
            RenderUtils.RenderQuad(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 0, 0x22FF0000);
            if (this.type == ElementType.TIMER && !this.timerMatch) {
                this.setWidth(client.textRenderer.getWidth("00.000 -00.00"));
                RenderUtils.RenderQuad(context, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 1, 0, 0x55000000);
                //context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0x55000000);
                context.drawTextWithShadow(client.textRenderer, "00.000 -00.00", this.getX() + 2, this.getY() + 2, 0xFFFFFFFF);
            }
            if (isDragging) {
                context.drawTextWithShadow(client.textRenderer, "X: " + this.getX() + " Y: " + this.getY(), this.getX(), this.getY() - 10, 0xFFFFFFFF);

                int lineColor = 0xFF000000;
                int highlightColor = 0xFF00FF00;

                for (int i = 0; i < w * 2; i++) {
                    if (i % 5 == 0) {
                        if (i == this.getX() || i == this.getX() + this.getWidth())
                            RenderUtils.RenderLine(context, i, 0, h, 90, highlightColor);
                        else RenderUtils.RenderLine(context, i, 0, h, 90, lineColor);
                    }
                }

                for (int i = 0; i < h; i++) {
                    if (i % 5 == 0) {
                        if (i == this.getY() || i == this.getY() + this.getHeight())
                            RenderUtils.RenderLine(context, 0, i, w * 2, 0, highlightColor);
                        else RenderUtils.RenderLine(context, 0, i, w * 2, 0, lineColor);
                    }
                }
            }
        }
    }

    public void render(DrawContext context, float tickDelta) {
        this.btnDelete.setPosition(this.getX() + this.getWidth() - 4, this.getY() - 4);
        if (!this.enabled) return;

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (type == ElementType.SPEEDOMETER) {
            if (Data.speedometerBackground)  RenderUtils.RenderTexture(context, "textures/speed_gauge_bg.png", this.getX(), this.getY(), 145, 145, this.scale, 0f);
            else RenderUtils.RenderTexture(context, "textures/speed_gauge.png", this.getX(), this.getY(), 145, 145, this.scale, 0f);
            RenderUtils.RenderTexture(context, "textures/speed_needle.png", this.getX(), this.getY(), 145, 145, this.scale, 45 + (float) Data.displayedSpeedVal * 3.1f);
        }

        if (type == ElementType.DPAD) {
            startScale(context);
            context.drawTexture(WIDGETS_TEXTURE, this.getX(), this.getY() + 17, 0, client.options.leftKey.isPressed() ? 46 : 30, 16, 16);
            context.drawTexture(WIDGETS_TEXTURE, this.getX() + 34, this.getY() + 17, 16, client.options.rightKey.isPressed() ? 46 : 30, 16, 16);
            context.drawTexture(WIDGETS_TEXTURE, this.getX() + 17, this.getY(), 32, client.options.forwardKey.isPressed() ? 46 : 30, 16, 16);
            context.drawTexture(WIDGETS_TEXTURE, this.getX() + 17, this.getY() + 17, 48, client.options.backKey.isPressed() ? 46 : 30, 16, 16);
            stopScale(context);
        }

        if (type == ElementType.PINGICON) {
            int offset;
            if(Data.pingVal < 0) offset = 40;
            else if(Data.pingVal < 150) offset = 0;
            else if(Data.pingVal < 300) offset = 8;
            else if(Data.pingVal < 600) offset = 16;
            else if(Data.pingVal < 1000) offset = 24;
            else offset = 32;
            startScale(context);
            context.drawTexture(WIDGETS_TEXTURE, this.getX(), this.getY(), 246, offset, 10, 8);
            stopScale(context);
        }

        if (type == ElementType.TIMER) {
            Text msg = ((InGameHudMixin) MinecraftClient.getInstance().inGameHud).getOverlayMessage();
            if (msg == null) return;
            String s = msg.getString();
            if (s.matches("(\\d+:)?(\\d+).(\\d+)( \\+?\\-?(\\d+:)?(\\d+).(\\d+))?")) {
                timerMatch = true;
                String[] split = s.split(" ");
                int timeWidth = client.textRenderer.getWidth(split[0]);

                this.setWidth(timeWidth + 4);
                context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0x55000000);
                context.drawTextWithShadow(client.textRenderer, split[0], this.getX() + 2, this.getY() + 2, 0xFFFFFFFF);

                if (split.length > 1) {
                    int splitWidth = client.textRenderer.getWidth(split[1]);
                    this.setWidth(timeWidth + splitWidth + 8);

                    context.fill(this.getX() + timeWidth + 4, this.getY(), this.getX() + timeWidth + splitWidth + 8, this.getY() + this.getHeight(), split[1].startsWith("+") ? 0x55FF0000 : 0x5500FF00);
                    context.drawTextWithShadow(client.textRenderer, split[1], this.getX() + timeWidth + 6, this.getY() + 2, 0xFFFFFFFF);
                }

                //((InGameHudMixin) MinecraftClient.getInstance().inGameHud).setOverlayRemaining(0);
            }
            timerMatch = false;
        }

        if (type == ElementType.TEXT) {
            this.setWidth((int) (client.textRenderer.getWidth(getFilteredText()) * scale));
            this.startScale(context);
            context.drawTextWithShadow(client.textRenderer, getFilteredText(), this.getX(), this.getY(), color);
            this.stopScale(context);
        }

        RenderSystem.disableBlend();
    }

    public void onClick(double mouseX, double mouseY) {
        this.lastMouseX = (int) mouseX;
        this.lastMouseY = (int) mouseY;
        this.lastX = this.getX();
        this.lastY = this.getY();
        this.isDragging = true;
    }

    public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        int distanceX = (int) mouseX - lastMouseX;
        int distanceY = (int) mouseY - lastMouseY;
        this.setX(this.lastX + distanceX);
        this.setY(this.lastY + distanceY);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    public void onRelease(double mouseX, double mouseY) {
        this.isDragging = false;
    }

    public void startScale(DrawContext context) {
        context.getMatrices().push();
        Matrix4f mx4f = context.getMatrices().peek().getPositionMatrix();
        mx4f.scaleAround((float) Math.sqrt(this.scale), this.getX(), this.getY(), 0);
        context.getMatrices().multiplyPositionMatrix(mx4f);
    }

    public void stopScale(DrawContext context) { context.getMatrices().pop(); }

    public Text getFilteredText() {
        return Text.literal(this.getMessage().getString()
                .replace("$name", client.player != null ? client.player.getName().getString() : "ERROR")
                .replace("$speed:m/s", String.format("%03.0f", Data.displayedSpeedVal))
                .replace("$speed:km/h", String.format("%03.0f", Data.displayedSpeedVal * 3.6d))
                .replace("$speed:mph", String.format("%03.0f", Data.displayedSpeedVal * 2.236936d))
                .replace("$speed:kt", String.format("%03.0f", Data.displayedSpeedVal * 1.943844d))
                .replace("$speed", String.format("%03.0f", Data.displayedSpeedVal * 3.6d))
                .replace("$ping", String.format("%d", Data.pingVal))
                .replace("$angle", String.format("%03.0f", Data.angleVal))
                .replace("$g", String.format("%+.1f", Data.gVal))
                .replace("$fps", String.format("%d", Data.fpsVal))
                .replace("$inertia", String.format("%03.0f", Math.abs(Data.displayedInertiaVal))));
    }

    public void initButton() {
        btnDelete = ButtonWidget.builder(Text.translatable("x"), button -> {
                    Data.hudElements.remove(this);
                    client.setScreen(client.currentScreen);
                    Config.save();
                })
                .dimensions(this.getX() + this.getWidth() - 4, this.getY() - 4, 10, 10)
                .build();
    }

    public void initKeybind() {
        this.keyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            this.getMessage().getString(),
            InputUtil.Type.KEYSYM,
            0,
            "nopey.ibrextras.category"
        ));
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}
}
