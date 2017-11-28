package com.ferullogaming.countercraft.game.arena;

import java.util.ArrayList;
import java.util.List;

import com.ferullogaming.countercraft.util.Location;

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

	}

	public void spawn() {
		EntityZombie zombie = new EntityZombie(loc.getWorld());
		NBTTagCompound nbt = zombie.getEntityData();

		nbt.setBoolean("mobspawner", true);
		zombie.targetTasks.addTask(10, new EntityAINearestAttackableTarget(zombie, EntityPlayer.class, 100, true)); // Find nearest player and attack
		loc.getWorld().spawnEntityInWorld(zombie);
		zombie.setPosition(loc.getX(), loc.getY(), loc.getZ());
		nbt.setBoolean("mobspawner", true); // Again just in case
		System.out.println("Zombie Spawned at " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
		zombies.add(zombie);
	}

}
