package com.ferullogaming.countercraft.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import com.ferullogaming.countercraft.Config;
import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.game.arena.Arena;
import com.ferullogaming.countercraft.game.arena.MobSpawner;
import com.ferullogaming.countercraft.game.kits.Kit;
import com.ferullogaming.countercraft.game.manager.PlayerManager;
import com.ferullogaming.countercraft.game.tasks.RunnableTask;
import com.ferullogaming.countercraft.game.tasks.ZombieSpawnTask;
import com.ferullogaming.countercraft.game.tasks.countdown.CountdownStartGameTask;
import com.ferullogaming.countercraft.util.ChatColor;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class Game {

	private Arena arena = null;

	private List<Kit> kits = new ArrayList<>();

	private Phase phase = Phase.LOBBY;

	private int round = 0;

	private PlayerManager playerManager = new PlayerManager();

	private int playersToStart = 4;

	private Timer timer = new Timer();

	/*
	 * Current monsters killed in this round.
	 */
	private int currentKilled = 0;

	public Game() {

		setPhase(Phase.LOBBY);

	}

	public Timer getTimer() {
		return timer;
	}

	public int getCurrentKilled() {
		return currentKilled;
	}

	public void incrementCurrentKilled() {
		currentKilled += 1;
	}

	public int getRound() {
		return round;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public int getPlayersNeeded() {
		return this.playersToStart;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public Arena getArena() {
		return this.arena;
	}

	public List<Kit> getKits() {
		return kits;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
		CounterCraft.log(this.phase.name() + " PHASE HAS BEGUN");
	}

	public Phase getPhase() {

		return this.phase;
	}

	public void broadcast(String message) {
		for (String username : MinecraftServer.getServer().getAllUsernames()) {
			EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);
			player.sendChatToPlayer(
					ChatMessageComponent.createFromText(ChatColor.GRAY + "[" + ChatColor.RED + "CounterCraft" + ChatColor.GRAY + "] " + message));
		}
	}

	public void nextRound() {
		round++;
		broadcast(ChatColor.YELLOW + "Round " + ChatColor.GRAY + round + ChatColor.YELLOW + " Started.");
		currentKilled = 0;
	}

	public void startGame() {
		this.setPhase(Phase.GAME_STARTING);
		getArena().teleportPlayersToSpawn();
		giveKits();
		broadcast(ChatColor.YELLOW + "Game Started!");
		nextRound();
		this.setPhase(Phase.GAME_STARTED);
		
		timer.schedule(new ZombieSpawnTask(), 5000, 1000); // Every 5 Seconds go to task, Tasks starts in 5 Seconds.

	}

	/**
	 * Give players who did not select a kit, a default kit.
	 */
	private void giveKits() {
		Kit kit = Config.getKit("wood-kit");
		for (PlayerData data : getPlayerManager().getPlayerData()) {
			if (data.getPlayer() != null && !data.choseKit()) {
				kit.applyTo(data.getPlayer());
			}
		}
	}

	public void countdownLobby() {
		broadcast(ChatColor.YELLOW + "Game starting soon.");
		getTimer().scheduleAtFixedRate(new CountdownStartGameTask(), 5000, 1000);

	}

	public boolean isKnownEntity(UUID uniqueID) {
		for (MobSpawner spawner : getArena().getSpawners()) {
			for (EntityZombie zombie : spawner.zombies) {
				if (zombie.getUniqueID().toString().equalsIgnoreCase(uniqueID.toString())) {
					System.out.println("KNOWN ENTITY SPAWNED");
					return true;
				}
			}
		}
		return false;
	}

}
