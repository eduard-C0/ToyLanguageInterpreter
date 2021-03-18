package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyILockTable;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLatchStmt implements IStmt{
    private final String variable;
    private Exp expression;

    private static Lock lock = new ReentrantLock();

    public NewLatchStmt (String variable,Exp expression)
    {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        MyILockTable countDownLatchTable = state.getCountDownLatch();
        MyIHeap<Value> heap = state.getPrgHeap();

        Value expr = expression.eval(symbolTable,heap);
        Value value = symbolTable.lookup(variable);
        if(!value.getType().equals(new IntegerType()))
        {
            throw new MyException("Not int type!");
        }
        int index = ((IntegerValue)value).getValue();

        int position = countDownLatchTable.allocate(index);

        countDownLatchTable.put(position, Integer.parseInt(String.valueOf(expr)));
        if(!symbolTable.isDefined(variable))
        {
            symbolTable.add(variable,new IntegerValue(position));
        }
        else {
            symbolTable.update(variable, new IntegerValue(position));
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type e = expression.typecheck(typeEnvironment);
        if(!(e instanceof IntegerType))
        {
            throw new MyException("Not int type!");
        }
        typeEnvironment.add(variable,new IntegerType());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "CountDownLatch(" + variable + ',' + expression + ')';
    }
}
