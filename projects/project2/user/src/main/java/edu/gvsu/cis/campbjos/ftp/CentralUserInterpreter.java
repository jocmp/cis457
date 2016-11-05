package edu.gvsu.cis.campbjos.ftp;

import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.IOException;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.common.Converter.convertToServerPortNumber;
import static java.lang.String.format;

class CentralUserInterpreter {

    private Socket socket;

    CentralUserInterpreter() {
        socket = null;
    }

    String connect(final String username, final String speed,
                   final String ipAddress, final String serverPort) throws IOException {
        final int port = convertToServerPortNumber(serverPort);
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket " +
                    "%s:%s", ipAddress, serverPort));
        }
        String hostname = format("%s/%s",
                socket.getLocalAddress().getHostName(),
                socket.getInetAddress());
//        Host host = new Host(ipAddress, port, username);
//        ControlWriter.write(socket.getOutputStream(), );
        return hostname;
    }

    public Results query(String searchTerm) {
        return null;
    }

}
