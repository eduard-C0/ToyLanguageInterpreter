package Model.ADT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyLatchTable implements MyILatch{
    private AtomicInteger freeValue;
    private Map<Integer, Integer> latchTable;

    public MyLatchTable (HashMap<Integer, Integer> latchTable)
    {
        this.latchTable = latchTable;
    }


    public MyLatchTable(){
        this.latchTable = new HashMap<>();
        freeValue = new AtomicInteger();
    }

    @Override
    public int allocate(int value) {
        int free = freeValue.incrementAndGet();
        latchTable.put(free,value);
        return free;
    }

    @Override
    public int get(int address) {
        return latchTable.get(address);
    }

    @Override
    public void put(int address, int value) {
        latchTable.put(address,value);
    }

    @Override
    public int deallocate(int address) {
        return latchTable.remove(address);
    }

    @Override
    public boolean exists(int address) {
        return latchTable.containsKey(address);
    }

    @Override
    public void setContent(Map<Integer, Integer> map) {
        latchTable = map;
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return latchTable;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(var element:latchTable.keySet())
        {
            if(element != null)
            {
                str.append(element.toString()).append("->").append(latchTable.get(element).toString()).append('\n');
            }
        }
        return str.toString();
    }
}
