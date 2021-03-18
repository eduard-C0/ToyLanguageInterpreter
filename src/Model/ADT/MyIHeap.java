package Model.ADT;


import java.util.Map;

public interface MyIHeap<T> {
    int allocate(T value);
    T get(int address);
    void put(int address, T value);
    T deallocate(int address);
    String toString();
    boolean exists(int address);
    void setContent(Map<Integer,T>map);
    Map<Integer,T>getContent();
}
