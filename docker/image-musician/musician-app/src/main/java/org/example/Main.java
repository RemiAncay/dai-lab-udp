package org.example;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import static java.nio.charset.StandardCharsets.*;

class MulticastSender {


    public static void main(String[] args) {
        if(args.length != 1)
            throw new IllegalArgumentException("not enough or too many parameters");

        String instrument = args[0];

        final int timeBetweenPlays = 1000;
        long lastMeasure = System.currentTimeMillis();
        long timeTillNextPlay = 0;

        Musician musician = new Musician(instrument);

        while(true) {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - lastMeasure;
            lastMeasure = currentTime;
            timeTillNextPlay -= delta;

            if(timeTillNextPlay <= 0) {
                timeTillNextPlay += timeBetweenPlays;
                musician.play();
            }
        }
    }
}