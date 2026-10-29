package com.iekei.client.gui;

import com.iekei.client.core.Config;
import com.iekei.client.core.ModuleManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** A toggleable client module. Subclasses implement the actual behaviour. */
public abstract class Module {
	protected static final Minecraft mc = Minecraft.getInstance();

	private final String id;
	private final String name;
	private final String description;
	private final List<Setting> settings = new ArrayList<>();
	private final Setting keybind = Setting.keybind("Keybind", 0);
	private Category category;
	private boolean enabled;
	private boolean expanded;
	private float expandProgress;

	protected Module(String id, String name, String description, boolean enabled, Setting... settings) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.settings.addAll(List.of(settings));
		this.settings.add(keybind);
	}

	/* ---------------- lifecycle ---------------- */

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	/** Runs on every client tick, but only while enabled. */
	public void onTick() {
	}

	/** Runs during the world render pass, after entities. */
	public void onRenderWorld(WorldRenderContext context) {
	}

	/** Runs on the HUD layer every frame. */
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
	}

	public String hudInfo() {
		return null;
	}

	/* ---------------- state ---------------- */

	public final void setEnabled(boolean value) {
		if (enabled == value) {
			return;
		}

		enabled = value;

		if (value) {
			onEnable();
			ModuleManager.onModuleEnabled(this);
		} else {
			onDisable();
			ModuleManager.onModuleDisabled(this);
		}

		Config.requestSave();
	}

	public final void toggleEnabled() {
		setEnabled(!enabled);
	}

	/* ---------------- accessors ---------------- */

	public String id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public List<Setting> settings() {
		return settings;
	}

	public Category category() {
		return category;
	}

	void setCategory(Category category) {
		this.category = category;
	}

	public boolean enabled() {
		return enabled;
	}

	public boolean expanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public float expandProgress() {
		return expandProgress;
	}

	public Setting keybind() {
		return keybind;
	}

	public int key() {
		return keybind.getKey();
	}

	/* ---------------- setting helpers ---------------- */

	protected Setting setting(String label) {
		for (Setting s : settings) {
			if (s.label().equalsIgnoreCase(label)) {
				return s;
			}
		}

		throw new IllegalStateException(id + " has no setting '" + label + "'");
	}

	protected double num(String label) {
		return setting(label).getNumber();
	}

	protected int numInt(String label) {
		return (int) Math.round(setting(label).getNumber());
	}

	protected boolean bool(String label) {
		return setting(label).getBool();
	}

	protected String mode(String label) {
		return setting(label).choiceLabel();
	}

	protected boolean modeIs(String label, String option) {
		return setting(label).choiceIs(option);
	}

	/* ---------------- animation ---------------- */

	/** Advances the accordion animation (delta in seconds). */
	public void updateAnimation(float deltaSeconds) {
		float target = expanded ? 1.0F : 0.0F;
		float speed = Math.min(1.0F, deltaSeconds * 7.0F);

		if (expandProgress < target) {
			expandProgress = Math.min(target, expandProgress + speed);
		} else if (expandProgress > target) {
			expandProgress = Math.max(target, expandProgress - speed);
		}
	}
}