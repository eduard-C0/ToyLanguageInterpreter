package Model.ADT;


import java.util.List;
import java.util.Stack;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public interface MyIStack<T> {

   T pop();
   void push(T element);
   boolean isEmpty();
   String toString();
    Stack copy();
    List<T> toList();
    T peek();
    int size();
    Stream<T> stream();
}

