package edu.gvsu.cis.campbjos.imgine.common;

import java.io.IOException;

public interface ProtocolInterpreter {

    void retrieve(final String filename) throws IOException;

    void quit() throws IOException;

}