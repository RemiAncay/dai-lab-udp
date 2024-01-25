package org.example;

import java.util.UUID;

public class MusicianInfo {
    public MusicianInfo(UUID uuid, String instrument, long lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = lastActivity;
    }

    public UUID uuid;
    public String instrument;
    public long lastActivity;
}
