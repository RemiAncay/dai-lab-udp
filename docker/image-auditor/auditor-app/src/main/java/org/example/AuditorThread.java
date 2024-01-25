package org.example;

import java.util.ArrayList;

public class AuditorThread extends Thread {
    private final Auditor auditor;

    public AuditorThread(Auditor auditor) {
        this.auditor = auditor;
    }

    @Override
    public void run() {
        auditor.startListening();
    }
}
