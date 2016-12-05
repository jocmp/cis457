package edu.gvsu.cis.campbjos.imgine.common;

import java.io.*;

public class FileReader {

    public static String getString(String filename) throws IOException {

        FileInputStream stream = new FileInputStream(new File(filename));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

}
