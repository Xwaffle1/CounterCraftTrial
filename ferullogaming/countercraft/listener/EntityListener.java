package com.ferullogaming.countercraft.listener;

import com.ferullogaming.countercraft.CounterCraft;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class EntityListener {

	@ForgeSubscribe
	public void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.entity instanceof EntityPlayer || CounterCraft.getInstance().getGame().isKnownEntity(event.entity.getUniqueID())) {
			return;
		}

		if (!(event.entity instanceof EntityCreature))
			return;

		event.setResult(Result.DENY);
	}

	@ForgeSubscribe
	public void onEntityDeath(LivingDeathEvent event) {
		if (CounterCraft.getInstance().getGame().isKnownEntity(event.entityLiving.getUniqueID())) {

		}
	}

	@ForgeSubscribe
	public void onUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityZombie)
			event.entityLiving.extinguish();
	}

}
