package nopey.ibrextras.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void RenderTexture(DrawContext context, String texturePath, int x, int y, int width, int height, float scale, float rotation) {
        MatrixStack mx = context.getMatrices();
        mx.push();

        Matrix4f pos = mx.peek().getPositionMatrix();
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buff = ts.getBuffer();

        mx.translate(-(x * (scale - 1f)), -(y * (scale - 1f)), 0);
        mx.scale(scale, scale, 1);
        mx.translate(x + (float) width / 2, y + (float) height / 2, 0);
        mx.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        mx.translate(-(x + (float) width / 2), -(y + (float) height / 2), 0);

        buff.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buff.vertex(pos, x, y, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
        buff.vertex(pos, x, y + height, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
        buff.vertex(pos, x + width, y + height, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buff.vertex(pos, x + width, y, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, new Identifier("ibrextras", texturePath));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mx.pop();
        ts.draw();

        RenderSystem.disableBlend();
    }

    public static void RenderQuad(DrawContext context, int x, int y, int width, int height, float scale, float rotation, int color) {
        MatrixStack mx = context.getMatrices();
        mx.push();

        Matrix4f pos = mx.peek().getPositionMatrix();
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buff = ts.getBuffer();

        mx.translate(-(x * (scale - 1f)), -(y * (scale - 1f)), 0);
        mx.scale(scale, scale, 1);
        mx.translate(x + (float) width / 2, y + (float) height / 2, 0);
        mx.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        mx.translate(-(x + (float) width / 2), -(y + (float) height / 2), 0);

        buff.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buff.vertex(pos, x, y, 0).color(color).next();
        buff.vertex(pos, x, y + height, 0).color(color).next();
        buff.vertex(pos, x + width, y + height, 0).color(color).next();
        buff.vertex(pos, x + width, y, 0).color(color).next();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mx.pop();
        ts.draw();

        RenderSystem.disableBlend();
    }

    public static void RenderLine(DrawContext context, int x, int y, int length, float rotation, int color) {
        MatrixStack mx = context.getMatrices();
        mx.push();

        Matrix4f pos = mx.peek().getPositionMatrix();
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buff = ts.getBuffer();

        mx.translate(x, y, 0);
        mx.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));
        mx.translate(-x, -y, 0);

        buff.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        buff.vertex(pos, x, y, 0).color(color).next();
        buff.vertex(pos, x + length, y, 0).color(color).next();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mx.pop();
        ts.draw();

        RenderSystem.disableBlend();
    }
}
