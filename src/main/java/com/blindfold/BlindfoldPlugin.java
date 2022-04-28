package com.blindfold;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
	name = "Blindfold"
)
public class BlindfoldPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BlindfoldConfig config;

	@Inject
	private BlindfoldOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Getter
	private boolean renderBlindfold;

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			renderBlindfold = !renderBlindfold;
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		renderBlindfold = config.defaultState();
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Provides
	BlindfoldConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BlindfoldConfig.class);
	}
}
