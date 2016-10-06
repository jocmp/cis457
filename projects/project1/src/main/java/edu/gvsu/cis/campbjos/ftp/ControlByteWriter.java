package edu.gvsu.cis.campbjos.ftp;


public final class ControlByteWriter {
    
    public static void sendFile(final OutputStream outputStream,
        final String filename) throws Exception {
        // Open the requested file.
        FileInputStream fileInputStream = null;
        boolean fileExists = true;
        try {
            fileInputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }
        // Send the entity body.
        if (fileExists) {
            sendBytes(fileInputStream, outputStream);
            fis.close();
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