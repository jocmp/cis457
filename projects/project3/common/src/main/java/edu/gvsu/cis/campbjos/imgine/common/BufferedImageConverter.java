package edu.gvsu.cis.campbjos.imgine.common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BufferedImageConverter {

    public static String getThumbnailStringFromImage(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            return null;
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static BufferedImage getImageFromJson(String json) {
        byte[] decodeBytes = Base64.getDecoder().decode(json);
        try {
            return ImageIO.read(new ByteArrayInputStream(decodeBytes));
        } catch (IOException e) {
            return null;
        }
    }
}
