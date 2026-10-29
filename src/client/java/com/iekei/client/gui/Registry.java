package com.iekei.client.gui;

import com.iekei.client.gui.Icons.Icon;
import com.iekei.client.modules.basefind.BaseFinder;
import com.iekei.client.modules.basefind.BedFinder;
import com.iekei.client.modules.basefind.ChestFinder;
import com.iekei.client.modules.basefind.IllegalBlockFinder;
import com.iekei.client.modules.basefind.NewChunks;
import com.iekei.client.modules.basefind.PortalFinder;
import com.iekei.client.modules.basefind.SpawnerFinder;
import com.iekei.client.modules.basefind.StashFinder;
import com.iekei.client.modules.basefind.TrailFinder;
import com.iekei.client.modules.basefind.WorldGuardFinder;
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
import com.iekei.client.modules.cosmetic.Cape;
import com.iekei.client.modules.cosmetic.CustomAnimations;
import com.iekei.client.modules.cosmetic.CustomCrosshair;
import com.iekei.client.modules.cosmetic.CustomModel;
import com.iekei.client.modules.cosmetic.CustomSky;
import com.iekei.client.modules.cosmetic.HitColor;
import com.iekei.client.modules.cosmetic.LowFire;
import com.iekei.client.modules.cosmetic.MotionBlur;
import com.iekei.client.modules.cosmetic.ParticleCustomizer;
import com.iekei.client.modules.cosmetic.TimeChanger;
import com.iekei.client.modules.cosmetic.WeatherChanger;
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
import com.iekei.client.modules.notify.AutoLogOnAlert;
import com.iekei.client.modules.notify.BaseAlert;
import com.iekei.client.modules.notify.PlayerAlert;
import com.iekei.client.modules.notify.SoundAlert;
import com.iekei.client.modules.notify.VisualAlert;
import com.iekei.client.modules.notify.WebhookNotifier;
import com.iekei.client.modules.render.Esp;
import com.iekei.client.modules.render.Freecam;
import com.iekei.client.modules.render.Fullbright;
import com.iekei.client.modules.render.ItemEsp;
import com.iekei.client.modules.render.Nametags;
import com.iekei.client.modules.render.StorageEsp;
import com.iekei.client.modules.render.Tracers;
import com.iekei.client.modules.render.Xray;
import com.iekei.client.modules.utility.AntiAFK;
import com.iekei.client.modules.utility.AutoArmor;
import com.iekei.client.modules.utility.AutoDisconnect;
import com.iekei.client.modules.utility.AutoEat;
import com.iekei.client.modules.utility.AutoRespawn;
import com.iekei.client.modules.utility.AutoTool;
import com.iekei.client.modules.utility.AutoTotem;
import com.iekei.client.modules.utility.ChestStealer;
import com.iekei.client.modules.utility.FastBreak;
import com.iekei.client.modules.utility.FastDrop;
import com.iekei.client.modules.utility.FastPlace;
import com.iekei.client.modules.utility.InventoryCleaner;
import com.iekei.client.modules.utility.NoFall;
import com.iekei.client.modules.utility.Scaffold;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.iekei.client.modules.settings.KeybindConfig;
import com.iekei.client.modules.settings.SoundEffects;
import com.iekei.client.modules.settings.ThemeSelector;

/**
 * The full Iekei Client module list: 8 categories, 78 modules.
 * All behaviour categories construct real module classes; Client Settings still
 * uses the plain data-only {@link #mod} helper.
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
		utility.add(new FastBreak());
		utility.add(new FastPlace());
		utility.add(new AutoArmor());
		utility.add(new AutoEat());
		utility.add(new AutoTotem());
		utility.add(new AutoTool());
		utility.add(new ChestStealer());
		utility.add(new InventoryCleaner());
		utility.add(new NoFall());
		utility.add(new Scaffold());
		utility.add(new FastDrop());
		utility.add(new AutoRespawn());
		utility.add(new AutoDisconnect());
		utility.add(new AntiAFK());
		CATEGORIES.add(utility);

		/* ------------------------------------------------ Render */
		Category render = new Category("render", "Render Visual", "視覚・描画系",
				"Client-side visualisation of the world around you.", Icon.EYE);
		render.add(new Xray());
		render.add(new Esp());
		render.add(new Tracers());
		render.add(new Nametags());
		render.add(new Fullbright());
		render.add(new Freecam());
		render.add(new StorageEsp());
		render.add(new ItemEsp());
		CATEGORIES.add(render);

		/* ------------------------------------------------ Cosmetic */
		Category cosmetic = new Category("cosmetic", "Cosmetic", "見た目系",
				"Purely visual tweaks — sky, animations and effects.", Icon.PALETTE);
		cosmetic.add(new CustomSky());
		cosmetic.add(new CustomModel());
		cosmetic.add(new CustomAnimations());
		cosmetic.add(new Cape());
		cosmetic.add(new TimeChanger());
		cosmetic.add(new WeatherChanger());
		cosmetic.add(new LowFire());
		cosmetic.add(new HitColor());
		cosmetic.add(new ParticleCustomizer());
		cosmetic.add(new MotionBlur());
		cosmetic.add(new CustomCrosshair());
		CATEGORIES.add(cosmetic);

		/* ------------------------------------------------ Base find */
		Category basefind = new Category("basefind", "Base Find", "拠点捜索系",
				"Anubis-style structure and stash scanning for anarchy servers.", Icon.HOME);
		basefind.add(new BaseFinder());
		basefind.add(new ChestFinder());
		basefind.add(new PortalFinder());
		basefind.add(new BedFinder());
		basefind.add(new SpawnerFinder());
		basefind.add(new IllegalBlockFinder());
		basefind.add(new StashFinder());
		basefind.add(new NewChunks());
		basefind.add(new TrailFinder());
		basefind.add(new WorldGuardFinder());
		CATEGORIES.add(basefind);

		/* ------------------------------------------------ Notification */
		Category notify = new Category("notify", "Notification", "通知・警報系",
				"Alerts that reach you outside the game window.", Icon.BELL);
		notify.add(new BaseAlert());
		notify.add(new PlayerAlert());
		notify.add(new VisualAlert());
		notify.add(new SoundAlert());
		notify.add(new WebhookNotifier());
		notify.add(new AutoLogOnAlert());
		CATEGORIES.add(notify);

		/* ------------------------------------------------ Settings */
		Category settings = new Category("settings", "Client Settings", "設定",
				"Keybinds, accent theme and client-wide preferences.", Icon.GEAR);
		settings.add(new KeybindConfig());
		settings.add(new ThemeSelector());
		settings.add(new SoundEffects());
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

	/** Data-only module used by the Client Settings category. */
	private static final class PlaceholderModule extends Module {
		private PlaceholderModule(String id, String name, String description, boolean enabled,
				Setting... settings) {
			super(id, name, description, enabled, settings);
		}
	}
}