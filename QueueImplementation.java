package oy.interact.tira.student;


import oy.interact.tira.util.QueueInterface;


public class QueueImplementation<E> implements QueueInterface<E>{
    
    private int front, rear, capacity, count;
    private E[] queue;
    private static final int DEFAULT_Q_SIZE = 10;

    @SuppressWarnings ("unchecked")
    public QueueImplementation(int capacity){
        this.capacity = capacity;
        queue = (E[]) new Object[capacity];
        front = 0;
        rear = -1;
        count = 0;
    }
    
    @SuppressWarnings("unchecked")
    public QueueImplementation(){
        count = 0;
        this.capacity = DEFAULT_Q_SIZE;
        this.queue = (E[]) new Object[capacity];
        front = 0;
        rear = -1;
        
    }
    

    public int capacity(){
        return capacity;
    }

    @SuppressWarnings("unchecked")
    public void enqueue(E element) throws OutOfMemoryError, NullPointerException{

        if(capacity == rear){
            throw new OutOfMemoryError("The queue is full!");
        }
        if(element == null){
            throw new NullPointerException("Element is null, cannot be added to queue");
        }
        if(queue[rear+1] == null){
            int newSize = capacity + 1;
            E[] temp = (E[]) new Object[newSize];
            for(int i = 0; i < capacity; i++){
                temp[i] = queue[i];
            }
            queue = temp;
            capacity = newSize;
        }
        if(rear == capacity){
            rear = -1;
        }
        rear++;
        queue[rear] = element;
        count++;
        
    }

    @Override
    public E dequeue() throws IllegalStateException{
        if(count == 0){
            throw new IllegalStateException("Queue is empty, cannot remove elements", null);
        }
        E temp = (E) queue[front];
        front++; 
        if(front >= capacity){
            front = 0;
        }
        count--;
        return temp;
        
    }
    
    public E element() throws IllegalStateException{
        if(isEmpty()){
            throw new IllegalStateException("Queue is empty.", null);
        }
        return queue[front];
    }

    public int size(){
        return count;
    }

    public boolean isEmpty(){
        if(count == 0){
            return true;
        }
        else return false;
    }

    @SuppressWarnings("unchecked")
    public void clear(){

        count = 0;
        rear = -1;
        front = 0;
        queue = (E[]) new Object[capacity];
        

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        if (isEmpty()) {
            sb.append("]");
            return sb.toString();
        }
        
        int index = front;
        for (int i = 0; i < count - 1; i++) {
            sb.append(queue[index]).append(", ");
            index = (index + 1) % capacity;
        }
        sb.append(queue[(front + count - 1) % capacity]).append("]");
    
        return sb.toString();
    }


}
