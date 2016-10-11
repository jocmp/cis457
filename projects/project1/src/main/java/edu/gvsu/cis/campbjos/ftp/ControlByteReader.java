package edu.gvsu.cis.campbjos.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static edu.gvsu.cis.campbjos.ftp.Constants.ENTRY_EXISTS;

public final class ControlByteReader {

    public static void readByteStream(final InputStream inputStream,
                                      final String filename) throws Exception {
        FileOutputStream output = null;
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            boolean isValidFile = false;
            boolean isStatusRead = true;
            while ((read = inputStream.read(bytes)) != -1) {
                isValidFile = bytes[0] == ENTRY_EXISTS;
                if (!isValidFile && isStatusRead) {
                    throw new NullPointerException("Invalid filename");
                } else if (isValidFile) {
                    isStatusRead = false;
                    output = new FileOutputStream(new File(filename));
                    continue;
                }
                output.write(bytes, 0, read);
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