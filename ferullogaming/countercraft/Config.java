package com.ferullogaming.countercraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ferullogaming.countercraft.game.arena.Arena;
import com.ferullogaming.countercraft.game.kits.Kit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ConfigCategory;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class Config {

	public static void initKits() {

		File kitFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "kits.json");

		if (!kitFile.exists()) {
			loadDefaultKitFile();
		}

		JsonParser parser = new JsonParser();

		try {
			JsonObject jsonObject = (JsonObject) parser.parse(new FileReader(kitFile));
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				if (entry == null) {
					CounterCraft.log("NULL ENTRY");
				}
				if (CounterCraft.getInstance() == null) {
					
				}
				if (CounterCraft.getInstance().getGame() == null) {

				}
				CounterCraft.getInstance().getGame().getKits().add(new Kit(entry.getKey()));
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// return config;
	}

	private static void loadDefaultKitFile() {

		CounterCraft.getInstance().log("CREATING KIT FILE");

		File kitFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "kits.json");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject kitsObject = new JsonObject();

		JsonObject stoneKit = new JsonObject();
		stoneKit.addProperty("name", "&aStone Kit");
		stoneKit.addProperty("cost", 100);
		JsonArray stoneKitItems = new JsonArray();
		stoneKitItems.add(new JsonPrimitive("272 1 name:&3&lAdventure's_Sword"));
		stoneKitItems.add(new JsonPrimitive("274 1 name:&3&lAdventure's_Pick"));
		stoneKit.add("items", stoneKitItems);

		JsonObject ironKit = new JsonObject();
		ironKit.addProperty("name", "&bIron Kit");
		ironKit.addProperty("cost", 100);
		JsonArray ironKitItems = new JsonArray();
		ironKitItems.add(new JsonPrimitive("267 1 name:&bMaster_Sword"));
		ironKitItems.add(new JsonPrimitive("257 1 name:&bMaster_Pick"));
		ironKit.add("items", ironKitItems);

		JsonObject diamondKit = new JsonObject();
		diamondKit.addProperty("name", "&dDiamond Kit");
		diamondKit.addProperty("cost", 100);
		JsonArray diamondKitItems = new JsonArray();
		diamondKitItems.add(new JsonPrimitive("276 1 name:&eLegendary_Sword"));
		diamondKitItems.add(new JsonPrimitive("278 1 name:&eLegendary_Pick"));
		diamondKit.add("items", diamondKitItems);

		kitsObject.add("stone-kit", stoneKit);
		kitsObject.add("iron-kit", ironKit);
		kitsObject.add("diamond-kit", diamondKit);

		String prettyOutput = gson.toJson(kitsObject);

		try (FileWriter file = new FileWriter(kitFile)) {
			file.write(prettyOutput);
			file.flush();
			file.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void init() {
		if (!CounterCraft.getInstance().folderLocation.exists()) {
			CounterCraft.getInstance().folderLocation.mkdirs();
		}

		initKits();
		initArena();
	}

	private static void initArena() {

		File arenaFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "arena.json");

		if (!arenaFile.exists()) {
			loadDefaultArenaFile();
		}

		JsonParser parser = new JsonParser();

		try {
			JsonObject arenaObject = (JsonObject) parser.parse(new FileReader(arenaFile));
			CounterCraft.getInstance().getGame().setArena(new Arena(arenaObject));
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void loadDefaultArenaFile() {
		CounterCraft.getInstance().log("CREATING ARENA FILE");

		File arenaFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "arena.json");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject arenaObject = new JsonObject();

		JsonObject lobbyObject = new JsonObject();
		lobbyObject.addProperty("x", 0);
		lobbyObject.addProperty("y", 0);
		lobbyObject.addProperty("z", 0);

		JsonObject playerSpawnObject = new JsonObject();
		playerSpawnObject.addProperty("x", 0);
		playerSpawnObject.addProperty("y", 0);
		playerSpawnObject.addProperty("z", 0);

		JsonArray mobSpawnersObject = new JsonArray();
		mobSpawnersObject.add(new JsonPrimitive("0,0,0"));
		mobSpawnersObject.add(new JsonPrimitive("1,1,1"));

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

	public static Kit getKit(String kitName) {
		for (Kit kit : CounterCraft.getInstance().getGame().getKits()) {
			if (kit.kitName.equalsIgnoreCase(kitName) || kit.kitKey.equalsIgnoreCase(kitName) || kit.kitKey.contains(kitName)
					|| kit.kitName.contains(kitName)) {
				return kit;
			}
		}

		return null;
	}

}
