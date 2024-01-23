package org.example;

import java.util.UUID;

public class SoundDatagram {
    public SoundDatagram(UUID uuid, String sound) {
        this.uuid = uuid;
        this.sound = sound;
    }

    public UUID uuid;
    public String sound;
}
