package com.ferullogaming.countercraft.commands;

import com.ferullogaming.countercraft.Config;
import com.ferullogaming.countercraft.CounterCraft;
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
		} else {
			if (sender instanceof EntityPlayer) {
				EntityPlayer entityPlayer = (EntityPlayer) sender;
				String kitName = "";
				for (String string : args) {
					kitName += string + " ";
				}
				kitName = kitName.trim();
				Kit kit = Config.getKit(kitName);
				if (kit != null) {
					kit.applyTo(entityPlayer);
				}
			}

		}
	}

}