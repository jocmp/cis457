package edu.gvsu.cis.campbjos.ftp;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public final class ControlWriter {
    
    public static void write(final OutputStream outputStream,
        final String message) {

        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
            writer.append(message).append(Constants.CRLF);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}