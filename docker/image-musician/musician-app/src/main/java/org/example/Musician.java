package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Musician {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;

    private UUID uuid;
    private String instrument;
    private final DatagramPacket soundDatagram;

    public Musician(String instrument) {
        this.instrument = instrument;
        this.uuid = UUID.randomUUID();

        String message = generateSoundDatagram();
        byte[] payload = message.getBytes(UTF_8);
        InetSocketAddress dest_address = new InetSocketAddress(IPADDRESS, 44444);
        this.soundDatagram = new DatagramPacket(payload, payload.length, dest_address);;
    }

    public void play() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(soundDatagram);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println(generateSound());
    }

    private String generateSound() {
        switch (instrument) {
            case "piano":return "ti-ta-ti";
            case "trumpet":return "pouet";
            case "flute":return "trulu";
            case "violin":return "gzi-gzi";
            case "drum":return "boom-boom";
        }
        throw new RuntimeException("Instrument not recognized");
    }

    private String generateSoundDatagram() {
        return "{\"uuid\":\""+uuid.toString()+"\", \"sound\":\""+generateSound()+"\"}";
    }
}
