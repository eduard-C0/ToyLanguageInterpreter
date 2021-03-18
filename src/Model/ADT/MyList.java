package Model.ADT;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import Exception.ADTException;
public class MyList<T> implements MyIList<T> {
    private final List<T> list;
    public MyList(){
        list = new ArrayList<>();
    }
    @Override
    public void add(T element) {
        list.add(element);
    }

    @Override
    public void remove(T element) {
        try{
            list.remove(element);
        }
        catch (NoSuchElementException err)
        {
            throw new ADTException("No such element in the list!");
        }
    }

    @Override
    public T get(int position) {
        try {
            return list.get(position);
        }
        catch (IndexOutOfBoundsException err)
        {
            throw new ADTException("Index out of bounds!");
        }
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Stream<T> stream() {
        synchronized (this.list) {
            return this.list.stream();
        }
    }
}
