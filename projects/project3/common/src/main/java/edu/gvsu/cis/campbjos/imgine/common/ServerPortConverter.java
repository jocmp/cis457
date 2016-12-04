package edu.gvsu.cis.campbjos.imgine.common;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;

public class ServerPortConverter {

    public static int convertToServerPortNumber(final String
                                                        serverPortText) {
        try {
            return valueOf(serverPortText);
        } catch (NumberFormatException exception) {
            if (serverPortText.isEmpty()) {
                throw new NumberFormatException("Empty port number");
            } else {
                throw new NumberFormatException(format("Invalid port=%s",
                        serverPortText));
            }
        }
    }
}
