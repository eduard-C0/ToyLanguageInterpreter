package Model.ADT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyLockTable implements MyILockTable{
    private AtomicInteger freeValue;
    private Map<Integer, Integer> lockTable;

    public MyLockTable (HashMap<Integer, Integer> lockTable)
    {
        this.lockTable = lockTable;
    }

    public MyLockTable(){
        this.lockTable = new HashMap<>();
        freeValue = new AtomicInteger();
    }

    @Override
    public int allocate(int value) {
        int free = freeValue.incrementAndGet();
        lockTable.put(free,value);
        return free;
    }

    @Override
    public int get(int address) {
        return lockTable.get(address);
    }

    @Override
    public void put(int address, int value) {
        lockTable.put(address,value);
    }

    @Override
    public int deallocate(int address) {
        return lockTable.remove(address);
    }

    @Override
    public boolean exists(int address) {
        return lockTable.containsKey(address);
    }

    @Override
    public void setContent(Map<Integer, Integer> map) {
        lockTable = map;
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return lockTable;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(var element:lockTable.keySet())
        {
            if(element != null)
            {
                str.append(element.toString()).append("->").append(lockTable.get(element).toString()).append('\n');
            }
        }
        return str.toString();
    }
}
