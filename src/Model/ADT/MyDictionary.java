package Model.ADT;
import Exception.ADTException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {
    private HashMap<K,V> dictionary;

    public MyDictionary()
    {
        dictionary = new HashMap<>();
    }

    @Override
    public V lookup(K key) {
        return dictionary.get(key);
    }

    @Override
    public boolean isDefined(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void add(K key, V value) {
        if (dictionary.containsKey(key)){
            throw new ADTException("Element already in the dictionary!");
        }
        dictionary.put(key,value);
    }

    @Override
    public void remove(K key) {
        if(!dictionary.containsKey(key)){
            throw new ADTException("This key doesn't exist in the dictionary!");
        }
        dictionary.remove(key);
    }

    @Override
    public void update(K key, V value) {
        dictionary.put(key,value);
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<K, V> el : dictionary.entrySet()) {
            content.append(el.getKey().toString()).append("=").append(el.getValue().toString()).append(" ");
        }
        return content.toString();
    }

    @Override
    public Map<K, V> getContent() {
        return dictionary;
    }

    @Override
    public MyIDictionary<K, V> copy() {
        MyIDictionary<K,V> newDict = new MyDictionary<>();
        for (K key : dictionary.keySet())
        {
            newDict.add(key,dictionary.get(key));
        }
        return newDict;
    }

    @Override
    public Stream<Map.Entry<K, V>> stream() {
        return this.dictionary.entrySet().stream();
    }
}
