package edu.gvsu.cis.campbjos.ftp;


public final class ControlByteReader {
    
    public static void readByteStream(final InputStream inputStream,
        final String filename) throws Exception {
      //create a file called 'filename' containing what it is in
      //input stream
      try {
        FileOutputStream output = 
                            new FileOutputStream(new File("./" + filename));
        int read = 0;
        byte[] bytes = new byte[1024];
        
        while ((read = inputStream.read(bytes)) != -1) {
			output.write(bytes, 0, read);
		}
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
            if (inputStream != null) {
    			try {
    				inputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
		    }
		    if (output != null) {
			    try {
				    // output.flush();
				    output.close();
			    } catch (IOException e) {
				    e.printStackTrace();
			    }
		    }   
        }
    }
    
}