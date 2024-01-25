package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class Auditor {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;
    private final static long ACTIVE_MUSICIAN_TIMESPAN_MS = 5000;

    private ArrayList<MusicianInfo> heardMusicians;

    public Auditor() {
        heardMusicians = new ArrayList<>();
    }

    public ArrayList<MusicianInfo> getActiveMusicians() {
        long currentTime = System.currentTimeMillis();

        ArrayList<MusicianInfo> activeMusicians = new ArrayList<>();
        for(var musician : heardMusicians)
            if(currentTime - musician.lastActivity <= ACTIVE_MUSICIAN_TIMESPAN_MS)
                activeMusicians.add(musician);

        return activeMusicians;
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
                    handleMessage(message);
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

    private Optional<MusicianInfo> getRegisteredMusician(UUID uuid) {
        return heardMusicians.stream().filter(x -> x.uuid.equals(uuid)).findFirst();
    }

    private String deduceInstrument(String sound) {
        switch(sound) {
            case "ti-ta-ti": return "piano";
            case "pouet": return "trumpet";
            case "trulu": return "flute";
            case "gzi-gzi": return "violin";
            case "boum-boum": return "drum";
        }
        throw new InvalidParameterException("sound not recognized");
    }

    private void handleMessage(String message) {
        var datagram = new Gson().fromJson(message, SoundDatagram.class);
        System.out.println("Heard sound : " + datagram.sound);

        long currentTime = System.currentTimeMillis();

        var maybeExisting = getRegisteredMusician(datagram.uuid);
        if(maybeExisting.isEmpty()) {
            String instrument = deduceInstrument(datagram.sound);
            heardMusicians.add(new MusicianInfo(datagram.uuid, instrument, currentTime));
        }
        else {
            MusicianInfo existing = maybeExisting.get();
            existing.lastActivity = currentTime;
        }
    }
}
