package Model.ADT;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface MyIDictionary<K,V> {
    V lookup(K key);
    void remove(K key);
    void add(K key, V value);
    String toString();
    boolean isDefined(K key);
    Map<K,V> getContent();
    void update(K key, V value);
    public MyIDictionary<K,V>copy();

    Stream<Map.Entry<K,V>> stream();
}
