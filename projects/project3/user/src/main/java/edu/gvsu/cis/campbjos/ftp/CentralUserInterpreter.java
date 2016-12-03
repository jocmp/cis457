package edu.gvsu.cis.campbjos.ftp;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.ftp.common.FileReader;
import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private Host host;

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
        host = new Host.Builder()
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

    private String readResultsFromFile() throws IOException {
        Gson gson = new Gson();
        Results results = gson.fromJson(FileReader.getString(CENTRAL_SERVER_MANIFEST), Results.class);
        results.list().forEach(result -> result.host = host);
        return gson.toJson(results);
    }

    Results query(String searchTerm) throws IOException {
        write(socket.getOutputStream(), format("%s %s", SEARCH, searchTerm));
        Gson gson = new Gson();
        String unParsedResults = bufferedReader.readLine();
        return gson.fromJson(unParsedResults, Results.class);
    }

    void quit() throws IOException {
        write(socket.getOutputStream(), QUIT);
        host = null;
        socket = null;
    }

    boolean isConnected() {
        return !(socket == null || socket.isClosed());
    }
}
