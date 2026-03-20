package com.inventoryrestore;

public enum OverlayTextPosition
{
	TOP_LEFT("Top Left"),
	TOP_RIGHT("Top Right"),
	BOTTOM_LEFT("Bottom Left"),
	BOTTOM_RIGHT("Bottom Right"),
	CENTER("Center");

	private final String displayName;

	OverlayTextPosition(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
