package com.blindfold;

import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.BeforeMenuRender;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.PostHealthBar;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
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
		updateMinimapWidgetVisibility(config.hideMinimap());
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(hotkeyListener);
		updateMinimapWidgetVisibility(false);
	}

	@Provides
	BlindfoldConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BlindfoldConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals(BlindfoldConfig.GROUP))
		{
			return;
		}

		if (event.getKey().equals("hideMinimap"))
		{
			updateMinimapWidgetVisibility(config.hideMinimap());
			return;
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired scriptPostFired)
	{
		if (scriptPostFired.getScriptId() == ScriptID.TOPLEVEL_REDRAW)
		{
			updateMinimapWidgetVisibility(config.hideMinimap());
		}
	}

	private void updateMinimapWidgetVisibility(boolean enable)
	{
		final Widget resizableStonesWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_STONES_WIDGET);

		if (resizableStonesWidget != null)
		{
			resizableStonesWidget.setHidden(enable);
		}

		final Widget resizableNormalWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_WIDGET);

		if (resizableNormalWidget != null && !resizableNormalWidget.isSelfHidden())
		{
			for (Widget widget : resizableNormalWidget.getStaticChildren())
			{
				if (widget.getId() != WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_LOGOUT_BUTTON.getId() &&
						widget.getId() != WidgetInfo.RESIZABLE_MINIMAP_LOGOUT_BUTTON.getId())
				{
					widget.setHidden(enable);
				}
			}
		}
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event) {
		if (!renderBlindfold || !config.hideMenuEntries())
		{
			return;
		}
		final MenuEntry firstEntry = event.getFirstEntry();
		if (firstEntry == null) {
			return;
		}

		MenuEntry[] entries = event.getMenuEntries();

		for (MenuEntry entry : entries) {
			entry.setOption("");
			entry.setTarget("");
		}
	}


	@Subscribe
	public void onBeforeMenuRender(BeforeMenuRender event) {
		if (config.hideMenu()) {
			client.drawOriginalMenu(config.menuAlpha());
			event.consume();
		}
	}

}
