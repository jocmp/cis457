package edu.gvsu.cis.campbjos.imgine.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ThumbnailGenerator {

    public static BufferedImage generate(String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        BufferedImage scaledImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        scaledImage.createGraphics()
                .drawImage(bufferedImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);

        return scaledImage;
    }
}
