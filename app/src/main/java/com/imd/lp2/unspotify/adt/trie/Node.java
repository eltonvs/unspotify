package com.imd.lp2.unspotify.adt.trie;

import java.util.HashMap;

/**
 * @author Elton Viana
 */
public class Node {

    private Character value;
    private HashMap<Character, Node> children;
    private boolean end;

    public Node() {
        this.value = null;
        this.children = null;
        this.end = false;
    }

    public Node(Character value) {
        this();
        this.value = value;
    }

    public Node(Character value, boolean end) {
        this();
        this.value = value;
        this.end = end;
    }

    public Character getValue() {
        return value;
    }

    public void setValue(Character value) {
        this.value = value;
    }

    public HashMap<Character, Node> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Character, Node> children) {
        this.children = children;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

}
