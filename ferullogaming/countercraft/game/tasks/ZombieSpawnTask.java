package com.ferullogaming.countercraft.game.tasks;

import java.util.List;
import java.util.TimerTask;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.game.arena.MobSpawner;
import com.ferullogaming.countercraft.util.ChatColor;

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
			spawner.onUpdate();
			activeZombies += spawner.zombies.size();
			if (currentSpawned < CounterCraft.getInstance().getGame().getArena().getMonsterLimit()) {
				if (seconds >= (10 - CounterCraft.getInstance().getGame().getArena().getMonsterLimit())) { // 1 Second less for each round.
					spawner.spawn();
					currentSpawned++;

					if (spawner == CounterCraft.getInstance().getGame().getArena().getSpawners()
							.get(CounterCraft.getInstance().getGame().getArena().getSpawners().size() - 1)) {
						seconds = 0;
					}

				}
			}
		}

		if (activeZombies == 0 && currentSpawned >= CounterCraft.getInstance().getGame().getArena().getMonsterLimit()) {
			if (CounterCraft.getInstance().getGame().getRound() >= 10) {
				// YOU WIN

				List<PlayerData> data = CounterCraft.getInstance().getGame().getPlayerManager().getPlayerData();

				for (PlayerData playerData : data) {
					if (playerData.getPlayer() != null) {
						if (playerData.getPlayer().isInvisible()) // In Spectate mode no coins for you.
							continue;
						playerData.addCredits(15); // Max is 10 for dying on ten rounds.
					}
				}
				CounterCraft.getInstance().getGame().broadcast(ChatColor.GREEN + "Congrats, you've made it passed Round 10!");
				CounterCraft.getInstance().getGame().broadcast(ChatColor.GREEN + "You were awarded bonus credits!");
				cancel();
			} else {
				// Next Round.

				currentSpawned = 0;
				seconds = 0;
				CounterCraft.getInstance().getGame().nextRound();
			}
		}

		// CounterCraft.log("getMonsterLimit: " +
		// CounterCraft.getInstance().getGame().getArena().getMonsterLimit());
		// CounterCraft.log("currentSpawned: " + currentSpawned);
		// CounterCraft.log("Active: " + activeZombies);

	}

}
