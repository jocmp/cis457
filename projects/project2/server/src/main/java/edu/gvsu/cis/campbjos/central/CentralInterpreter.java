package edu.gvsu.cis.campbjos.central;

import com.google.gson.Gson;
import edu.gvsu.cis.campbjos.ftp.common.model.Host;
import edu.gvsu.cis.campbjos.ftp.common.model.Result;
import edu.gvsu.cis.campbjos.ftp.common.model.Results;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static edu.gvsu.cis.campbjos.ftp.common.Commands.*;
import static edu.gvsu.cis.campbjos.ftp.common.ControlWriter.write;
import static edu.gvsu.cis.campbjos.ftp.common.StringReader.listenForCharacterStream;
import static java.util.stream.Collectors.toList;

public class CentralInterpreter implements Runnable {

    private static final Vector<Host> HOSTS;
    private static final Vector<Result> RESULTS;

    private final BufferedReader bufferedReader;
    private static final int COMMAND_INDEX = 0;
    private static final int SEARCH_TERM_INDEX = 1;

    private Socket socket;
    private Host host;

    static {
        HOSTS = new Vector<>();
        RESULTS = new Vector<>();
    }

    CentralInterpreter(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String unParsedHost = listenForCharacterStream(socket.getInputStream());
        host = new Gson().fromJson(unParsedHost, Host.class);
        HOSTS.add(host);
        write(socket.getOutputStream(), ACK);
        String unParsedResults = listenForCharacterStream(socket.getInputStream());
        Results hostResults = new Gson().fromJson(unParsedResults, Results.class);
        RESULTS.addAll(hostResults.list());
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            Gson gson = new Gson();
            while (true) {
                String requestLine = bufferedReader.readLine();
                System.out.println(requestLine);
                if (requestLine.equals(QUIT)) {
                    break;
                }
                List<Result> results = queryIfValid(requestLine);

                write(socket.getOutputStream(), gson.toJson(results));
            }
        } catch (IOException ex) {
            System.out.println("-- Connection to user lost.");
        } finally {
            System.out.println("Connection ended");
            RESULTS.removeIf(result -> result.hostname.equals(host.hostname));
            HOSTS.remove(host);
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Socket to user already closed");
            }
        }
    }

    private List<Result> queryIfValid(final String input) {
        List<String> tokens = Arrays.asList(input.split(" "));
        if (tokens.size() < 2) {
            return new ArrayList<>();
        }
        if (!tokens.get(COMMAND_INDEX).equals(SEARCH)) {
            return new ArrayList<>();
        }
        return queryFileList(tokens.get(SEARCH_TERM_INDEX));
    }

    private List<Result> queryFileList(final String searchTerm) {
        return RESULTS.stream()
                .filter(result -> result.filename.contains(searchTerm))
                .collect(toList());
    }
}
