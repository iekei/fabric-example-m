package com.iekei.client.gui;

import com.iekei.client.gui.Icons.Icon;
import com.iekei.client.modules.combat.AimAssist;
import com.iekei.client.modules.combat.AutoClicker;
import com.iekei.client.modules.combat.BowAimbot;
import com.iekei.client.modules.combat.Criticals;
import com.iekei.client.modules.combat.FastBow;
import com.iekei.client.modules.combat.FastUse;
import com.iekei.client.modules.combat.HitBox;
import com.iekei.client.modules.combat.Killaura;
import com.iekei.client.modules.combat.Reach;
import com.iekei.client.modules.combat.TargetStrafe;
import com.iekei.client.modules.combat.Triggerbot;
import com.iekei.client.modules.combat.Velocity;
import com.iekei.client.modules.movement.AirJump;
import com.iekei.client.modules.movement.Blink;
import com.iekei.client.modules.movement.EntitySpeed;
import com.iekei.client.modules.movement.FastLadder;
import com.iekei.client.modules.movement.Fly;
import com.iekei.client.modules.movement.HighJump;
import com.iekei.client.modules.movement.InventoryMove;
import com.iekei.client.modules.movement.Jesus;
import com.iekei.client.modules.movement.NoSlowdown;
import com.iekei.client.modules.movement.Parkour;
import com.iekei.client.modules.movement.SafeWalk;
import com.iekei.client.modules.movement.Speed;
import com.iekei.client.modules.movement.Spider;
import com.iekei.client.modules.movement.Step;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The full Iekei Client module list: 8 categories, 78 modules.
 * Migrated categories construct real module classes; the rest still use the
 * plain data-only {@link #mod} helper until their behaviour lands.
 */
public final class Registry {
	private Registry() {
	}

	private static final List<Category> CATEGORIES = new ArrayList<>();
	private static int activeCategory;

	static {
		/* ------------------------------------------------ Movement */
		Category movement = new Category("movement", "Movement", "移動系",
				"Travel faster, higher and further than vanilla allows.", Icon.FOOTPRINTS);
		movement.add(new Fly());
		movement.add(new Speed());
		movement.add(new NoSlowdown());
		movement.add(new Jesus());
		movement.add(new Step());
		movement.add(new HighJump());
		movement.add(new SafeWalk());
		movement.add(new Spider());
		movement.add(new InventoryMove());
		movement.add(new AirJump());
		movement.add(new EntitySpeed());
		movement.add(new Blink());
		movement.add(new FastLadder());
		movement.add(new Parkour());
		CATEGORIES.add(movement);

		/* ------------------------------------------------ Combat */
		Category combat = new Category("combat", "Combat", "戦闘系",
				"Combat assistance with humanised timing and rotation.", Icon.SWORDS);
		combat.add(new Killaura());
		combat.add(new Triggerbot());
		combat.add(new AimAssist());
		combat.add(new AutoClicker());
		combat.add(new Reach());
		combat.add(new Velocity());
		combat.add(new Criticals());
		combat.add(new BowAimbot());
		combat.add(new FastBow());
		combat.add(new TargetStrafe());
		combat.add(new HitBox());
		combat.add(new FastUse());
		CATEGORIES.add(combat);

		/* ------------------------------------------------ Utility */
		Category utility = new Category("utility", "Player Utility", "ユーティリティ",
				"Quality of life automation for grinding and survival.", Icon.WRENCH);
		utility.add(mod("fastbreak", "FastBreak", "Mines blocks faster.", true,
				Setting.slider("Speed", 2.0, 1.0, 5.0, 0.1, "x"),
				Setting.toggle("Packet Mode", true)));
		utility.add(mod("fastplace", "FastPlace", "Removes the block placement delay.", false,
				Setting.slider("Delay", 0, 0, 4, 1, "t")));
		utility.add(mod("autoarmor", "AutoArmor", "Equips the best armour automatically.", true,
				Setting.toggle("Allow Hotbar", false)));
		utility.add(mod("autoeat", "AutoEat", "Eats when your hunger drops.", false,
				Setting.slider("Hunger Threshold", 14, 2, 19, 1, "/20")));
		utility.add(mod("autototem", "AutoTotem", "Keeps a totem in your offhand.", true,
				Setting.slider("Swap Below", 10, 1, 20, 1, "hp")));
		utility.add(mod("autotool", "AutoTool", "Selects the fastest tool when mining.", false,
				Setting.toggle("Respect Silk Touch", false),
				Setting.toggle("Stop Low Durability", true)));
		utility.add(mod("cheststealer", "ChestStealer", "Empties chests into your inventory.", false,
				Setting.slider("Delay", 60, 0, 500, 10, "ms")));
		utility.add(mod("inventorycleaner", "InventoryCleaner", "Drops junk items automatically.", false,
				Setting.toggle("Auto Drop", true),
				Setting.toggle("Keep Gapples", true)));
		utility.add(mod("nofall", "NoFall", "Cancels fall damage.", true,
				Setting.choice("Mode", "Packet", "NoGround", "Spoof")));
		utility.add(mod("scaffold", "Scaffold", "Places blocks beneath you as you walk.", false,
				Setting.choice("Mode", "Normal", "Tower", "Legit"),
				Setting.toggle("Silent Rotate", true)));
		utility.add(mod("fastdrop", "FastDrop", "Drops items without the delay.", false,
				Setting.slider("Speed", 3, 1, 5, 1, "x")));
		utility.add(mod("autorespawn", "AutoRespawn", "Respawns instantly after dying.", false,
				Setting.toggle("Enabled", true)));
		utility.add(mod("autodisconnect", "AutoDisconnect", "Leaves the server at low health.", false,
				Setting.slider("Health", 6, 1, 20, 1, "hp")));
		utility.add(mod("antiafk", "AntiAFK", "Avoids AFK kicks with small moves.", false,
				Setting.choice("Mode", "Swing", "Walk", "Rotate"),
				Setting.slider("Interval", 30, 5, 120, 5, "s")));
		CATEGORIES.add(utility);

		/* ------------------------------------------------ Render */
		Category render = new Category("render", "Render Visual", "視覚・描画系",
				"Client-side visualisation of the world around you.", Icon.EYE);
		render.add(mod("xray", "Xray", "Reveals ores through solid blocks.", false,
				Setting.slider("Transparency", 70, 0, 100, 1, "%"),
				Setting.toggle("Ores Only", true)));
		render.add(mod("esp", "ESP", "Highlights entities through walls.", true,
				Setting.choice("Style", "Outline", "Skeleton", "Box"),
				Setting.choice("Color", "Accent", "Red", "Cyan", "Gold")));
		render.add(mod("tracers", "Tracers", "Draws lines from you to targets.", false,
				Setting.choice("Color", "Accent", "Blue", "Lime", "White"),
				Setting.slider("Thickness", 2, 1, 5, 0.5, "px")));
		render.add(mod("nametags", "Nametags", "Enlarges and restyles nametags.", false,
				Setting.slider("Size", 1.4, 0.5, 3.0, 0.1, "x"),
				Setting.toggle("Show Health", true)));
		render.add(mod("fullbright", "Fullbright", "Removes darkness client-side.", true,
				Setting.slider("Brightness", 12, 1, 15, 1, "lvl")));
		render.add(mod("freecam", "Freecam", "Detaches the camera from your body.", false,
				Setting.slider("Fly Speed", 1.5, 0.5, 5.0, 0.1, "x")));
		render.add(mod("storageesp", "StorageESP", "Highlights chests and shulkers.", false,
				Setting.choice("Color", "Amber", "Accent", "White")));
		render.add(mod("itemesp", "ItemESP", "Highlights dropped items on the ground.", false,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(render);

		/* ------------------------------------------------ Cosmetic */
		Category cosmetic = new Category("cosmetic", "Cosmetic", "見た目系",
				"Purely visual tweaks — sky, animations and effects.", Icon.PALETTE);
		cosmetic.add(mod("customsky", "CustomSky", "Replaces the vanilla skybox texture.", false,
				Setting.choice("Skybox", "Galaxy", "Sunset", "Nebula")));
		cosmetic.add(mod("custommodel", "CustomModel", "Swaps the model used by held tools.", false,
				Setting.choice("Model", "Vanilla", "Slim 1.7", "Glow")));
		cosmetic.add(mod("customanimations", "CustomAnimations", "Changes the first-person hand motion.", true,
				Setting.choice("Animation", "1.7", "Smooth", "Modern")));
		cosmetic.add(mod("cape", "Cape / Cosmetics", "Shows capes and cosmetic accessories.", false,
				Setting.toggle("Show Cape", true),
				Setting.toggle("Show Wings", false)));
		cosmetic.add(mod("timechanger", "TimeChanger", "Pins the world time client-side.", false,
				Setting.slider("Time", 13000, 0, 24000, 100, "t")));
		cosmetic.add(mod("weatherchanger", "WeatherChanger", "Forces a weather state client-side.", false,
				Setting.choice("Weather", "Clear", "Rain", "Thunder")));
		cosmetic.add(mod("lowfire", "LowFire", "Lowers the fire overlay while burning.", true,
				Setting.slider("Overlay Height", 30, 0, 100, 1, "%")));
		cosmetic.add(mod("hitcolor", "HitColor", "Tints the screen when you take a hit.", false,
				Setting.choice("Tint", "Red", "Accent", "Orange")));
		cosmetic.add(mod("particles", "ParticleCustomizer", "Amplifies hit and critical particles.", false,
				Setting.slider("Amount", 120, 0, 300, 10, "%")));
		cosmetic.add(mod("motionblur", "MotionBlur", "Adds a subtle trailing screen blur.", false,
				Setting.slider("Strength", 3.5, 0.0, 10.0, 0.1, "")));
		cosmetic.add(mod("crosshair", "CustomCrosshair", "Reshapes the crosshair to your taste.", false,
				Setting.slider("Size", 9, 2, 20, 1, "px"),
				Setting.slider("Thickness", 2, 1, 4, 0.5, "px")));
		CATEGORIES.add(cosmetic);

		/* ------------------------------------------------ Base find */
		Category basefind = new Category("basefind", "Base Find", "拠点捜索系",
				"Anubis-style structure and stash scanning for anarchy servers.", Icon.HOME);
		basefind.add(mod("basefinder", "BaseFinder", "Detects man-made block clusters.", true,
				Setting.slider("Sensitivity", 6, 1, 10, 1, ""),
				Setting.slider("Scan Range", 128, 32, 512, 32, "b")));
		basefind.add(mod("chestfinder", "ChestFinder", "Scans for containers and logs coordinates.", false,
				Setting.slider("Range", 96, 16, 256, 16, "b"),
				Setting.toggle("Log to Chat", true)));
		basefind.add(mod("portalfinder", "PortalFinder", "Detects nether portals nearby.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("bedfinder", "BedFinder", "Locates beds for respawn anchors.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("spawnerfinder", "SpawnerFinder", "Finds mob spawners underground.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("illegalblockfinder", "IllegalBlockFinder", "Flags blocks that should not exist.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("stashfinder", "StashFinder", "Identifies high-volume storage stashes.", false,
				Setting.slider("Min Containers", 12, 2, 64, 1, "")));
		basefind.add(mod("newchunks", "NewChunks", "Marks chunks generated by other players.", false,
				Setting.choice("Highlight", "Emerald", "Accent", "Red")));
		basefind.add(mod("trailfinder", "TrailFinder", "Traces movement paths of players.", false,
				Setting.toggle("Show Lines", true)));
		basefind.add(mod("worldguardfinder", "WorldGuardFinder", "Detects claims and protected regions.", false,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(basefind);

		/* ------------------------------------------------ Notification */
		Category notify = new Category("notify", "Notification", "通知・警報系",
				"Alerts that reach you outside the game window.", Icon.BELL);
		notify.add(mod("basealert", "BaseAlert", "Notifies when a base is detected.", true,
				Setting.toggle("System Sound", true)));
		notify.add(mod("playeralert", "PlayerAlert", "Warns when players come close.", false,
				Setting.slider("Trigger Distance", 64, 8, 256, 8, "b")));
		notify.add(mod("visualalert", "VisualAlert", "Shows on-screen popup warnings.", false,
				Setting.toggle("Enabled", true)));
		notify.add(mod("soundalert", "SoundAlert", "Plays a detection sound alert.", false,
				Setting.slider("Volume", 60, 0, 100, 5, "%")));
		notify.add(mod("webhook", "WebhookNotifier", "Sends detections to a Discord webhook.", false,
				Setting.choice("Target", "Discord", "Custom URL")));
		notify.add(mod("autologalert", "AutoLogOnAlert", "Disconnects when an alert fires.", false,
				Setting.slider("Health Threshold", 8, 1, 20, 1, "hp"),
				Setting.slider("Distance Threshold", 32, 8, 128, 8, "b")));
		CATEGORIES.add(notify);

		/* ------------------------------------------------ Settings */
		Category settings = new Category("settings", "Client Settings", "設定",
				"Keybinds, accent theme and client-wide preferences.", Icon.GEAR);
		settings.add(mod("keybind", "Keybind Config", "The key that opens and closes this menu.", true,
				Setting.choice("Menu Toggle Key", "Right Shift", "Right Ctrl", "Grave")));
		settings.add(mod("theme", "Theme Selector", "Accent colour used across the whole client.", true,
				Setting.slider("Glow Intensity", 70, 0, 100, 5, "%")));
		settings.add(mod("sound", "Sound Effects", "Subtle UI clicks and toggle blips.", true,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(settings);
	}

	private static Module mod(String id, String name, String description, boolean enabled, Setting... settings) {
		return new PlaceholderModule(id, name, description, enabled, settings);
	}

	public static List<Category> categories() {
		return Collections.unmodifiableList(CATEGORIES);
	}

	public static Category active() {
		return CATEGORIES.get(activeCategory);
	}

	public static int activeIndex() {
		return activeCategory;
	}

	public static void setActive(int index) {
		activeCategory = Math.floorMod(index, CATEGORIES.size());
	}

	public static int totalModules() {
		int n = 0;

		for (Category c : CATEGORIES) {
			n += c.modules().size();
		}

		return n;
	}

	public static int totalEnabled() {
		int n = 0;

		for (Category c : CATEGORIES) {
			n += c.enabledCount();
		}

		return n;
	}

	/** Every module that matches the query (name, description or category). */
	public static List<Module> search(String query) {
		String q = query.toLowerCase().trim();
		List<Module> out = new ArrayList<>();

		if (q.isEmpty()) {
			return out;
		}

		for (Category c : CATEGORIES) {
			for (Module m : c.modules()) {
				String haystack = (m.name() + " " + m.description() + " " + c.name() + " " + c.subtitle())
						.toLowerCase();

				if (haystack.contains(q)) {
					out.add(m);
				}
			}
		}

		return out;
	}

	/** Data-only module used by categories whose behaviour has not landed yet. */
	private static final class PlaceholderModule extends Module {
		private PlaceholderModule(String id, String name, String description, boolean enabled,
				Setting... settings) {
			super(id, name, description, enabled, settings);
		}
	}
}