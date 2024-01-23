package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;

public class Auditor {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    public Auditor() {

    }

    public void startListening() {
        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            InetSocketAddress group_address =  new InetSocketAddress(IPADDRESS, PORT);
            NetworkInterface netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(group_address, netif);

            byte[] buffer = new byte[1024];

            while(true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                    var datagram = new Gson().fromJson(message, SoundDatagram.class);

                    System.out.println("Received message: " + datagram.sound);
                }
                catch(Exception e) {
                    break;
                }
            }

            socket.leaveGroup(group_address, netif);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
