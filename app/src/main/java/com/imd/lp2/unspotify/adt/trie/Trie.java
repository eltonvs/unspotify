package com.imd.lp2.unspotify.adt.trie;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Elton Viana
 */
public class Trie implements ITrie {

    private Node root;

    public Trie() {
        this.root = new Node();
    }

    /**
     * Verifica se a árvore está vazia
     * @return boolean
     */
    public boolean isEmpty() {
        return root.isLeaf();
    }

    /**
     * Wrapper público para busca na árvore
     * @param value valor a ser buscado
     * @return Nó com o valor ou null
     */
    public Node search(String value) {
        Node tmp = search(value, root);
        return tmp == null || tmp.isEnd() ? tmp : null;
    }

    /**
     * Método privado de busca na árvore
     * @param value valor a ser buscado
     * @param node nó para buscar o valor
     * @return Nó com o valor ou null
     */
    private Node search(String value, Node node) {
        if (root.isLeaf() || node == null || value == null || value.length() == 0) {
            return node;
        }

        if (node != root && node.getValue().equals(value.charAt(0))) {
            if (value.length() == 1) {
                return node;
            }
            value = value.substring(1);
        }

        return node.isLeaf() ? null : node.getChildren().containsKey(value.charAt(0))
                ? search(value, node.getChildren().get(value.charAt(0)))
                : null;
    }

    /**
     * Wrapper público para inserção na árvore
     * @param value valor a ser inserido na árvore
     * @return boolean se foi inserido com sucesso ou não
     */
    public boolean insert(String value) {
        return insert(value, root);
    }

    /**
     * Método privado de inserção na árvore
     * @param value valor a ser inserido na árvore
     * @param node nó a ser inserido o valor
     * @return boolean se foi inserido com sucesso ou não
     */
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

    /**
     * Wrapper público para retornar um ArrayList com todas
     * as strings que começam com um determinado prefixo
     * @param prefix Prefixo das strings a serem retornadas
     * @return ArrayList de strings que começam com um determinado prefixo
     */
    public ArrayList<String> getChildren(String prefix) {
        Node oc = search(prefix, root);

        if (oc == null) {
            return new ArrayList<>();
        }

        if (oc != root) {
            ArrayList<String> r = new ArrayList<>();
            for (HashMap.Entry<Character, Node> entry : oc.getChildren().entrySet()) {
                ArrayList<String> tmp;
                tmp = getChildren(prefix, entry.getValue());
                r.addAll(tmp);
            }
            return r;
        }

        return getChildren(prefix, oc);
    }

    /**
     * Método privado para retornar um ArrayList com todas
     * as strings que começam com um determinado prefixo
     * @param prefix Prefixo das strings a serem retornadas
     * @param node Nó a ser feita a busca pelas strings
     * @return ArrayList de strings que começam com um determinado prefixo
     */
    private ArrayList<String> getChildren(String prefix, Node node) {
        ArrayList<String> r = new ArrayList<>();

        if (node.isEnd() && node.getValue() != null) {
            r.add(prefix + node.getValue());
        }

        if (!node.isLeaf()) {
            for (HashMap.Entry<Character, Node> entry : node.getChildren().entrySet()) {
                ArrayList<String> tmp;
                tmp = getChildren(node.getValue() != null ? prefix + node.getValue() : prefix, entry.getValue());
                r.addAll(tmp);
            }
        }

        return r;
    }

}
