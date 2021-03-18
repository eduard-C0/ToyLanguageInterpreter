package Model.ADT;

import java.util.Optional;
import java.util.stream.Stream;

public interface MyIList<T> {
    void add(T element);
    void remove(T element);
    void clear();
    public boolean isEmpty();
    T get(int position);
    String toString();

    int size();

    Stream<T> stream();
}

