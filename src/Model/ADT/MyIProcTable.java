package Model.ADT;

import Model.Statement.IStmt;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface MyIProcTable {
    Pair<List<String>,IStmt> lookup(String key);
    void remove(String key);
    void add(String key, Pair<List<String>,IStmt> value);
    String toString();
    boolean isDefined(String key);
    Map<String,Pair<List<String>,IStmt>> getContent();
    void update(String key, Pair<List<String>,IStmt> value);
    public MyIDictionary<String,Pair<List<String>,IStmt>>copy();

    Stream<Map.Entry<String,Pair<List<String>,IStmt>>> stream();
}
