package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.Value;

public class ReturnStmt implements IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        MyIStack<MyIDictionary<String, Value>> stackOfSymTable = state.getStackOfSymTable();
        stackOfSymTable.pop();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "return()";
    }
}
