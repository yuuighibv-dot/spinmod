package com.spinmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SpinModClient implements ClientModInitializer {
	private static final float SPEED_DEGREES_PER_TICK = 25.0f;

	private static KeyBinding toggleKey;
	private static boolean spinning = false;

	@Override
	public void onInitializeClient() {
		KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("spinmod", "main"));

		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.spinmod.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				category
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player == null) {
				spinning = false;
				return;
			}

			while (toggleKey.wasPressed()) {
				spinning = !spinning;
				player.sendMessage(Text.translatable(spinning ? "message.spinmod.on" : "message.spinmod.off"), true);
			}

			if (spinning && player.isAlive() && client.currentScreen == null) {
				player.setYaw(player.getYaw() + SPEED_DEGREES_PER_TICK);
			}
		});
	}
}
