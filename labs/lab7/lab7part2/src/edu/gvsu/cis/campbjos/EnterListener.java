package edu.gvsu.cis.campbjos;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EnterListener extends KeyAdapter {

    private static final String BYE = "bye";
    ChatClient client;
    ChatFrame gui;

    public EnterListener(ChatClient client, ChatFrame gui) {
        this.client = client;
        this.gui = gui;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String message = gui.input.getText();
            if (message.toLowerCase().equals(BYE)) {
                close();
                return;
            }
            client.sendTextToChat(message);
            gui.input.setText("");
        }
    }

    private void close() {
        client.disconnect();
        gui.setVisible(false);
    }
}
