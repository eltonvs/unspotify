/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imd.lp2.unspotify.adt.bst;

/**
 * Classe para gerenciamento de Nós da BST
 *
 * @author Elton Viana
 * @param <Type>
 */
public class Node<Type extends Comparable<Type>> {

    private Type value;
    private Node<Type> leftChild;
    private Node<Type> rightChild;

    /**
     * Construtor padrão para um nó vazio
     */
    public Node() {
        leftChild = null;
        rightChild = null;
    }

    /**
     * Construtor para um nó preenchido
     *
     * @param value
     */
    public Node(Type value) {
        this();
        this.value = value;
    }

    public Type getValue() {
        return value;
    }

    public Node<Type> getLeftChild() {
        return leftChild;
    }

    public Node<Type> getRightChild() {
        return rightChild;
    }

    public void setValue(Type value) {
        this.value = value;
    }

    public void setLeftChild(Node<Type> leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node<Type> rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Verificação para se o nó possui filhos
     *
     * @return boolean
     */
    public boolean hasChild() {
        return this.getLeftChild() != null || this.getRightChild() != null;
    }

    /**
     * Verificação para se o nó possui filhos a esquerda
     *
     * @return boolean
     */
    public boolean hasOnlyLeftChild() {
        return this.getLeftChild() != null && this.getRightChild() == null;
    }

    /**
     * Verificação para se o nó possui filhos a direita
     *
     * @return boolean
     */
    public boolean hasOnlyRightChild() {
        return this.getLeftChild() == null && this.getRightChild() != null;
    }

}
