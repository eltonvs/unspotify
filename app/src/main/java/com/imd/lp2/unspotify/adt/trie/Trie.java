package com.imd.lp2.unspotify.adt.trie;

import java.util.HashMap;

/**
 * @author Elton Viana
 */
public class Trie {

    private Node root;

    public Trie() {
        this.root = new Node();
    }

    public boolean isEmpty() {
        return root.isLeaf();
    }

    public Node search(String value) {
        return search(value, root);
    }

    private Node search(String value, Node node) {
        if (root.isLeaf() || node == null || value == null || value.length() == 0) {
            return null;
        }

        if (node != root && node.getValue().equals(value.charAt(0))) {
            if (value.length() == 1) {
                return node.isEnd() ? node : null;
            }
            value = value.substring(1);
        }

        return node.isLeaf() ? null : node.getChildren().containsKey(value.charAt(0))
                ? search(value, node.getChildren().get(value.charAt(0)))
                : null;
    }

    public boolean insert(String value) {
        return insert(value, root);
    }

    private boolean insert(String value, Node node) {
        // Quando não pode inserir: value = null, value = ""
        if (value == null || value.length() == 0) {
            return false;
        }

        // Quando o no pai nao tem filhos
        if (node.isLeaf()) {
            node.setChildren(new HashMap<Character, Node>());
            // é o final da string inserida
            if (value.length() == 1) {
                node.getChildren().put(value.charAt(0), new Node(value.charAt(0), true));
                return true;
            }
            node.getChildren().put(value.charAt(0), new Node(value.charAt(0), false));
        } else if (node.getChildren().containsKey(value.charAt(0))) {
            // Se existe um no c o caracter atual
            if (value.length() == 1) {
                node.getChildren().get(value.charAt(0)).setEnd(true);
                return true;
            }
        } else {
            if (value.length() == 1) {
                // cria a chave como true
                node.getChildren().put(value.charAt(0), new Node(value.charAt(0), true));
                return true;
            }
            // cria a chave e continua inserindo...
            node.getChildren().put(value.charAt(0), new Node(value.charAt(0), false));
        }

        return insert(value.substring(1), node.getChildren().get(value.charAt(0)));
    }

}
