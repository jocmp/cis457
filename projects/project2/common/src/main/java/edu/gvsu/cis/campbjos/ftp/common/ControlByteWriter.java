package edu.gvsu.cis.campbjos.ftp.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public final class ControlByteWriter {


    public static void sendFile(final OutputStream outputStream,
                                final String filename) throws
            Exception {
        FileInputStream fileInputStream = null;
        boolean fileExists = true;
        try {
            fileInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }
        if (fileExists) {
            sendBytes(fileInputStream, outputStream);
            fileInputStream.close();
        }
    }

    private static void sendBytes(FileInputStream fis,
                                  OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // Copy requested file into the socket's output stream.
        int counter = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
            counter++;
        }
    }
}