package com.laton95.pyramidplunder.advancements;

import com.laton95.pyramidplunder.PyramidPlunder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = PyramidPlunder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCriteriaTriggers {
	
	public static final ModCriterion LOOT_URN = new ModCriterion("loot_urn");
	
	public static final ModCriterion SNAKE_BITE = new ModCriterion("snake_bite");
	
	public static final ModCriterion CHARM_SNAKE = new ModCriterion("charm_snake");
	
	public static final ModCriterion[] TRIGGERS = new ModCriterion[] {
			LOOT_URN,
			SNAKE_BITE,
			CHARM_SNAKE
	};
	
	@SubscribeEvent
	public static void registerAdvancementTriggers(FMLCommonSetupEvent event) {
		for(ModCriterion trigger : TRIGGERS) {
			CriteriaTriggers.register(trigger);
		}
	}
}