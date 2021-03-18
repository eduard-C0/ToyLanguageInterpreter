package Model.ADT;

import Model.Statement.IStmt;
import javafx.util.Pair;
import Exception.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MyProcTable implements MyIProcTable{
    private  Map<String,Pair<List<String>, IStmt>> procedures;

    public MyProcTable(HashMap<String,Pair<List<String>, IStmt>> procedures)
    {
        this.procedures = procedures;
    }

    public MyProcTable()
    {
        this.procedures = new HashMap<>();
    }
    @Override
    public Pair<List<String>, IStmt> lookup(String key) {
        return procedures.get(key);
    }

    @Override
    public void remove(String key) {
        if(!procedures.containsKey(key)){
            throw new ADTException("This key doesn't exist in the dictionary!");
        }
        procedures.remove(key);
    }

    @Override
    public void add(String key, Pair<List<String>, IStmt> value) {
        if (procedures.containsKey(key)){
            throw new ADTException("Element already in the dictionary!");
        }
        procedures.put(key,value);
    }

    @Override
    public boolean isDefined(String key) {
        return procedures.containsKey(key);
    }

    @Override
    public Map<String, Pair<List<String>, IStmt>> getContent() {
        return procedures;
    }

    @Override
    public void update(String key, Pair<List<String>, IStmt> value) {
        procedures.put(key,value);
    }

    @Override
    public MyIDictionary<String, Pair<List<String>, IStmt>> copy() {
        MyIDictionary<String,Pair<List<String>, IStmt>> newDict = new MyDictionary<>();
        for (String key : procedures.keySet())
        {
            newDict.add(key,procedures.get(key));
        }
        return newDict;
    }

    @Override
    public Stream<Map.Entry<String, Pair<List<String>, IStmt>>> stream() {
        return this.procedures.entrySet().stream();
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String,Pair<List<String>, IStmt>> el : procedures.entrySet()) {
            content.append(el.getKey().toString()).append("=").append(el.getValue().toString()).append(" ");
        }
        return content.toString();
    }
}
