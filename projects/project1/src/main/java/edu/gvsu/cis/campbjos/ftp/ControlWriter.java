package edu.gvsu.cis.campbjos.ftp;


final class ControlWriter {
    
    public static void write(final OutputStream outputStream, 
        final String message) {
        
        Writer writer 
            = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));

        writer.append(message).append(Constants.CRLF);
        writer.flush(); 
    }
    
    
}