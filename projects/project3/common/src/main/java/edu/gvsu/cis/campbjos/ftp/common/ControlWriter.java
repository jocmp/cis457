package edu.gvsu.cis.campbjos.ftp.common;

import java.io.*;

public final class ControlWriter {

    public static void write(final OutputStream outputStream,
                             final String message) throws IOException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter
                    (outputStream));
            writer.append(message);
            writer.newLine();
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}