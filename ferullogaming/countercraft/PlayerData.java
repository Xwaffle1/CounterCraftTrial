package com.ferullogaming.countercraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ferullogaming.countercraft.game.kits.Kit;
import com.ferullogaming.countercraft.util.ChatColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class PlayerData {

	private int credits;
	// TODO Put to UUID when new version.
	private String username;
	private String[] kitsOwned = new String[] {};

	private boolean selectedKit = false;

	private Kit kit = null;

	public String getUsername() {
		return this.username;
	}

	public int getCredits() {
		return this.credits;
	}

	public String kitsToString() {
		String kitsString = "";

		for (String kit : kitsOwned) {
			kitsString += kit;

			if (!(kit.equalsIgnoreCase(kitsOwned[kitsOwned.length - 1]))) {
				kitsString += ",";
			}
		}

		return kitsString;
	}

	public EntityPlayer getPlayer() {
		return MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(this.username);
	}

	/**
	 * 
	 * @param kit
	 * @return Checks to see if the KIT KEY is a part of the kits Owned array.
	 */
	public boolean hasKit(String kit) {
		for (String kitKey : kitsOwned) {
			if (kit.equalsIgnoreCase(kitKey))
				return true;
		}
		return false;
	}

	public void buyKit(Kit kit) {
		if (!this.hasKit(kit.kitKey)) {
			this.takeCredits((int) kit.cost);

			String[] tempKits = new String[this.kitsOwned.length + 1];

			for (int i = 0; i < this.kitsOwned.length; i++) {
				tempKits[i] = kitsOwned[i];
			}
			tempKits[this.kitsOwned.length] = kit.kitKey;

			kitsOwned = tempKits;

			EntityPlayer player = getPlayer();

			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "- " + kit.cost + ChatColor.GRAY + " Credits"));
				player.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + "Purchased " + kit.kitName));
				sendCredits();

			}
			save();
		}
	}

	public void sendCredits() {
		getPlayer().sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + "Current Credits: " + ChatColor.YELLOW + "" + getCredits()));
	}

	private void save() {
		CounterCraft.getInstance().getGame().getPlayerManager().saveData(username);
	}

	public PlayerData(String username) {
		this.username = username;
		File file = new File(CounterCraft.getInstance().folderLocation + File.separator + "playerData" + File.separator + username.toLowerCase() + ".json");

		JsonParser parser = new JsonParser();

		try {
			JsonObject jsonObject = (JsonObject) parser.parse(new FileReader(file));
			JsonObject currencyObject = jsonObject.getAsJsonObject("currency");
			credits = currencyObject.get("credits").getAsInt();
			kitsOwned = jsonObject.get("kits").getAsString().split(",");
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean choseKit() {
		return kit != null;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

	public Kit getKit() {
		return this.kit;
	}

	public void addCredits(int credits) {
		this.credits += credits;
		if (getPlayer() != null) {
			getPlayer()
					.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.YELLOW + "+ " + ChatColor.GREEN + credits + ChatColor.GRAY + " Credits"));
			sendCredits();
		}
		save();
	}

	public void takeCredits(int credits) {
		this.credits -= credits;
		save();
	}

}
