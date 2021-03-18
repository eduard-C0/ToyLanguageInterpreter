package Model.ADT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import Exception.ADTException;
import Model.Statement.CompStmt;

public class MyStack<T> implements MyIStack<T> {
    private Stack<T> stack;
    public MyStack(Stack<T> stack){
        this.stack = stack;
    }
    public MyStack(){
        stack = new Stack<T>();
    }

    @Override
    public T pop() {
        if(stack.size() == 0)
        {
            throw new ADTException("Stack is empty!");
        }
        return stack.pop();
    }

    @Override
    public void push(T element) {
        stack.push(element);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        String msg = "";
        if (stack.size() > 0)
        {
            if(!(stack.peek() instanceof CompStmt))
                msg = msg + stack.peek().toString() + "\n";
        }
        if (stack.size() == 2)
        {
            if(!(stack.get(0) instanceof CompStmt))
                msg = msg + stack.get(0).toString() + "\n";
        }
        for (var element : stack) {
            if (element instanceof CompStmt)
            {
                CompStmt el = (CompStmt) element;
                msg = msg + el.recursivePrint(el) + "\n";
            }
        }
        return msg;
    }

    @Override
    public T peek() {
        if (stack.size() == 0)
            return (T) stack;
        return stack.peek();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public Stack copy() {
        Stack<T> newStack = new Stack<>();
        newStack.addAll(this.stack);

        return newStack;
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>(stack);
        Collections.reverse(list);
        return list;
    }

    @Override
    public Stream<T> stream() {
        return this.stack.stream();
    }
}
