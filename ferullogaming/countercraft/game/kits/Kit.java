package com.ferullogaming.countercraft.game.kits;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.w3c.dom.css.Counter;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.util.ChatColor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.Property;

public class Kit {

	public String kitKey;
	public String kitName;
	public double cost;
	public List<ItemStack> items = new ArrayList<>();

	public Kit(String kitKey) {
		CounterCraft.getInstance().log("Kit Found: " + kitKey);

		File kitFile = new File(CounterCraft.getInstance().folderLocation + File.separator + "kits.json");
		JsonParser parser = new JsonParser();
		try {
			JsonObject kitObject = ((JsonObject) parser.parse(new FileReader(kitFile))).get(kitKey).getAsJsonObject();

			this.kitKey = kitKey;
			this.kitName = ChatColor.translateAlternateColorCodes('&', kitObject.get("name").getAsString());
			this.cost = kitObject.get("cost").getAsDouble();

			JsonArray jsonItemArray = kitObject.get("items").getAsJsonArray();
			for (int i = 0; i < jsonItemArray.size(); i++) {
				JsonPrimitive itemObject = jsonItemArray.get(i).getAsJsonPrimitive();
				String itemString = itemObject.getAsString();
				ItemStack stack = createItemStack(itemString);
				if (stack != null) {
					items.add(stack);
					CounterCraft.log("   ADDED " + stack.getDisplayName());
				}
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private ItemStack createItemStack(String itemString) {
		String[] elements = itemString.split(" ");
		int itemID = Integer.parseInt(elements[0]);
		int ammount = Integer.parseInt(elements[1]);
		ItemStack stack = new ItemStack(itemID, ammount, 0);

		for (int i = 2; i < elements.length; i++) {
			String theString = elements[i];
			if (!theString.contains(":"))
				continue;
			String tag = theString.split(":")[0];
			String tagValue = theString.split(":")[1];
			switch (tag.toLowerCase()) {
			case "name":
				stack.setItemName(ChatColor.translateAlternateColorCodes('&', tagValue).replaceAll("_", " "));
				break;
			// TODO case enchant?
			// TODO case damageValue?
			// TODO case lore?
			}
		}
		// TODO MAKE CUSTOM STUFF
		return stack;
	}

	public void applyTo(EntityPlayer entityPlayer) {
		entityPlayer.inventory.clearInventory(-1, -1);
		for (ItemStack stack : items) {
			entityPlayer.inventory.addItemStackToInventory(stack.copy());
		}
		PlayerData data = CounterCraft.getInstance().getGame().getPlayerManager().getData(entityPlayer.username);
		data.setKit(this);

		entityPlayer.addChatMessage("Kit " + kitName + ChatColor.WHITE + " applied.");
	}

}
