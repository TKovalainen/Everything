package oy.interact.tira.student;

import oy.interact.tira.util.StackInterface;

public class StackImplementation<E> implements StackInterface<E>{

    private static final int DEFAULT_STACK_SIZE = 10;
    private E[] array;
    int size;
    int top;    

    @SuppressWarnings("unchecked")
    public StackImplementation(int size){
        this.size = size;
        this.array = (E[]) new Object[size];
        top = -1;
    }

    public StackImplementation(){
        this(DEFAULT_STACK_SIZE);
        top = -1;
    }

    public int capacity(){
        return array.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void push(E element) throws OutOfMemoryError, NullPointerException{
        
        if(element == null){
            throw new NullPointerException();
        }
        
        if(array[size-1] != null){
            int newSize = array.length+1;
            E[] temp = (E[]) new Object[newSize];
            for(int i = 0; i < size; i++){
                temp[i] = array[i];
            }
            array = temp;
            size = newSize;
        }
        top++;
        array[top] = element;

    }
    @Override
    public E pop() throws IllegalStateException{
        if(isEmpty()){
            throw new IllegalStateException("Cannot invoke method pop(), since the stack is empty", null);
        }
        int oldTop = top;
        E temp = array[top];
        top--;
        array[oldTop] = null;
        return (E) temp;

    }
    @Override
    public E peek() throws IllegalStateException{
        if(isEmpty()){
            throw new IllegalStateException("Stack is empty, cannot peek element", null);
        }
        return array[top];
    }
    @Override
    public int size(){
        return top + 1;
    }
    @Override
    public boolean isEmpty(){
        return (top == -1);
    }

    @SuppressWarnings("unchecked")
    public void clear(){
        array = (E[]) new Object[size];
        top = -1;

    }

    @Override
    public String toString(){
        
        StringBuilder s = new StringBuilder();
        s.append("[");
        for(int i = 0; i < array.length; i++){
            if(array[i] == null){
                break;
            }
            s.append(array[i]);
            if(i < top){
                s.append(", ");
            }            
        }
        s.append("]");

        return s.toString();
    }

}