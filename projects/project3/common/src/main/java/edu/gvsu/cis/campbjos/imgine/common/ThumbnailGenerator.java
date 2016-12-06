package edu.gvsu.cis.campbjos.imgine.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailGenerator {

    public static BufferedImage generate(String filename) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            return null;
        }
        BufferedImage scaledImage = null;
        try {
            scaledImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            scaledImage.createGraphics()
                    .drawImage(bufferedImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);

        } catch (NullPointerException e) {
            // It's fine. Continue
            scaledImage = null;
        }
        return scaledImage;
    }
}
