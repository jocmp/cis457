package edu.gvsu.cis.campbjos;

import java.awt.*;

public class ChatFrame extends Frame {

    protected TextArea output;
    protected TextField input;

    protected Thread listener;

    public ChatFrame(String title) {
        super(title);

        setLayout(new BorderLayout());
        add("Center", output = new TextArea());
        output.setEditable(false);
        add("South", input = new TextField());

        pack();
        setVisible(true);
        input.requestFocus();
    }

    public static void main(String args[]) {
        new ChatFrame("Chat ");
    }
}

