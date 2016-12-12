package edu.gvsu.cis.campbjos.imgine.central

import java.io.IOException
import java.lang.System.out
import java.net.InetAddress
import java.net.ServerSocket

class CentralServer @Throws(IOException::class)
constructor(port: Int) {

    init {
        val server = ServerSocket(port)
        out.printf("New server started from %s %d\n", InetAddress.getLocalHost().hostAddress, server.localPort)

        while (true) {
            val client = server.accept()
            val handler = CentralInterpreter(client)
            val thread = Thread(handler)
            thread.start()
        }
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            if (args.size != 1) {
                throw RuntimeException("Syntax: java CentralServer <port>")
            }
            try {
                CentralServer(Integer.parseInt(args[0]))
            } catch (e: IOException) {
                println(e.message)
            }

        }
    }
}
