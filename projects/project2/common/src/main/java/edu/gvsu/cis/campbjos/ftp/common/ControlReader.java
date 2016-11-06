package edu.gvsu.cis.campbjos.ftp.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ControlReader {

    public static String listenForCharacterStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                (inputStream));
        String message = "";
        String response = "";
        while ((response = bufferedReader.readLine()) != null) {
            message += response;
        }
        return message;
    }
}
