package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyILockTable;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnlockStmt implements IStmt{
    private String variable;
    private static Lock lock = new ReentrantLock();

    public UnlockStmt(String variable)
    {
        this.variable = variable;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();

        MyILockTable lockTable = state.getLockTable();
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if(!symTable.isDefined(variable)){
            throw new MyException("Nope!");
        }
        Value value = symTable.lookup(variable);
        if(!value.getType().equals(new IntegerType()))
        {
            throw new MyException("Not int type!");
        }
        Integer index = ((IntegerValue)value).getValue();

        if(!lockTable.exists(index))
        {
            throw new MyException("No such variable in the LockTable!");
        }
        int position  = lockTable.get(index);
        if(position == -1)
            throw new MyException("No such index in the LockTable!");
        if(position == state.getStateID())
        {
            lockTable.put(index,-1);
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Unlock(" + variable + ')' ;
    }
}
