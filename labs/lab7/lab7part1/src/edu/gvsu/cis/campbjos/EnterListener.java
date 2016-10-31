package edu.gvsu.cis.campbjos;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EnterListener extends KeyAdapter {

    ChatClient client;
    ChatFrame gui;

    public EnterListener(ChatClient client, ChatFrame gui) {
        this.client = client;
        this.gui = gui;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            client.sendTextToChat(gui.input.getText());
            gui.input.setText("");
        }
    }
}
