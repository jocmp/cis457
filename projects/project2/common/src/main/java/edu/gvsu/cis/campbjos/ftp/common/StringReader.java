package edu.gvsu.cis.campbjos.ftp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringReader {

    public static String listenForCharacterStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                (inputStream));
        boolean isReceivingStream = true;
        String message = "";

        while (isReceivingStream) {
            try {
                final String requestLine = bufferedReader.readLine();
                isReceivingStream = !requestLine.isEmpty();
                if (isReceivingStream) {
                    message += requestLine;
                }
            } catch (IOException e) {
                break;
            }
        }
        return message;
    }
}
