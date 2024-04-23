package xyz.syodo.listener;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.ChunkUnloadEvent;
import cn.nukkit.level.format.IChunk;
import xyz.syodo.entity.EntityNPC;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        IChunk chunk = e.getChunk();
        for(Entity entity : chunk.getEntities().values()) {
            if(entity instanceof EntityNPC) {
                e.setCancelled();
                return;
            }
        }
    }

}
