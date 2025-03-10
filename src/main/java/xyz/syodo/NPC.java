package xyz.syodo;

import java.io.File;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.registry.EntityRegistry;
import cn.nukkit.registry.RegisterException;
import cn.nukkit.registry.Registries;
import xyz.syodo.commands.NPCCommand;
import xyz.syodo.entity.EntityNPC;
import xyz.syodo.listener.ChunkListener;
import xyz.syodo.listener.DamageListener;
import xyz.syodo.listener.InteractListener;
import xyz.syodo.utils.NPCCreator;

public class NPC extends PluginBase {

	private static Plugin plugin;

	public void onLoad() {
		try {
			Registries.ENTITY.registerCustomEntity(this, new EntityRegistry.CustomEntityDefinition("syodo:npc", "", false, true), EntityNPC.class);
		} catch (RegisterException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {
		plugin = this;
		Server.getInstance().getCommandMap().register("npc", new NPCCommand());
		Server.getInstance().getPluginManager().registerEvents(new ChunkListener(), get());
		Server.getInstance().getPluginManager().registerEvents(new DamageListener(), get());
		Server.getInstance().getPluginManager().registerEvents(new InteractListener(), get());
		File pluginFolder = get().getDataFolder();
		pluginFolder.mkdir();
		File npcs = new File(pluginFolder, "NPCS");
		npcs.mkdir();
		File skins = new File(pluginFolder, "SKINS");
		skins.mkdir();

		for(File f : npcs.listFiles()) {
			NPCCreator.spawnNPC(f.getName().replace(".yml", ""));
		}

	}
	
	public static Plugin get() {
		return plugin;
	}
	
}
