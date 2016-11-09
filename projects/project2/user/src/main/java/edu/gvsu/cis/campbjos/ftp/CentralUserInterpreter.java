package edu.gvsu.cis.campbjos.ftp;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.QUIT;
import static edu.gvsu.cis.campbjos.ftp.common.Commands.SEARCH;
import static edu.gvsu.cis.campbjos.ftp.common.Constants.CENTRAL_SERVER_MANIFEST;
import static edu.gvsu.cis.campbjos.ftp.common.ControlWriter.write;
import static edu.gvsu.cis.campbjos.ftp.common.Converter.convertToServerPortNumber;
import static java.lang.String.format;

class CentralUserInterpreter {

    private Socket socket;
    private BufferedReader bufferedReader;

    CentralUserInterpreter() {
        socket = null;
    }

    void connect(final String username, final String speed,
                 final String ipAddress, final String serverPort) throws IOException, NumberFormatException {
        final int port = convertToServerPortNumber(serverPort);
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket " +
                    "%s:%s", ipAddress, serverPort));
        }
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        Host host = new Host.Builder()
                .setIp(localAddress)
                .setPort(port)
                .setHostname(InetAddress.getLocalHost().toString())
                .setSpeed(speed)
                .setUsername(username).createHost();
        write(socket.getOutputStream(), new Gson().toJson(host));
        replyWhenAcknowledged(host);
    }

    private void replyWhenAcknowledged(Host host) throws IOException {
        bufferedReader.readLine();
        write(socket.getOutputStream(), readResultsFromFile());
    }

    private String readResultsFromFile() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(CENTRAL_SERVER_MANIFEST));
        Gson gson = new Gson();
        return gson.toJson(gson.fromJson(reader, Results.class));
    }

    Results query(String searchTerm) throws IOException {
        write(socket.getOutputStream(), format("%s %s", SEARCH, searchTerm));
        Gson gson = new Gson();
        String unParsedResults = bufferedReader.readLine();
        return gson.fromJson(unParsedResults, Results.class);
    }

    void quit() throws IOException {
        write(socket.getOutputStream(), QUIT);
        socket = null;
    }

    boolean isConnected() {
        return !(socket == null || socket.isClosed());
    }
}
