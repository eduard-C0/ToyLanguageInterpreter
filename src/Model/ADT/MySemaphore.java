package Model.ADT;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MySemaphore implements MyISemaphore{
    private  AtomicInteger freeValue;
    private  Map<Integer,Pair<Integer, List<Integer>>> semaphore;
    public MySemaphore (HashMap<Integer,Pair<Integer, List<Integer>>> semaphore)
    {
        this.semaphore = semaphore;
    }

    public MySemaphore()
    {
        this.semaphore = new HashMap<>();
        freeValue = new AtomicInteger();
    }
    @Override
    public int allocate(Pair<Integer, List<Integer>> value) {
        int free = freeValue.incrementAndGet();
        semaphore.put(free,value);
        return free;
    }

    @Override
    public Pair<Integer, List<Integer>> get(int address) {
        return semaphore.get(address);
    }

    @Override
    public void put(int address, Pair<Integer, List<Integer>> value) {
        semaphore.put(address,value);
    }

    @Override
    public Pair<Integer, List<Integer>> deallocate(int address) {
        return semaphore.remove(address);
    }

    @Override
    public boolean exists(int address) {
        return semaphore.containsKey(address);
    }

    @Override
    public void setContent(Map<Integer, Pair<Integer, List<Integer>>> map) {
        semaphore = map;
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return semaphore;
    }

    @Override
    public void update(int address, Pair<Integer, List<Integer>> value) {
        semaphore.replace(address,value);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (var element : semaphore.keySet()) {
            if (element != null) {
                str.append(element.toString()).append("->").append(semaphore.get(element).toString()).append('\n');
            }
        }
        return str.toString();
    }
}
