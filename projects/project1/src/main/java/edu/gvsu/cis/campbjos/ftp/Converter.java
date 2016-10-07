package edu.gvsu.cis.campbjos.ftp;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;

public class Converter {

    public static int convertToServerPortNumber(final String serverPortText) {
        try {
            return valueOf(serverPortText);
        } catch (NumberFormatException exception) {
            throw new NumberFormatException(format("Invalid port=%s", serverPortText));
        }
    }
}
