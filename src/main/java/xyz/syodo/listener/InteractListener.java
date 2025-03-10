package xyz.syodo.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import xyz.syodo.entity.EntityNPC;

public class InteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if(event.getEntity() instanceof EntityNPC npc) {
            interact(event.getPlayer(), npc);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            if(event.getEntity() instanceof EntityNPC npc) {
                interact(player, npc);
            }
        }
    }

    private void interact(Player player, EntityNPC npc)  {
        if(npc.namedTag.containsList("playerCommands", Tag.TAG_String)) {
            npc.namedTag.getList("playerCommands", StringTag.class).getAll().forEach(tag -> executeCommand(player, tag.parseValue(), player, npc));
        }
        if(npc.namedTag.containsList("consoleCommands", Tag.TAG_String)) {
            npc.namedTag.getList("consoleCommands", StringTag.class).getAll().forEach(tag -> executeCommand(Server.getInstance().getConsoleSender(), tag.parseValue(), player, npc));
        }
    }

    private void executeCommand(CommandSender commandSender, final String command, Player actor, EntityNPC npc) {
        final String finalCommand = command
                .replaceAll("\\{username\\}", actor.getName())
                .replaceAll("\\{display\\}", actor.getDisplayName())
                .replaceAll("\\{uuid\\}", actor.getUniqueId().toString())
                .replaceAll("\\{xuid\\}", actor.getLoginChainData().getXUID())
                .replaceAll("\\{id\\}", String.valueOf(actor.getId()))
                .replaceAll("\\{npc_name\\}", npc.getName())
                .replaceAll("\\{npc_id\\}", String.valueOf(npc.getId()));

        Server.getInstance().executeCommand(commandSender, finalCommand);
    }

}
