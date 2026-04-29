package com.inventoryrestore;

public enum ItemDisplayFilter
{
	ALL("All Items"),
	FIRST_ONLY("First Item Only"),
	LAST_ONLY("Last Item Only");

	private final String displayName;

	ItemDisplayFilter(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
