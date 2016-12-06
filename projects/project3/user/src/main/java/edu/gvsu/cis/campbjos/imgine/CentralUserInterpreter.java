package edu.gvsu.cis.campbjos.imgine;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.imgine.common.FileReader;
import edu.gvsu.cis.campbjos.imgine.common.model.Descriptions;
import edu.gvsu.cis.campbjos.imgine.common.model.Host;
import edu.gvsu.cis.campbjos.imgine.common.model.Result;
import edu.gvsu.cis.campbjos.imgine.common.model.Results;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static edu.gvsu.cis.campbjos.imgine.common.BufferedImageConverter.getThumbnailStringFromImage;
import static edu.gvsu.cis.campbjos.imgine.common.Commands.QUIT;
import static edu.gvsu.cis.campbjos.imgine.common.Commands.SEARCH;
import static edu.gvsu.cis.campbjos.imgine.common.Constants.DESCRIPTIONS_JSON;
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
        write(socket.getOutputStream(), createResultsFromFiles());
    }

    private String createResultsFromFiles() throws IOException {
        Gson gson = new Gson();
        File folder = new File(".");
        List<File> files = Arrays.asList(getFiles(folder));
        Results results = new Results();
        Descriptions desc = gson.fromJson(FileReader.getString(DESCRIPTIONS_JSON), Descriptions.class);
        files.parallelStream()
                .filter(this::isImageFile)
                .map(file -> createResultFromFile(file, desc))
                .forEach(results::addResult);

        return gson.toJson(results);
    }

    private Result createResultFromFile(File file, Descriptions desc) {
        String filename = file.getName();
        String thumbnail = getThumbnailStringFromImage(generate(filename));
        String description = desc.getFileDescription(filename);
        return new Result(host, filename, thumbnail, description);
    }

    private boolean isImageFile(File file) {
        try {
            return Files.probeContentType(file.toPath()).startsWith("image");
        } catch (IOException e) {
            return false;
        }
    }

    private File[] getFiles(File folder) {
        File[] files = folder.listFiles();
        if (files == null) {
            return new File[]{};
        }
        return files;
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
