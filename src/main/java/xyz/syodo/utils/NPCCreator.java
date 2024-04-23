package xyz.syodo.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import lombok.SneakyThrows;
import xyz.syodo.NPC;
import xyz.syodo.entity.EntityNPC;

import java.io.File;

public class NPCCreator {

    @SneakyThrows
    public static boolean newNPC(String name, String skin, Location location) {
        File file = new File(NPC.get().getDataFolder() + "/NPCS", name + ".yml");
        File skinfile = new File(NPC.get().getDataFolder() + "/SKINS", skin + ".json");
        if(!skinfile.exists()) return false;
        if(file.exists()) return false;
        file.createNewFile();
        Config config = new Config(file);
        config.set("name", name);
        config.set("skin", skin);
        config.set("scale", 1.0f);
        config.set("location.x", location.x);
        config.set("location.y", location.y);
        config.set("location.z", location.z);
        config.set("location.yaw", location.yaw);
        config.set("location.pitch", location.pitch);
        config.set("location.level", location.level.getFolderName());
        config.save();
        return true;
    }

    public static EntityNPC spawnNPC(String name) {
        File file = new File(NPC.get().getDataFolder() + "/NPCS", name + ".yml");
        if(file.exists()) {
            Config config = new Config(file);
            Location location = new Location(
                    config.getDouble("location.x"),
                    config.getDouble("location.y"),
                    config.getDouble("location.z"),
                    config.getDouble("location.yaw"),
                    config.getDouble("location.pitch"),
                    Server.getInstance().getLevelByName(config.getString("location.level"))
            );
            EntityNPC npc = new EntityNPC(location.getChunk(), EntityNPC.nbt(location, config.getString("name"), config.getString("skin"), (float) config.getDouble("scale")));
            npc.spawnToAll();
            return npc;
        }
        return null;
    }
}
