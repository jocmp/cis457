package edu.gvsu.cis.campbjos;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitListener extends WindowAdapter {

    ChatClient client;

    public ExitListener(ChatClient client) {
        this.client = client;
    }

    public void windowClosing(WindowEvent e) {
        client.disconnect();
        System.exit(0);
    }
}
