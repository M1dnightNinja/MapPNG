package org.wallentines.mappng;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public interface PNGMapItemData {

    void setCustomData(String buffer);

    String getCustomData();

    default void loadFromPNG(File f) {
        if (f.isFile()) {
            String mapData;

            try {

                BufferedImage bufferedImage = ImageIO.read(f);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);

                mapData = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            } catch (IOException ex) {

                MapPNG.LOGGER.warn("An error occurred while loading a PNG!");
                ex.printStackTrace();
                return;
            }

            setCustomData(mapData);
        }
    }

}
