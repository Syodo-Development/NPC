package xyz.syodo.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import xyz.syodo.entity.EntityNPC;
import xyz.syodo.utils.NPCCreator;

public class NPCCommand extends Command {

    public NPCCommand() {
        super("npc");
        setPermission("syodo.npc");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player p) {
            if(args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "create":
                    case "spawn":
                        if(args.length == 3) {
                            String name = args[1];
                            String skin = args[2];
                            boolean couldCreate = NPCCreator.newNPC(name, skin, p.getLocation());
                            if(couldCreate) {
                                NPCCreator.spawnNPC(name);
                                p.sendMessage("§aNPC created successfully!");
                            } else {
                                p.sendMessage("§cFailed to create NPC!");
                            }
                        } else sendHelp(p);
                        break;
                    default:
                        sendHelp(p);
                        break;
                }
            } else sendHelp(p);
        } else sender.sendMessage("§cYou have to be a player to execute this command!");
        return true;
    }

    public void sendHelp(Player p) {
        p.sendMessage(
                "§l§eNPC HELP\n" +
                "/npc create [NAME] [SKIN]"
        );
    }

}
