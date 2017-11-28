package com.ferullogaming.countercraft.game.arena;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.util.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class Arena {
	private List<MobSpawner> spawners = new ArrayList<>();
	private Location lobbyLoc = null;
	private Location playerSpawn = null;

	public Arena(JsonObject arenaObject) {
		JsonObject lobbyObject = arenaObject.get("lobby").getAsJsonObject();
		lobbyLoc = new Location(MinecraftServer.getServer().getEntityWorld(), lobbyObject.get("x").getAsInt(), lobbyObject.get("y").getAsInt(),
				lobbyObject.get("z").getAsInt());

		JsonObject playerSpawnObject = arenaObject.get("player-spawn").getAsJsonObject();
		playerSpawn = new Location(MinecraftServer.getServer().getEntityWorld(), playerSpawnObject.get("x").getAsInt(), playerSpawnObject.get("y").getAsInt(),
				playerSpawnObject.get("z").getAsInt());

		JsonArray spawnersArray = arenaObject.get("spawners").getAsJsonArray();

		for (int i = 0; i < spawnersArray.size(); i++) {
			String spawnerString = spawnersArray.get(i).getAsJsonPrimitive().getAsString();
			spawners.add(new MobSpawner(spawnerString));
		}

		CounterCraft.log("LOBBY: " + lobbyLoc.getX() + " " + lobbyLoc.getY() + " " + lobbyLoc.getZ());

	}

	public int getMonsterLimit() {
		return (10 * CounterCraft.getInstance().getGame().getRound()) * CounterCraft.getInstance().getGame().getArena().getSpawners().size();
	}

	public Location getLobbyLocation() {
		return lobbyLoc;
	}

	public Location getPlayerSpawn() {
		return this.playerSpawn;
	}

	public List<MobSpawner> getSpawners() {
		return spawners;
	}

	public void teleportPlayersToSpawn() {

		List<PlayerData> data = CounterCraft.getInstance().getGame().getPlayerManager().getPlayerData();

		for (PlayerData playerData : data) {
			EntityPlayer player = playerData.getPlayer();
			if (player != null) {
				player.setPosition(this.getPlayerSpawn().getX(), this.getPlayerSpawn().getY(), this.getPlayerSpawn().getZ());
				player.posX = this.getPlayerSpawn().getX();
				player.posY = this.getPlayerSpawn().getY();
				player.posZ = this.getPlayerSpawn().getZ();

			}
		}

	}

	public void removeSpawner(Location location) {

	}

	public void addSpawner(Location location) {
		int x = (int) location.getX();
		int y = (int) location.getY();
		int z = (int) location.getZ();
		this.getSpawners().add(new MobSpawner(x + "," + y + "," + z));
		saveArena();
	}

	private void saveArena() {
		CounterCraft.getInstance().log("ARENA FILE SAVED");

		File arenaFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "arena.json");

		arenaFile.delete();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject arenaObject = new JsonObject();

		JsonObject lobbyObject = new JsonObject();
		lobbyObject.addProperty("x", (int) lobbyLoc.getX());
		lobbyObject.addProperty("y", (int) lobbyLoc.getY());
		lobbyObject.addProperty("z", (int) lobbyLoc.getZ());

		JsonObject playerSpawnObject = new JsonObject();
		playerSpawnObject.addProperty("x", (int) this.playerSpawn.getX());
		playerSpawnObject.addProperty("y", (int) playerSpawn.getY());
		playerSpawnObject.addProperty("z", (int) playerSpawn.getZ());

		JsonArray mobSpawnersObject = new JsonArray();

		for (MobSpawner spawner : getSpawners()) {
			mobSpawnersObject.add(new JsonPrimitive((int) spawner.loc.getX() + "," + (int) spawner.loc.getY() + "," + (int) spawner.loc.getZ()));

		}
		arenaObject.add("lobby", lobbyObject);
		arenaObject.add("player-spawn", playerSpawnObject);
		arenaObject.add("spawners", mobSpawnersObject);

		String prettyOutput = gson.toJson(arenaObject);

		try (FileWriter file = new FileWriter(arenaFile)) {
			file.write(prettyOutput);
			file.flush();
			file.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void setPlayerSpawn(Location location) {
		this.playerSpawn = location;
		saveArena();
	}

	public void setLobby(Location location) {
		this.lobbyLoc = location;
		saveArena();
	}

}
