package com.ferullogaming.countercraft.game.tasks;

import java.util.TimerTask;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.game.arena.MobSpawner;

public class ZombieSpawnTask extends TimerTask {

	public int currentSpawned = 0;

	@Override
	public void run() {
		System.out.println("CALLED");
		for (MobSpawner spawner : CounterCraft.getInstance().getGame().getArena().getSpawners()) {
			if (currentSpawned >= CounterCraft.getInstance().getGame().getArena().getMonsterLimit()) {
				cancel();
				break;
			}
			spawner.spawn();
		}
	}

}
