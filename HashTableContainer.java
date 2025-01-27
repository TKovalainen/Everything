package oy.interact.tira.student;

import java.lang.reflect.Array;
import java.util.function.Predicate;


import oy.interact.tira.util.Pair;
import oy.interact.tira.util.TIRAKeyedContainer;

public class HashTableContainer<K extends Comparable<K>,V> implements TIRAKeyedContainer<K,V> {

    private long size;
    private Pair<K,V>[] array;
    private static final int DEFAULT_HASHTABLE_SIZE = 16;
    private static final double LOAD_FACTOR = 0.65;

    @SuppressWarnings("unchecked")
    public HashTableContainer() {
        this.size = 0;
        this.array = (Pair<K,V>[]) Array.newInstance(Pair.class, DEFAULT_HASHTABLE_SIZE);
    }

    @SuppressWarnings("unchecked")
    public HashTableContainer(int capacity){
        this.size = 0;
        this.array = (Pair<K,V>[]) Array.newInstance(Pair.class, capacity);
    }

    @Override
    public void add(K key, V value) throws OutOfMemoryError, IllegalArgumentException {
        int collisionCounter = 0;
        
        if(size >= array.length * LOAD_FACTOR){
            reallocate(array.length * 2);
        }
        long hash = key.hashCode();
        boolean added = false;
        do{
            long index = indexFor(hash, collisionCounter);
            if(array[(int) index] == null){
                array[(int) index] = new Pair<K,V>(key, value);
                size++;
                added = true;
            }else if(array[(int) index].getKey().equals(key)){
                array[(int) index].setValue(value);
                added = true;
            }else{
                collisionCounter++;       
            }

        }while(!added);

    }

    @SuppressWarnings("unchecked")
    private void reallocate(int newCapacity){
        Pair<K,V>[] oldArray = this.array;
        long oldArrayLength = oldArray.length;
        array = (Pair<K, V>[]) Array.newInstance(Pair.class, newCapacity);
        size = 0;
        for(int i = 0; i < oldArrayLength; i++){
            if(oldArray[i] != null){
                add(oldArray[i].getKey(), oldArray[i].getValue());
            }
        }
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        V valueFound = null;
        long collisionCounter = 0;
        long hash = key.hashCode();
        boolean looping = true;

        do{
            long index = indexFor(hash, collisionCounter);
            final Pair<K,V> pair = array[(int) index];
            if(pair == null){
                looping = false;    
            }else if(key.equals(pair.getKey())){
                valueFound = pair.getValue();
                looping = false;
            }else{
                collisionCounter++;
            }

        }while(looping);

        return valueFound;
    }

    long indexFor(long hash, long collisionCounter){
        return ((hash + collisionCounter) & 0x7fffffff) % array.length;
    }

    @Override
    public V remove(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public V find(Predicate<V> searcher) {
        for(Pair<K, V> pair : array) {
            if(pair != null && searcher.test(pair.getValue())) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return (int)size;
    }

    @Override
    public int capacity() {
        return Integer.MAX_VALUE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void ensureCapacity(int capacity) throws OutOfMemoryError, IllegalArgumentException {
        if(size >= capacity * (1.0 + LOAD_FACTOR)){
            throw new IllegalArgumentException("Not enough space for elements already in the hashtable");
        }
        if(size == 0){
           clear();
           array = null;
           array = (Pair<K,V>[]) Array.newInstance(Pair.class, capacity);
           return;
        }
        reallocate(capacity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        array = (Pair<K,V>[]) Array.newInstance(Pair.class, DEFAULT_HASHTABLE_SIZE);;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<K, V>[] toArray() throws Exception {
        Pair<K, V>[] result = (Pair<K, V>[]) Array.newInstance(Pair.class, (int) size);
        int index = 0;
        for(Pair<K, V> pair : array) {
            if(pair != null) {
                result[index++] = pair;
            }
        }
        return result;
    }
    
}
