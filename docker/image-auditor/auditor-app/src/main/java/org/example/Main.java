package org.example;

import java.net.ServerSocket;
import java.util.concurrent.*;
import java.net.*;

public class Main {
    final static String IPADDRESS = "239.1.2.3";
    final static int PORT = 44444;

    public static void main(String[] args) {
        Auditor auditor = new Auditor();
        auditor.startListening();
    }
}