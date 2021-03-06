package com.blindfold;

import net.runelite.client.config.*;

import java.awt.Color;

@ConfigGroup(BlindfoldConfig.GROUP)
public interface BlindfoldConfig extends Config
{
	String GROUP = "blindfold";
	@ConfigItem(
			keyName = "color",
			name = "Blindfold Color",
			description = "The color of the blindfold",
			position = 0
	)
	default Color blindfoldColor()
	{
		return new Color(0, 0, 0);
	}

	@ConfigItem(
			keyName = "playerColor",
			name = "Player color",
			description = "The color of the player",
			position = 1
	)
	default Color playerColor()
	{
		return new Color(0xFF, 0xFF, 0xFF);
	}

	@ConfigItem(
			keyName = "toggleKey",
			name= "Toggle Key",
			description = "Key to press to toggle blindfold",
			position = 1
	)
	default Keybind hotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			keyName = "defaultState",
			name = "Should Default On",
			description = "What state should the blindfold default to",
			position = 2
	)
	default boolean defaultState()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideMinimap",
			name = "Hide minimap",
			description = "Do not show the minimap on screen (Resizable only)"
	)
	default boolean hideMinimap()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideMenuEntries",
			name = "Hide MenuEntries",
			description = "Do not show right click options"
	)
	default boolean hideMenuEntries()
	{
		return false;
	}

	@ConfigItem(
			keyName = "hidePlayer",
			name = "Hide player",
			description = "Do not show a cutout of the player model"
	)
	default boolean hidePlayer()
	{
		return false;
	}

	@ConfigItem(
			keyName = "hideMenu",
			name = "Hide menu",
			description = "Do not show menu overlay at all"
	)
	default boolean hideMenu()
	{
		return false;
	}

	@Range(
			max = 255
	)
	@ConfigItem(
			keyName = "menuAlpha",
			name = "Menu alpha",
			description = "Configures the transparency of the right-click menu"
	)
	default int menuAlpha()
	{
		return 0;
	}

	@ConfigItem(
			keyName = "hideCrosses",
			name = "Hide crosses",
			description = "Do not show yellow/red crosses when clicking"
	)
	default boolean hideCrosses()
	{
		return false;
	}
}
