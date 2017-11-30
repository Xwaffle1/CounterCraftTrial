package com.ferullogaming.countercraft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;

import com.ferullogaming.countercraft.commands.*;
import com.ferullogaming.countercraft.game.Game;
import com.ferullogaming.countercraft.listener.EntityListener;
import com.ferullogaming.countercraft.listener.PlayerListener;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumOS;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = CounterCraft.MOD_ID, name = CounterCraft.MOD_NAME, version = CounterCraft.MOD_VERSION)
public class CounterCraft {

	/*
	 * IF YOU REQUIRE ANY ASSISTANCE CONTACT THATABSTRACTWOLF VIA THE DEVELOPMENT
	 * TRIAL CHANNEL ON DISCORD.
	 */

	public static final String MC_VERSION = "1.6.4";

	public static final String MOD_ID = "CounterCraft";
	public static final String MOD_VERSION = "1.0.0";
	public static final String MOD_NAME = "Counter Craft";

	@Instance(CounterCraft.MOD_ID)
	private static CounterCraft instance;

	/** The asset folder's name */
	public static final String resLocation = "countercraft";

	/** Folder location for all the files */
	public File folderLocation = null;

	private Game game;

	public static CounterCraft getInstance() {
		return instance;
	}

	public Game getGame() {
		return this.game;
	}

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event) {
		log("Counter Craft Version %s found!", MOD_VERSION);
		log("Pre-Loading initialized...");
		this.folderLocation = new File(event.getSuggestedConfigurationFile().getParent().toString() + File.separator + "countercraft");

		MinecraftForge.EVENT_BUS.register(new EntityListener());
		MinecraftForge.EVENT_BUS.register(new PlayerListener());
		GameRegistry.registerPlayerTracker(new PlayerListener());

		if (FMLCommonHandler.instance().getSide().isClient()) {
			Display.setTitle("CounterCraft Trial");
			if (Util.getOSType() != EnumOS.MACOS) {

				try {
					Display.setIcon(new ByteBuffer[] {
							this.readImage(Minecraft.getMinecraft().getResourceManager()
									.getResource(new ResourceLocation(CounterCraft.resLocation + ":textures/gui/icon_16x16.png")).getInputStream()),
							this.readImage(Minecraft.getMinecraft().getResourceManager()
									.getResource(new ResourceLocation(CounterCraft.resLocation + ":textures/gui/icon_32x32.png")).getInputStream()) });
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		log("Main Loading initialized...");
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
		log("Post Loading initialized...");
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new KitCommand());
		event.registerServerCommand(new ArenaCommand());

	}

	@EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		// Config config = new Config();
		this.game = new Game();
		Config.init(); // INIT ARENA, KITS, ETC.

		MinecraftServer.getServer().executeCommand("kill @e[type=!Player]");

	}

	@EventHandler
	public void onServerStop(FMLServerStoppedEvent event) {
		CounterCraft.getInstance().getGame().getTimer().cancel();
	}

	/** Log information or anything counter craft related */
	public static void log(String par1, Object... par2) {
		String var1 = String.format(par1, par2);
		System.out.println("[Counter Craft] " + var1);
	}

	/**
	 * Used to read Input Stream image (for displaying window icon
	 * 
	 * @param par1File
	 * @return
	 * @throws IOException
	 */
	private ByteBuffer readImage(InputStream par1File) throws IOException {

		BufferedImage bufferedimage = ImageIO.read(par1File);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0, bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
		int[] aint1 = aint;
		int i = aint.length;

		for (int j = 0; j < i; ++j) {
			int k = aint1[j];
			bytebuffer.putInt(k << 8 | k >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}
}
