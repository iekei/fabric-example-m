package com.iekei.client.modules.hud;

import com.iekei.client.core.Tps;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class FpsTps extends Module {
	private static final Setting width = Setting.slider("Width", 70, 30, 160, 5, "px");
	private final Deque<Float> fpsHistory = new ArrayDeque<>();
	private final Deque<Float> tpsHistory = new ArrayDeque<>();

	public FpsTps() {
		super("fpstps", "FPS / TPS Graph", "Plots FPS and TPS over time.", false, width);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		push(fpsHistory, mc.getFps());
		push(tpsHistory, Tps.value() * 10.0F);

		int w = numInt("Width");
		int x = mc.getWindow().getGuiScaledWidth() - w - 6;
		int y = 40;
		graph(g, fpsHistory, x, y, w, 0xFF66CCFF);
		graph(g, tpsHistory, x, y + 26, w, 0xFF77FF99);
		Draw.text(g, "FPS " + mc.getFps(), x, y + 52, 0xFF66CCFF);
		Draw.text(g, String.format("TPS %.1f", Tps.value()), x, y + 62, 0xFF77FF99);
	}

	private void push(Deque<Float> queue, float value) {
		queue.addLast(value);

		while (queue.size() > 60) {
			queue.removeFirst();
		}
	}

	private void graph(GuiGraphicsExtractor g, Deque<Float> queue, int x, int y, int w, int color) {
		int i = 0;
		float step = (float) w / Math.max(1, queue.size() - 1);

		for (float value : queue) {
			int height = (int) Math.min(20.0F, value / 12.0F);
			int px = x + Math.round(i * step);
			g.fill(px, y + 20 - height, px + 1, y + 20, color);
			i++;
		}
	}
}