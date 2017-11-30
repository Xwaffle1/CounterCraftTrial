package com.ferullogaming.countercraft.game.tasks;

import java.util.TimerTask;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.game.arena.MobSpawner;

public class ZombieSpawnTask extends TimerTask {

	public int currentSpawned = 0;

	// Called Every Second.

	int seconds = 0;

	int activeZombies = 0;

	boolean sentMessage = false;

	@Override
	public void run() {
		seconds++;
		activeZombies = 0;
		System.out.println(CounterCraft.getInstance().getGame().getArena().getSpawners().size() + " SPAWNER COUNT");
		
		for (MobSpawner spawner : CounterCraft.getInstance().getGame().getArena().getSpawners()) {
			System.out.println("CALLED");
			spawner.onUpdate();
			if (currentSpawned < CounterCraft.getInstance().getGame().getArena().getMonsterLimit()) {
				if (seconds == 8) {
					spawner.spawn();
					currentSpawned++;
					
					if(spawner == CounterCraft.getInstance().getGame().getArena().getSpawners().get(CounterCraft.getInstance().getGame().getArena().getSpawners().size() - 1)) {
						seconds = 0;
					}
					
				}
			} else {
				if (activeZombies == 0) {
					currentSpawned = 0;
					seconds = 0;
					CounterCraft.getInstance().getGame().nextRound();
				}
			}
			activeZombies += spawner.zombies.size();
		}
		// CounterCraft.log("getMonsterLimit: " +
		// CounterCraft.getInstance().getGame().getArena().getMonsterLimit());
		// CounterCraft.log("currentSpawned: " + currentSpawned);
		// CounterCraft.log("Active: " + activeZombies);

	}

}
