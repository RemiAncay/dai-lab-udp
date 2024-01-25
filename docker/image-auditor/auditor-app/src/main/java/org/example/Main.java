package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.net.*;

public class Main {
    private static final int TCP_PORT = 2205;

    public static void main(String[] args) {

        Auditor auditor = new Auditor();

        AuditorThread auditorThread = new AuditorThread(auditor);
        auditorThread.start();

        try (var serverSocket = new ServerSocket(TCP_PORT)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();

                    try(socket; // Not strictly necessary, but this is allowed.
                        var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

                        var activeMusicians = auditor.getActiveMusicians();

                        String jsonResponse = new Gson().toJson(activeMusicians);

                        out.write(jsonResponse);
                        out.flush();

                    } catch (IOException e) {
                        System.err.println("Error while handling socket: " + e.getMessage());
                    }

                } catch (IOException e) {
                    System.err.println("Error using client socket: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error opening server socket: " + e.getMessage());
        }
    }
}