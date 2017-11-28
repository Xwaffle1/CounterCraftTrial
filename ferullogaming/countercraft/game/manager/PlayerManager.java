package com.ferullogaming.countercraft.game.manager;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.EnumGameType;

public class PlayerManager {

	private List<PlayerData> playerData = new ArrayList<>();

	public List<PlayerData> getPlayerData() {
		return this.playerData;
	}

	public void onJoin(String playerName) {
		File associateFile = new File(
				CounterCraft.getInstance().folderLocation + File.separator + "playerData" + File.separator + playerName.toLowerCase() + ".json");

		if (!associateFile.exists()) {
			saveData(playerName.toLowerCase());
		}
		loadData(playerName.toLowerCase());

		if (playerData.size() == CounterCraft.getInstance().getGame().getPlayersNeeded()) {
			CounterCraft.getInstance().getGame().countdownLobby();
		}

	}

	public void onQuit(String playerName) {
		PlayerData data = getData(playerName);

		if (data != null) {
			saveData(playerName);
		}

		getPlayerData().remove(data);

	}

	public PlayerData getData(String username) {
		for (PlayerData data : CounterCraft.getInstance().getGame().getPlayerManager().getPlayerData()) {
			if (data.getUsername().equalsIgnoreCase(username)) {
				return data;
			}
		}
		return null;
	}

	public void loadData(String playerName) {
		if (getData(playerName) == null) {
			CounterCraft.getInstance().getGame().getPlayerManager().getPlayerData().add(new PlayerData(playerName));
		}
	}

	public void saveData(String playerName) {

		File associateFile = new File(
				CounterCraft.getInstance().folderLocation + File.separator + "playerData" + File.separator + playerName.toLowerCase() + ".json");

		File playerDir = new File(CounterCraft.getInstance().folderLocation + File.separator + "playerData");

		if (!playerDir.exists()) {
			playerDir.mkdirs();
		}
		if (!associateFile.exists()) {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject base = new JsonObject();
			JsonObject currency = new JsonObject();
			currency.addProperty("credits", 0);

			base.addProperty("kits", "");
			base.add("currency", currency);

			String prettyOutput = gson.toJson(base);

			try (FileWriter file = new FileWriter(associateFile)) {
				file.write(prettyOutput);
				file.flush();
				file.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		} else {
			PlayerData data = getData(playerName);

			associateFile.delete(); // DELETE LOCAL FILE
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject base = new JsonObject();
			JsonObject currency = new JsonObject();
			currency.addProperty("credits", data.getCredits());
			base.addProperty("kits", data.kitsToString());
			base.add("currency", currency);

			String prettyOutput = gson.toJson(base);

			try (FileWriter file = new FileWriter(associateFile)) {
				file.write(prettyOutput);
				file.flush();
				file.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	public void toggleSpectate(EntityPlayer player) {

		if (!player.isInvisible()) {
			player.setGameType(EnumGameType.CREATIVE);
			player.setInvisible(true);
		} else {
			player.setGameType(EnumGameType.SURVIVAL);
			player.setInvisible(false);
		}
	}

}
