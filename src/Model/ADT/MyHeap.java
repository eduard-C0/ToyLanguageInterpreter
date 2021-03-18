package Model.ADT;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MyHeap<T> implements MyIHeap<T>{
    private AtomicInteger freeValue;
    private Map<java.lang.Integer, T> heap;

    public MyHeap(HashMap<java.lang.Integer, T> heap){
        this.heap = heap;
    }

    public MyHeap(){
        this.heap = new HashMap<java.lang.Integer, T>();
        freeValue = new AtomicInteger();
    }

    @Override
    public int allocate(T value) {
        int free = freeValue.incrementAndGet();
        heap.put(free,value);
        return free;
    }

    @Override
    public T get(int address) {
        return heap.get(address);
    }

    @Override
    public void put(int address, T value) {
        heap.put(address,value);
    }

    @Override
    public T deallocate(int address) {
        return heap.remove(address);
    }

    public Map<java.lang.Integer, T> getHeap() {
        return heap;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(var element:heap.keySet())
        {
            if(element != null)
            {
                str.append(element.toString()).append("->").append(heap.get(element).toString()).append('\n');
            }
        }
        return str.toString();
    }

    @Override
    public boolean exists(int address) {
        return heap.containsKey(address);
    }

    @Override
    public void setContent(Map<Integer, T> map) {
        heap = map;
    }

    @Override
    public Map<Integer, T> getContent() {
        return heap;
    }
}
