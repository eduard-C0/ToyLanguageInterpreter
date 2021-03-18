package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyISemaphore;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStmt implements IStmt{
    private  String variable;
    private static Lock lock = new ReentrantLock();

    public ReleaseStmt (String variable) {
        this.variable = variable;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();
        try {
            MyISemaphore semaphoreTable = state.getSemaphore();
            MyIStack<IStmt> stack = state.getStack();
            MyIDictionary<String, Value> symbolTable = state.getStackOfSymTable().peek();

            if (!symbolTable.isDefined(variable)) {
                throw new MyException("Nope!");
            }

            Value value = symbolTable.lookup(variable);

            if (!value.getType().equals(new IntegerType())) {
                throw new MyException("Not int type!");
            }
            Integer index = ((IntegerValue) value).getValue();

            Pair<Integer, List<Integer>> semaphorValue = semaphoreTable.get(index);
            ArrayList<Integer> threads = (ArrayList<Integer>) semaphorValue.getValue();
            Integer maxNumber = semaphorValue.getKey();

            if (threads.contains(state.getStateID())) {
                int in = threads.indexOf(state.getStateID());
                threads.remove(in);
            }

            semaphoreTable.update(index, new Pair<>(maxNumber, threads));

        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
        finally {
            lock.unlock();
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Release(" + variable + ')';
    }

}
