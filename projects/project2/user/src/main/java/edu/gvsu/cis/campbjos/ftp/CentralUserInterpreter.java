package edu.gvsu.cis.campbjos.ftp;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.QUIT;
import static edu.gvsu.cis.campbjos.ftp.common.Commands.SEARCH;
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
        File currentDirectory = new File(".");
        Results results = new Results();
        Arrays.stream(currentDirectory.listFiles())
                .forEach(file -> {
                    results.addResult(host, file.getName());
                });
        write(socket.getOutputStream(), new Gson().toJson(results));
    }

    Results query(String searchTerm) throws IOException {
        write(socket.getOutputStream(), format("%s %s", SEARCH, searchTerm));
        Gson gson = new Gson();
        String unParsedResults = bufferedReader.readLine();
        Results results = gson.fromJson(unParsedResults, Results.class);
        return results;
    }

    void quit() throws IOException {
        write(socket.getOutputStream(), QUIT);
        socket = null;
    }

    boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}
