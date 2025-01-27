package oy.interact.tira.student;

import java.util.Comparator;
import java.util.function.Predicate;

import oy.interact.tira.util.Pair;
import oy.interact.tira.util.TIRAKeyedOrderedContainer;
import oy.interact.tira.util.Visitor;

public class BinarySearchTreeContainer<K extends Comparable<K>, V> implements TIRAKeyedOrderedContainer<K, V> {


    private TreeNode<K, V> root;
    private int count = 0;
    private Comparator<K> comparator; //käytä vertailussa!!

    public BinarySearchTreeContainer(Comparator<K> comparator){
        this.comparator = comparator;
        this.root = null;
        this.count = 0;
    }

    @Override
    public void add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
        if(root == null){
           root = new TreeNode<K, V>(key, value);
           count++;
        }else{
            if(root.insert(key, value, comparator)){
                count++;
            }
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key is null");
        }
        return root != null ? root.get(key, comparator) : null;
    }

    @Override
    public V remove(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public V find(Predicate<V> searcher) {
        if (root == null) {
            return null;
        }

        StackImplementation<TreeNode<K, V>> stack = new StackImplementation<>();
        TreeNode<K, V> current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            current = stack.pop();
            if (searcher.test(current.getValue())) {
                return current.getValue();
            }
            current = current.getRight();
        }

        return null;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void ensureCapacity(int capacity) throws OutOfMemoryError, IllegalArgumentException {
        //Tyhjä metodi
    }

    @Override
    public void clear() {
        root = null;
        count = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V>[] toArray() throws Exception {
        if(root == null){
            Pair<K, V>[] emptyArray = (Pair<K, V>[]) new Pair[0];
            return emptyArray;
        }
        Pair<K, V>[] array = (Pair<K, V>[]) new Pair[count];
        int index = 0;
        root.toArray(array, index);
        return array;
    }

    @Override
    public int indexOf(K itemKey) {
        if(root == null){
            return -1;
        }

        int index = 0;
        TreeNode<K, V> current = root;
        TreeNode<K, V> parent = null;
        StackImplementation<TreeNode<K, V>> nodeStack = new StackImplementation<>();

        while(!nodeStack.isEmpty() || current != null){
            if(current != null){
                nodeStack.push(current);
                parent = current;
                current = current.getLeft();
            }else{
                parent = nodeStack.pop();
                current = parent.getRight();
                if(parent.getKey().equals(itemKey)){
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    @Override
    public Pair<K, V> getIndex(int index) throws IndexOutOfBoundsException {

        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

        StackImplementation<TreeNode<K, V>> stack = new StackImplementation<>();
        TreeNode<K, V> current = root;
        int currentIndex = 0;

        while(current != null || !stack.isEmpty()) {
            while(current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            current = stack.pop();
            if(currentIndex == index) {
                return new Pair<>(current.getKey(), current.getValue());
            }
            currentIndex++;
            current = current.getRight();
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public int findIndex(Predicate<V> searcher) {
        if (root == null) {
            return -1;
        }

        StackImplementation<TreeNode<K, V>> stack = new StackImplementation<>();
        TreeNode<K, V> current = root;
        int index = 0;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            current = stack.pop();
            if (searcher.test(current.getValue())) {
                return index;
            }
            index++;
            current = current.getRight();
        }

        return -1;
    }

    @Override
    public void accept(Visitor<K, V> visitor) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }
    
}
