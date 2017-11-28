package com.ferullogaming.countercraft.game.tasks.countdown;

import java.util.TimerTask;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.util.ChatColor;

public class CountdownStartGameTask extends TimerTask {

	int runs = 0;

	@Override
	public void run() {
		runs++;
		if (runs == 10) {
			CounterCraft.getInstance().getGame().startGame();
			cancel();
			return;
		}
		CounterCraft.getInstance().getGame()
				.broadcast("Game Starts" + ChatColor.YELLOW + " in " + ChatColor.RED + (10 - runs) + ChatColor.YELLOW + " seconds.");

	}

}
