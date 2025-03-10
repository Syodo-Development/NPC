package xyz.syodo.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import xyz.syodo.entity.EntityNPC;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof EntityNPC) {
            event.setCancelled();
        }
    }
}
