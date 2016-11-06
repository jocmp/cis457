package edu.gvsu.cis.campbjos.central;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Result;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.ControlWriter.write;

public class CentralInterpreter implements Runnable {

    //    private static final Vector<Host> HOSTS;
    private static final Vector<Result> RESULTS;

    private final BufferedReader bufferedReader;
    private static final int COMMAND_INDEX = 0;
    private static final int SEARCH_TERM_INDEX = 1;

    private Socket socket;
    private Host host;

    static {
//        HOSTS = new Vector<>();
        RESULTS = new Vector<>();
    }

    CentralInterpreter(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String unParsedHost = bufferedReader.readLine();
        host = new Gson().fromJson(unParsedHost, Host.class);
//        HOSTS.add(host);
        write(socket.getOutputStream(), ACK);
        System.out.println(host.toString());
        String unParsedResults = bufferedReader.readLine();
        Results hostResults = new Gson().fromJson(unParsedResults, Results.class);
        RESULTS.addAll(hostResults.list());
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String requestLine = bufferedReader.readLine();
                System.out.println(requestLine);
                if (requestLine.equals(QUIT)) {
                    break;
                }
                String serializedResults = new Gson().toJson(queryIfValid(requestLine));

                write(socket.getOutputStream(), serializedResults);
            }
        } catch (IOException ex) {
            System.out.println("Connection to user lost.");
        } finally {
            System.out.println("Connection ended");
            RESULTS.removeIf(result -> result.host.equals(host));
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Socket to user already closed");
            }
        }
    }

    private Results queryIfValid(final String input) {
        List<String> tokens = Arrays.asList(input.split(" ", 2));
        if (tokens.size() < 2) {
            return new Results();
        }
        if (!tokens.get(COMMAND_INDEX).equals(SEARCH)) {
            return new Results();
        }
        return queryFileList(tokens.get(SEARCH_TERM_INDEX));
    }

    private Results queryFileList(final String searchTerm) {
        Results queryResults = new Results();
        RESULTS.stream()
                .filter(result -> !result.host.equals(host))
                .filter(result -> result.filename.contains(searchTerm))
                .forEach(queryResults::addResult);
        return queryResults;
    }
}
