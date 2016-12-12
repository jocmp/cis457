package edu.gvsu.cis.campbjos.imgine.central

import com.google.gson.Gson
import edu.gvsu.cis.campbjos.imgine.common.Commands.*
import edu.gvsu.cis.campbjos.imgine.common.ControlWriter.write
import edu.gvsu.cis.campbjos.imgine.common.model.Host
import edu.gvsu.cis.campbjos.imgine.common.model.Result
import edu.gvsu.cis.campbjos.imgine.common.model.Results
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Socket
import java.util.*

internal class CentralInterpreter @Throws(IOException::class)
constructor(private val socket: Socket) : Runnable {

    private val bufferedReader: BufferedReader
    private val host: Host

    init {
        bufferedReader = BufferedReader(InputStreamReader(socket.inputStream))
        val unParsedHost = bufferedReader.readLine()
        host = Gson().fromJson(unParsedHost, Host::class.java)

        write(socket.outputStream, ACK)
        System.out.printf("New client %s\n", host)

        val unParsedResults = bufferedReader.readLine()
        val hostResults = Gson().fromJson(unParsedResults,
                Results::class.java)
        RESULTS.addAll(hostResults.list())
    }

    override fun run() {
        try {

            while (true) {
                val requestLine = bufferedReader.readLine()
                if (requestLine == null || requestLine == QUIT) {
                    System.out.printf("Disconnecting %s\n", host)
                    break
                }
                println(requestLine)
                val serializedResults = Gson().toJson(queryIfValid(requestLine))

                write(socket.outputStream, serializedResults)
            }
        } catch (ex: IOException) {
            println("Connection to user lost.")
        } finally {
            println("Connection ended")
            RESULTS.removeIf { result -> result.host == host }
            try {
                socket.close()
            } catch (ex: IOException) {
                println("Socket to user already closed")
            }

        }
    }

    private fun queryIfValid(input: String): Results {
        val tokens = Arrays.asList(*input.split(" ".toRegex(), 2).toTypedArray())
        if (tokens.size < 2) {
            return Results()
        }
        if (tokens[COMMAND_INDEX] != SEARCH) {
            return Results()
        }
        return queryFileList(tokens[SEARCH_TERM_INDEX]
                .toLowerCase())
    }

    private fun queryFileList(searchTerm: String): Results {
        val queryResults = Results()
        RESULTS.filter({ result -> result.host != host })
                .filter({ result -> result.containsKeyword(searchTerm) })
                .forEach({ queryResults.addResult(it) })
        return queryResults
    }

    companion object {

        private val RESULTS: Vector<Result>
        private val COMMAND_INDEX = 0
        private val SEARCH_TERM_INDEX = 1

        init {
            RESULTS = Vector<Result>()
        }
    }
}
