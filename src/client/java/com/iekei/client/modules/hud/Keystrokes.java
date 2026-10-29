package com.iekei.client.modules.hud;

import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class Keystrokes extends Module {
	private static final Setting size = Setting.slider("Key Size", 16, 10, 28, 1, "px");

	public Keystrokes() {
		super("keystrokes", "Keystrokes", "Visualises your current key state.", false, size);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		int s = numInt("Key Size");
		int x = 6;
		int y = mc.getWindow().getGuiScaledHeight() - s * 2 - 26;

		key(g, x + s + 2, y, s, "W", mc.options.keyUp.isDown());
		key(g, x, y + s + 2, s, "A", mc.options.keyLeft.isDown());
		key(g, x + s + 2, y + s + 2, s, "S", mc.options.keyDown.isDown());
		key(g, x + (s + 2) * 2, y + s + 2, s, "D", mc.options.keyRight.isDown());
		key(g, x, y + (s + 2) * 2, s * 2 + 2, "LMB", mc.options.keyAttack.isDown());
		key(g, x + (s + 2) * 2, y + (s + 2) * 2, s * 2 + 2, "RMB", mc.options.keyUse.isDown());
	}

	private void key(GuiGraphicsExtractor g, int x, int y, int size, String label, boolean down) {
		g.fill(x, y, x + size, y + size, down ? 0xCC7A4BE0 : 0x88101018);
		Draw.textCenter(g, label, x + size / 2, y + size / 2 - 4, down ? 0xFFFFFFFF : 0xFFAAAAAA);
	}
}