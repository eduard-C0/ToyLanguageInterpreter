package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyISemaphore;
import Model.ADT.MySemaphore;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewSemaphoreStmt implements IStmt{
    private String variable;
    private Exp expression;
    private static Lock lock = new ReentrantLock();

    public NewSemaphoreStmt(String variable, Exp expression)
    {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        MyISemaphore semaphore = state.getSemaphore();

        Value v = expression.eval(symbolTable,state.getPrgHeap());
        Integer value = ((IntegerValue)v).getValue();
        Integer index = semaphore.allocate(new Pair<>(value,new ArrayList<>()));
        symbolTable.update(variable,new IntegerValue(index));
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "NewSemaphore(" + variable + ',' + expression.toString() +')';
    }
}
