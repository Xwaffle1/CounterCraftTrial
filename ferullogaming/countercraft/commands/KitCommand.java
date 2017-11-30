package com.ferullogaming.countercraft.commands;

import com.ferullogaming.countercraft.Config;
import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.game.kits.Kit;
import com.ferullogaming.countercraft.util.ChatColor;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;

public class KitCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "kit";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/kit kitName";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + ChatColor.BOLD.toString() + "KITS: "));

			for (Kit kit : CounterCraft.getInstance().getGame().getKits()) {
				sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.YELLOW + "  - " + kit.kitName));
			}
		} else if (args.length == 1) {
			if (sender instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer) sender;
				String kitName = "";
				for (String string : args) {
					kitName += string + " ";
				}
				kitName = kitName.trim();
				Kit kit = Config.getKit(kitName);

				PlayerData data = CounterCraft.getInstance().getGame().getPlayerManager().getData(entityPlayer.username);
				if (kit != null) {
					if (data.hasKit(kit.kitKey)) {
						kit.applyTo(entityPlayer);
					} else {
						entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "You do not OWN this kit."));
						entityPlayer.sendChatToPlayer(ChatMessageComponent
								.createFromText(ChatColor.RED + "You may purchase this kit for " + ChatColor.GRAY + kit.cost + ChatColor.YELLOW + " Credits."));
						entityPlayer.sendChatToPlayer(ChatMessageComponent
								.createFromText(ChatColor.GREEN + "Type /kit buy " + kit.kitName + " " + ChatColor.GREEN + " to purchase."));
					}
				}
			}
		} else if (args.length >= 2 && args[0].equalsIgnoreCase("buy")) {
			if (sender instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer) sender;
				String kitName = "";
				for (int i = 1; i < args.length; i++) {
					String string = args[i];
					kitName += string + " ";
				}
				kitName = kitName.trim();
				Kit kit = Config.getKit(kitName);
				PlayerData data = CounterCraft.getInstance().getGame().getPlayerManager().getData(entityPlayer.username);
				if (kit != null) {
					if (data.getCredits() < kit.cost) {
						entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "You do not have " + ChatColor.GRAY + kit.cost
								+ ChatColor.YELLOW + " Credits " + ChatColor.RED + " to purchase this kit."));
						return;
					}
					data.buyKit(kit);
				} else {
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "That Kit does not exhist."));
				}
			}
		} else if (args[0].equalsIgnoreCase("credits")) {
			if (sender instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer) sender;
				PlayerData data = CounterCraft.getInstance().getGame().getPlayerManager().getData(entityPlayer.username);
				data.sendCredits();
			}

		}
	}
}