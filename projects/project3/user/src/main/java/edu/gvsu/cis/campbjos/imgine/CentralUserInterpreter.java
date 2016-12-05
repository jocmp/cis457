package edu.gvsu.cis.campbjos.imgine;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.imgine.common.FileReader;
import edu.gvsu.cis.campbjos.imgine.common.model.Host;
import edu.gvsu.cis.campbjos.imgine.common.model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import static edu.gvsu.cis.campbjos.imgine.common.BufferedImageConverter.getThumbnailStringFromImage;
import static edu.gvsu.cis.campbjos.imgine.common.Commands.QUIT;
import static edu.gvsu.cis.campbjos.imgine.common.Commands.SEARCH;
import static edu.gvsu.cis.campbjos.imgine.common.Constants.CENTRAL_SERVER_MANIFEST;
import static edu.gvsu.cis.campbjos.imgine.common.ControlWriter.write;
import static edu.gvsu.cis.campbjos.imgine.common.ServerPortConverter.convertToServerPortNumber;
import static edu.gvsu.cis.campbjos.imgine.common.ThumbnailGenerator.generate;
import static java.lang.String.format;

class CentralUserInterpreter {

    private Socket socket;
    private BufferedReader bufferedReader;
    private Host host;

    CentralUserInterpreter() {
        socket = null;
    }

    void connect(final String username, final String ipAddress, final String centralServerPort,
                 final int ftpServerPort) throws IOException, NumberFormatException {
        final int port = convertToServerPortNumber(centralServerPort);
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException exception) {
            throw new IOException(format("Error opening socket to server %s:%s", ipAddress, centralServerPort));
        }
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String localAddress = InetAddress.getLocalHost().getHostAddress();
        host = new Host.Builder()
                .setIp(localAddress)
                .setPort(ftpServerPort)
                .setUsername(username).createHost();
        uploadHostManifestToServer();
    }

    private void uploadHostManifestToServer() {
        new Thread(() -> {
            try {
                write(socket.getOutputStream(), new Gson().toJson(host));
                replyWhenAcknowledged();
            } catch (IOException e) {
                // couldn't start server connection
            }
        }).start();
    }

    private void replyWhenAcknowledged() throws IOException {
        bufferedReader.readLine();
        write(socket.getOutputStream(), readResultsFromFile());
    }

    private String readResultsFromFile() throws IOException {
        Gson gson = new Gson();
        Results results = gson.fromJson(FileReader.getString(CENTRAL_SERVER_MANIFEST), Results.class);
        results.list()
                .parallelStream()
                .forEach(result -> {
                    result.host = host;
                    result.thumbnail = getThumbnailStringFromImage(generate(result.filename));
                });
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
