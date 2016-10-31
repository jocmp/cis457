package edu.gvsu.cis.campbjos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ExitListener extends WindowAdapter {

    private ChatClient client;

    ExitListener(ChatClient client) {
        this.client = client;
    }

    public void windowClosing(WindowEvent e) {
        client.disconnect();
        System.exit(0);
    }
}
