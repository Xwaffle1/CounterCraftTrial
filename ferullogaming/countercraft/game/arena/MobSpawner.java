package com.ferullogaming.countercraft.game.arena;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.util.Location;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class MobSpawner {

	public Location loc = null;

	public List<EntityZombie> zombies = new ArrayList<>();

	public MobSpawner(String locString) {
		loc = new Location(MinecraftServer.getServer().getEntityWorld(), Integer.parseInt(locString.split(",")[0]), Integer.parseInt(locString.split(",")[1]),
				Integer.parseInt(locString.split(",")[2]));
		System.out.println("New Mob Spawner: " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
	}

	public void onUpdate() {
		Iterator<EntityZombie> iterator = zombies.iterator();
		while (iterator.hasNext()) {
			EntityZombie zombie = iterator.next();
			if (zombie.isDead) {
				iterator.remove();
			}
		}
	}

	public void spawn() {

		EntityZombie zombie = new EntityZombie(loc.getWorld());
		NBTTagCompound nbt = zombie.getEntityData();

		nbt.setBoolean("mobspawner", true);
		zombie.targetTasks.addTask(10, new EntityAINearestAttackableTarget(zombie, EntityPlayer.class, 100, true)); // Find nearest player and attack
		zombie.setPosition(loc.getX(), loc.getY(), loc.getZ());
		zombie.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), 0, 0); // Just in case.
		loc.getWorld().spawnEntityInWorld(zombie);
		zombie.setPosition(loc.getX(), loc.getY(), loc.getZ());
		zombie.setLocationAndAngles(loc.getX(), loc.getY(), loc.getZ(), 0, 0);

		zombie.fireResistance = Integer.MAX_VALUE;
		System.out.println("Zombie Spawned at " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
		zombies.add(zombie);
	}

}
