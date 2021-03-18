package Model.Statement;

import Model.ADT.MyDictionary;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyILockTable;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

import javax.swing.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStmt implements IStmt {
    private final String variable;
    private static final Lock lock = new ReentrantLock();

    public NewLockStmt(String variable)
    {
        this.variable = variable;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();
        MyILockTable lockTable = state.getLockTable();
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if(!symTable.isDefined(variable))
        {
            throw new MyException("Nope!");
        }

        Value value = symTable.lookup(variable);
        if(!value.getType().equals(new IntegerType()))
        {
            throw new MyException("Not int type!");
        }

        int index = ((IntegerValue)value).getValue();

        int position = lockTable.allocate(index);

        lockTable.put(position, -1);
        symTable.update(variable,new IntegerValue(position));

        state.setLockTable(lockTable);
        state.setSymTable(symTable);
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "NewLockStmt(" + variable + ')';
    }
}
