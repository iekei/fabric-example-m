package com.iekei.client.core;

import com.mojang.blaze3d.platform.InputConstants;

/** Thin wrapper around the raw key state so the source of truth lives in one place. */
public final class Input {
	private Input() {
	}

	public static boolean isDown(long window, int scancode) {
		return InputConstants.isKeyDown(window, scancode);
	}
}