package edu.gvsu.cis.campbjos.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ControlByteReader {

    public static void readByteStream(final InputStream inputStream,
                                      final String filename) throws
            Exception {
        FileOutputStream output = null;
        try {
            byte[] bytes = new byte[1024];
            int read = 0;
            output = new FileOutputStream(new File(filename));
            boolean hasBytes = false;
            while ((read = inputStream.read(bytes)) != -1) {
                output.write(bytes, 0, read);
                hasBytes = true;
            }
            if (!hasBytes) {
                new File(filename).delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}