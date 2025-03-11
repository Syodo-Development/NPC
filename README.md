# NPC

This is a NPC Plugin for PowerNukkitX

Use this plugin to spawn player statues. 
Unlike other NPC Plugins that were made for NukkitX, this NPC plugin saves your NPC data in a seperate folder.

If you want to spawn a NPC with a custom geometry, drop the texture file, and the geometry json in the SKINS folder. The geometrys identifier has to be geometry.unknown (blockbench default)
Then you can spawn a NPC with that Skin using /npc spawn npcname skinname. (Replace npcname by the name your NPC should be called. This name has to be unique. Replace skinname by the skinname)

If you want to spawn a NPC of yourself, use /npc save SKINNAME. This will save your skin as a NPC skin. Make your you're using NO premium or persona skins since they do not use textures and geometries.
If your skin uses a modified geormtry, check if the geometry identifier was replaced to geometry.unknown.

Do you want to modify your NPC? 
Then go into the NPCS folder and open the corresponding NPC file.
There you can modify everything like displayname, scale, commands etc.

To apply those changes, reload the world or restart your server.
