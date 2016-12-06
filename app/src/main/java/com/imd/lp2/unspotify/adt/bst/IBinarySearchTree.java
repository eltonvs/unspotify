package com.imd.lp2.unspotify.adt.bst;

import java.util.ArrayList;

/**
 * @autor Elton Viana
 */

public interface IBinarySearchTree<Type extends Comparable<Type>> {

    boolean isEmpty();

    int getHeight();

    int getHeight(Node<Type> node);

    int getDepth(Node<Type> node);

    Node<Type> getSmallestElement();

    Node<Type> getBiggestElement();

    Node<Type> search(Type val);

    Node<Type> insert(Type element);

    boolean remove(Type val);

    void printInOrder();

    void printPreOrder();

    void printPostOrder();

    ArrayList<Type> getArrayList();

}
