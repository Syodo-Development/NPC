package xyz.syodo.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.custom.CustomEntity;
import cn.nukkit.entity.data.EntityDataMap;
import cn.nukkit.entity.data.EntityDataType;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.inventory.HumanInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.utils.PersonaPiece;
import cn.nukkit.utils.PersonaPieceTint;
import cn.nukkit.utils.SkinAnimation;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityNPC extends EntityHuman implements CustomEntity {

    public EntityNPC(IChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        setScale(namedTag.getFloat("Scale"));
        setNameTag(namedTag.getString("NameTag"));
        setNameTagAlwaysVisible(true);
    }

    @Override
    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);
            this.server.updatePlayerListData(this.getUniqueId(), this.getId(), this.getName(), this.checkSkin(this.skin), new Player[]{player});

            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = this.getUniqueId();
            pk.username = this.getName();
            pk.entityUniqueId = this.getId();
            pk.entityRuntimeId = this.getId();
            pk.x = (float) this.x;
            pk.y = (float) this.y;
            pk.z = (float) this.z;
            pk.speedX = (float) this.motionX;
            pk.speedY = (float) this.motionY;
            pk.speedZ = (float) this.motionZ;
            pk.yaw = (float) this.yaw;
            pk.pitch = (float) this.pitch;
            pk.item = this.inventory.getItemInHand();
            pk.entityData = this.entityDataMap.copy(this.entityDataMap.keySet().toArray(new EntityDataType[this.entityDataMap.keySet().size()]));
            player.dataPacket(pk);

            this.inventory.sendHeldItem(player);
            this.inventory.sendArmorContents(player);
            this.offhandInventory.sendContents(player);

            this.server.removePlayerListData(this.getUniqueId(), new Player[]{player});
        }
    }

    @Override
    public boolean attack(float damage) {
        Server.getInstance().broadcastMessage(this.getId() + "");
        return super.attack(damage);
    }

    private Skin checkSkin(Skin skin) {
        skin.setTrusted(true);
        if (!skin.isPersona()) {
            skin.setFullSkinId(UUID.randomUUID().toString());
        }
        return skin;
    }

    private void setInventories(Player pl) {
        HumanInventory inventoryPl = pl.getInventory();
        HumanInventory inventoryEnt = this.getInventory();
        inventoryEnt.setHelmet(inventoryPl.getHelmet());
        inventoryEnt.setChestplate(inventoryPl.getChestplate());
        inventoryEnt.setLeggings(inventoryPl.getLeggings());
        inventoryEnt.setBoots(inventoryPl.getBoots());
        inventoryEnt.setItemInHand(inventoryPl.getItemInHand());
        this.getOffhandInventory().setItem(0, pl.getOffhandInventory().getItem(0));
        this.saveNBT();
    }

    public static CompoundTag nbt(Location loc, String name, String skinName, float scale) {
        CompoundTag nbt = new CompoundTag()
                .putList("Pos",new ListTag<>()
                        .add(new DoubleTag(loc.x))
                        .add(new DoubleTag(loc.y))
                        .add(new DoubleTag(loc.z)))
                .putList("Motion", new ListTag<DoubleTag>()
                        .add(new DoubleTag(0))
                        .add(new DoubleTag(0))
                        .add(new DoubleTag(0)))
                .putList("Rotation", new ListTag<FloatTag>()
                        .add(new FloatTag((float) loc.getYaw()))
                        .add(new FloatTag((float) loc.getPitch())))
                .putBoolean("Invulnerable", true)
                .putString("NameTag", name)
                .putFloat("Scale", scale)
                .putBoolean("npc", true);
        nbt.putCompound("Skin", createSkinTag(skinName));
        nbt.putBoolean("ishuman", true);
        return nbt;
    }

    private static CompoundTag createSkinTag(String skinName) {
        Skin skin = new Skin();
        File json = new File(xyz.syodo.NPC.get().getDataFolder() + "/SKINS", skinName + ".json");
        String geometry = "";
        try {
            geometry = Files.readString(json.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        skin.setGeometryData(geometry);
        skin.setGeometryName("geometry.unknown");
        BufferedImage image = null;
        File imageFile = new File(xyz.syodo.NPC.get().getDataFolder() + "/SKINS", skinName + ".png");
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        skin.setCapeId("nocape");
        skin.setSkinData(image);
        skin.setTrusted(true);
        CompoundTag skinTag = new CompoundTag()
                .putString("ModelId", skin.getSkinId())
                .putByteArray("Data", skin.getSkinData().data)
                .putInt("SkinImageWidth", skin.getSkinData().width)
                .putInt("SkinImageHeight", skin.getSkinData().height)
                .putString("CapeId", skin.getCapeId())
                .putByteArray("CapeData", skin.getCapeData().data)
                .putInt("CapeImageWidth", skin.getCapeData().width)
                .putInt("CapeImageHeight", skin.getCapeData().height)
                .putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("SkinAnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", skin.isPremium())
                .putBoolean("PersonaSkin", skin.isPersona())
                .putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic())
                .putString("ArmSize", skin.getArmSize())
                .putString("SkinColor", skin.getSkinColor())
                .putBoolean("IsTrustedSkin", true);
        List<SkinAnimation> animations = skin.getAnimations();
        if (!animations.isEmpty()) {
            ListTag<CompoundTag> animationsTag = new ListTag<>(); // "AnimatedImageData"
            for (SkinAnimation animation : animations) {
                animationsTag.add(new CompoundTag()
                        .putFloat("Frames", animation.frames)
                        .putInt("Type", animation.type)
                        .putInt("ImageWidth", animation.image.width)
                        .putInt("ImageHeight", animation.image.height)
                        .putInt("AnimationExpression", animation.expression)
                        .putByteArray("Image", animation.image.data));
            }
            skinTag.putList("AnimatedImageData",animationsTag);
        }
        List<PersonaPiece> personaPieces = skin.getPersonaPieces();
        if (!personaPieces.isEmpty()) {
            ListTag<CompoundTag> piecesTag = new ListTag<>(); // "PersonaPieces"
            for (PersonaPiece piece : personaPieces) {
                piecesTag.add(new CompoundTag().putString("PieceId", piece.id)
                        .putString("PieceType", piece.type)
                        .putString("PackId", piece.packId)
                        .putBoolean("IsDefault", piece.isDefault)
                        .putString("ProductId", piece.productId));
            }
        }
        List<PersonaPieceTint> tints = skin.getTintColors();
        if (!tints.isEmpty()) {
            for (PersonaPieceTint tint : tints) {
                ListTag<StringTag> colors = new ListTag<>(); // "Colors"
                colors.setAll(tint.colors.stream().map(s -> new StringTag(s)).collect(Collectors.toList()));
            }
        }
        if (!skin.getPlayFabId().isEmpty()) {
            skinTag.putString("PlayFabId", skin.getPlayFabId());
        }
        return skinTag;
    }
}

