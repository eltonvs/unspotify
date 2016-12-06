package com.imd.lp2.unspotify.adt.trie;

import java.util.ArrayList;

/**
 * @autor Elton Viana
 */

public interface ITrie {

    boolean isEmpty();

    Node search(String value);

    boolean insert(String value);

    ArrayList<String> getChildren(String prefix);

}
