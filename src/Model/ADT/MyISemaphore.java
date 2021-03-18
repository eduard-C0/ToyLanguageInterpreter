package Model.ADT;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface MyISemaphore {
    int allocate(Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> get(int address);
    void put(int address, Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> deallocate(int address);
    String toString();
    boolean exists(int address);
    void setContent(Map<Integer,Pair<Integer, List<Integer>>> map);
    Map<Integer, Pair<Integer, List<Integer>>>getContent();
    void update(int address, Pair<Integer, List<Integer>> value);
}
