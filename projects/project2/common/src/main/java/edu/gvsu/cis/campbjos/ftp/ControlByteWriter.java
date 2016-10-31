package edu.gvsu.cis.campbjos.ftp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import static edu.gvsu.cis.campbjos.ftp.Constants.ENTRY_DOES_NOT_EXIST;
import static edu.gvsu.cis.campbjos.ftp.Constants.ENTRY_EXISTS;

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
            outputStream.write(ENTRY_EXISTS);
            sendBytes(fileInputStream, outputStream);
            fileInputStream.close();
        } else {
            outputStream.write(ENTRY_DOES_NOT_EXIST);
        }
    }

    private static void sendBytes(FileInputStream fis,
                                  OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }
}