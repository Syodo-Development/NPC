package xyz.syodo.utils;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.utils.SerializedImage;
import xyz.syodo.NPC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class SkinSaver {

    public static void saveSkin(String name, Skin skin) {
        SerializedImage image = skin.getSkinData();
        BufferedImage img = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);

        int index = 0;
        for (int y = 0; y < image.height; ++y) {
            for (int x = 0; x < image.width; ++x) {
                byte r = image.data[index];
                byte g = image.data[index + 1];
                byte b = image.data[index + 2];
                byte a = (byte) (255 - (image.data[index + 3] >> 1));
                index += 4;

                int color = (((a&0xFF)|0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                        ((g & 0xFF) << 8) |
                        (b & 0xFF);
                if ((color & 0x00FFFFFF) != 0) {
                    img.setRGB(x, y, color);
                }
            }
        }

        try {
            File texture = new File(NPC.get().getDataFolder() + "/SKINS", name + ".png");
            ImageIO.write(img, "png", texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String skinId = skin.getSkinResourcePatch().replaceAll("\n", "").replaceAll("\s+", "").replace("{\"geometry\":{\"default\":\"", "").replace("\"}}", "");
        String geometry = skin.getGeometryData().replaceFirst(skinId, "geometry.unknown");
        File geometryFile = new File(NPC.get().getDataFolder() + "/SKINS", name + ".json");
        try {
            geometryFile.createNewFile();
            FileWriter writer = new FileWriter(geometryFile);
            writer.write(geometry);
            writer.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
