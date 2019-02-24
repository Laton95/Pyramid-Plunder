package com.laton95.pyramidplunder.advancement;

public class Triggers
{
	public static final CustomTrigger LOOT_URN = new CustomTrigger("loot_urn");
	
	public static final CustomTrigger SNAKE_BITE = new CustomTrigger("snake_bite");
	
	public static final CustomTrigger CHARM_SNAKE = new CustomTrigger("charm_snake");
	
	public static final CustomTrigger[] TRIGGER_ARRAY = new CustomTrigger[] {
			LOOT_URN,
			SNAKE_BITE,
			CHARM_SNAKE
	};
}