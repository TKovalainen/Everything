package oy.interact.tira.student;

import java.util.Comparator;

import oy.interact.tira.util.Pair;

public class TreeNode<K extends Comparable<K>, V> {
    
    private Pair<K, V> keyValue;
    private TreeNode<K, V> leftChild;
    private TreeNode<K, V> rightChild;
    private TreeNode<K, V> parent;

    public TreeNode(K key, V value) {
        this.keyValue = new Pair<>(key, value);
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public K getKey() {
        return keyValue.getKey();
    }

    public V getValue() {
        return keyValue.getValue();
    }

    public TreeNode<K, V> getLeft() {
        return leftChild;
    }

    public TreeNode<K, V> getRight() {
        return rightChild;
    }

    public TreeNode<K, V> getParent() {
        return parent;
    }

    public void setLeft(TreeNode<K, V> left) {
        this.leftChild = left;
    }

    public void setRight(TreeNode<K, V> right) {
        this.rightChild = right;
    }

    public void setParent(TreeNode<K, V> parent) {
        this.parent = parent;
    }

    public boolean insert(K key, V value, Comparator<K> comparator) {

        if (this.keyValue.getValue().equals(value)) {
            this.keyValue = new Pair<>(key, value);
            return false;
        }
        

        if (comparator.compare(key, this.getKey()) < 0) {
            if (this.leftChild == null) {
                this.leftChild = new TreeNode<>(key, value);
                this.leftChild.setParent(this);
                return true;
            } else {
                return this.leftChild.insert(key, value, comparator);
            }
        } else {
            if (this.rightChild == null) {
                this.rightChild = new TreeNode<>(key, value);
                this.rightChild.setParent(this);
                return true;
            } else {
                return this.rightChild.insert(key, value, comparator);
            }
        }
    }

    public V get(K key, Comparator<K> comparator){
        if(comparator.compare(key, this.keyValue.getKey()) == 0){
            return this.keyValue.getValue();
        }else{
            if(comparator.compare(key, this.keyValue.getKey()) <= 0){
                if(this.leftChild != null){
                    return this.leftChild.get(key, comparator);
                }
            }else{
                if(this.rightChild != null){
                    return this.rightChild.get(key, comparator);
                }
            }
        }
        return null;
    }

    public int toArray(Pair<K, V>[] array, int index){
        if(this.leftChild != null){
            index = this.leftChild.toArray(array, index);
        }
        array[index] = new Pair<K, V>(this.keyValue.getKey(), this.keyValue.getValue());
        index++;
        if(this.rightChild != null){
            index = this.rightChild.toArray(array, index);
        }
        return index;
    }
    
}
