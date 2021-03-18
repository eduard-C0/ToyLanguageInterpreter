package Model.ADT;

import java.util.Map;

public interface MyILatch {
    int allocate(int value);
    int get(int address);
    void put(int address, int value);
    int deallocate(int address);
    String toString();
    boolean exists(int address);
    void setContent(Map<Integer,Integer> map);
    Map<Integer,Integer>getContent();
}
