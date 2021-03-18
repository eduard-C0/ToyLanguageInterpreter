package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyILockTable;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

public class AwaitLatchStmt implements IStmt{
    private String variable;

    public AwaitLatchStmt(String variable)
    {
        this.variable = variable;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        try {
            MyIStack<IStmt> stack = state.getStack();
            MyIDictionary<String, Value> symbolTable = state.getSymTable();
            MyILockTable latchTable = state.getCountDownLatch();
            if (!symbolTable.isDefined(variable)) {
                throw new MyException("Nope!");
            }
            Value value = symbolTable.lookup(variable);
            if (!value.getType().equals(new IntegerType())) {
                throw new MyException("Not int type!");
            }
            int index = ((IntegerValue) value).getValue();
            int latchValue = latchTable.get(index);
            if (latchValue == -1)
                throw new MyException("No such index in the LockTable!");
            else if (latchValue > 0){
                stack.push(new AwaitLatchStmt(variable));
            }

        }
        catch (Exception err)
        {
            err.printStackTrace();
        }

        return null;
    }


    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type typevar = typeEnvironment.lookup(variable);

        if(typevar instanceof IntegerType)
            return typeEnvironment;

        throw new MyException("CountDownLatch wrong type !");
    }

    @Override
    public String toString() {
        return "Await(" + variable + ')';
    }
}
