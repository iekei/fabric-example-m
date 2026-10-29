package com.iekei.client;

import com.iekei.IekeiClient;
import com.iekei.client.core.Config;
import com.iekei.client.core.Hud;
import com.iekei.client.core.ModuleManager;
import com.iekei.client.core.RenderDispatcher;
import com.iekei.client.gui.ClickGuiScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.sdl.SDLScancode;
import com.iekei.client.core.ChatCommands;
import com.iekei.client.core.Webhook;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

/**
 * Client entrypoint.
 *
 * <p>Registers the ClickGUI keybind ([Right Shift], rebindable in
 * Options > Controls), the HUD layer, the world render hook and opens the
 * ClickGUI while no other screen is shown. Closing is handled inside
 * {@link ClickGuiScreen} itself, which keeps the toggle reliable even while a
 * screen is focused.
 */
public final class IekeiClientClient implements ClientModInitializer {
	/** Custom keybind category shown in Options > Controls. */
	public static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(IekeiClient.MOD_ID, "main"));

	/** [Right Shift] - opens the ClickGUI. */
	public static KeyMapping openGuiKey;

	/**
	 * When the GUI closes itself because [Right Shift] was pressed again, the
	 * very same key press must not re-open it on the next client tick.
	 */
	private static long suppressOpenUntil;

	@Override
	public void onInitializeClient() {
		openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.iekeiclient.open_gui",
				InputConstants.Type.KEYBOARD,
				SDLScancode.SDL_SCANCODE_RSHIFT,
				CATEGORY));

		ClientTickEvents.END_CLIENT_TICK.register(IekeiClientClient::onEndTick);
		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());

		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath(IekeiClient.MOD_ID, "hud"),
				(extractor, deltaTracker) -> Hud.render(extractor));

		WorldRenderEvents.AFTER_ENTITIES.register(RenderDispatcher::onWorldRender);
		
		ClientSendMessageEvents.ALLOW_CHAT.register(ChatCommands::onChat);
		Webhook.load();

		IekeiClient.LOGGER.info("[Iekei Client] ClickGUI bound to [Right Shift]");
	}

	private static void onEndTick(Minecraft client) {
		ModuleManager.tick(client);
		Config.tickAutosave();

		if (openGuiKey == null) {
			return;
		}

		// drain every queued press so holding the key never spams the GUI
		while (openGuiKey.consumeClick()) {
			if (client.gui.screen() == null && System.currentTimeMillis() >= suppressOpenUntil) {
				client.gui.setScreen(new ClickGuiScreen());
			}
		}
	}

	/** Called by the GUI when it closes itself via the toggle key. */
	public static void suppressOpenFor(long millis) {
		suppressOpenUntil = System.currentTimeMillis() + millis;
	}
}