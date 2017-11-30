package com.ferullogaming.countercraft.commands;

import com.ferullogaming.countercraft.Config;
import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.game.kits.Kit;
import com.ferullogaming.countercraft.util.ChatColor;
import com.ferullogaming.countercraft.util.Location;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;

public class ArenaCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "arena";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/arena";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "/arena setlobby"));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "/arena setplayerspawn"));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "/arena addspawner"));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.RED + "/arena removespawner"));
		} else {
			if (sender instanceof EntityPlayer) {

				EntityPlayer entityPlayer = (EntityPlayer) sender;
				if (!MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(entityPlayer.username))
					return;

				switch (args[0].toLowerCase()) {
				case "setlobby":
					CounterCraft.getInstance().getGame().getArena()
							.setLobby(new Location(entityPlayer.getEntityWorld(), entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + " Lobby Location set!"));
					break;
				case "setplayerspawn":
					CounterCraft.getInstance().getGame().getArena()
							.setPlayerSpawn(new Location(entityPlayer.getEntityWorld(), entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + " Player Spawn set!"));
					break;
				case "addspawner":
					CounterCraft.getInstance().getGame().getArena()
							.addSpawner(new Location(entityPlayer.getEntityWorld(), entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + "Spawner Added!"));
					break;
				case "removespawner":
					CounterCraft.getInstance().getGame().getArena()
							.removeSpawner(new Location(entityPlayer.getEntityWorld(), entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText(ChatColor.GREEN + "Spawner Removed!"));
					break;
				case "start":
					CounterCraft.getInstance().getGame().countdownLobby();
					break;
				case "zombie":
					EntityZombie zombie = new EntityZombie(MinecraftServer.getServer().getEntityWorld());
					zombie.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
					MinecraftServer.getServer().getEntityWorld().spawnEntityInWorld(zombie);
					zombie.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
					zombie.setLocationAndAngles(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, 0, 0);
					break;
				}
			}

		}
	}

}