package edu.gvsu.cis.campbjos.imgine;

import javafx.scene.Node;

class Selection {

    private Node node;

    public Selection() {
        node = null;
    }

    public void set(Node currentNode) {
        if (node != null) {
            node.setStyle("-fx-effect: null");
        }
        node = currentNode;
        node.setStyle("-fx-effect: dropshadow(three-pass-box, rgb(77,102,204), 5, 1, 0, 0);");
    }
}
