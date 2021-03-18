package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyILockTable;
import Model.ADT.MyIStack;
import Model.Expression.ValueExpression;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountDownStmt implements IStmt{
    private String variable;
    private static Lock lock = new ReentrantLock();

    public CountDownStmt(String variable)
    {
        this.variable = variable;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        lock.lock();
        try {
            MyIStack<IStmt> stack = state.getStack();
            MyILockTable latchTable = state.getCountDownLatch();
            MyIDictionary<String, Value> symbolTable = state.getSymTable();

            if (!symbolTable.isDefined(variable)) {
                throw new MyException("Nope!");
            }

            Value value = symbolTable.lookup(variable);
            if (!value.getType().equals(new IntegerType())) {
                throw new MyException("Not int type!");
            }

            int index = ((IntegerValue) value).getValue();
            int latchValue = latchTable.get(index);
            if (latchValue == -1) {
                throw new MyException("No such index in the LatchTable");
            }
            if (latchValue > 0) {
                latchTable.put(index, latchValue - 1);
                stack.push(new PrintStmt(new ValueExpression(new IntegerValue(state.getStateID()))));
            }
            else
            {
                stack.push(new PrintStmt(new ValueExpression(new IntegerValue(state.getStateID()))));
            }
        }
        catch (MyException err)
        {
            err.toString();
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
        return "CountDown(" + variable  + ')';
    }
}
