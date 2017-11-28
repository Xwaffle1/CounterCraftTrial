package com.ferullogaming.countercraft.listener;

import java.io.File;

import com.ferullogaming.countercraft.CounterCraft;
import com.ferullogaming.countercraft.PlayerData;
import com.ferullogaming.countercraft.game.Phase;
import com.ferullogaming.countercraft.util.ChatColor;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumGameType;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;

public class PlayerListener implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		player.setGameType(EnumGameType.SURVIVAL);
		player.setInvisible(false);
		player.posX = CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getX();
		player.posY = CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getY();
		player.posZ = CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getZ();
		player.setPosition(CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getX(),
				CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getY(),
				CounterCraft.getInstance().getGame().getArena().getLobbyLocation().getZ());
		CounterCraft.getInstance().getGame().broadcast(ChatColor.WHITE + player.username + ChatColor.YELLOW + " has joined the game.");
		CounterCraft.getInstance().getGame().getPlayerManager().onJoin(player.username.toLowerCase());
		CounterCraft.getInstance().getGame().broadcast(
				(CounterCraft.getInstance().getGame().getPlayersNeeded() - CounterCraft.getInstance().getGame().getPlayerManager().getPlayerData().size())
						+ " players needed to start.");

		player.addChatMessage(ChatColor.YELLOW + "Welcome, to " + ChatColor.RED + "CounterCraft.");
		player.addChatMessage(ChatColor.YELLOW + "Please type " + ChatColor.GREEN + "/kit " + ChatColor.YELLOW + "to see a list of available kits.");
		player.addChatMessage(ChatColor.YELLOW + "Then use " + ChatColor.GREEN + "/kit <kit name>" + ChatColor.YELLOW + " to obtain it!");

		player.inventory.clearInventory(-1, -1);

		MinecraftServer.getServer().getCommandManager().executeCommand(player, "kit");
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		CounterCraft.getInstance().getGame().getPlayerManager().onJoin(player.username.toLowerCase());
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {

	}

	@ForgeSubscribe
	public void onBlockBreak(BlockEvent.HarvestDropsEvent event) {
		// event.setCanceled(true);
		// UNCANNCELABLE
		if (event.harvester != null)
			if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(event.harvester.username)) {
				return;
			}
		event.drops.clear();
	}

	@ForgeSubscribe
	public void onBlockPlace(PlayerInteractEvent event) {
		if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(event.entityPlayer.username)) {
			return;
		}

		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.getCurrentEquippedItem() != null
				&& Block.isAssociatedBlockID(event.entityPlayer.getCurrentEquippedItem().itemID, event.entityPlayer.getCurrentEquippedItem().itemID)) {

			// Cancel placing of blocks ?
			event.setCanceled(true);
		}

		if (event.action == Action.LEFT_CLICK_BLOCK) {
			event.setCanceled(true);
		}

	}

	@ForgeSubscribe
	public void onPlayerDeath(LivingHurtEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			float newHealth = player.getHealth() - event.ammount;
			if (newHealth <= 0) {
				CounterCraft.getInstance().getGame().getPlayerManager().toggleSpectate(player);
				event.setCanceled(true);
			}
		}
	}

	@ForgeSubscribe
	public void onDmgDuringLobby(LivingHurtEvent event) {
		if (CounterCraft.getInstance().getGame().getPhase() != Phase.LOBBY) {
			return;
		}
		event.setCanceled(true);
	}

}
