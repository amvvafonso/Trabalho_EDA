package utils;

public interface LinkedList {
    public boolean isEmpty(); 
    public void addFirst(Object o); 
    public void addLast(Object o); 
    public boolean contains(Object o); 
    public boolean remove(Object o); 
    public Object peekFirst(); 
    public Object peekLast();  
}
