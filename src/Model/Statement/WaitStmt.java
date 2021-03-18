package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.ValueExpression;
import Model.Expression.VarExpression;
import Model.PrgState;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;

public class WaitStmt implements IStmt{
    private Integer value;

    public WaitStmt(Integer value)
    {
        this.value = value;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        if (value != 0) {
            MyIStack<IStmt> stack = state.getStack();
            stack.push(new CompStmt(new PrintStmt(new ValueExpression(new IntegerValue(value))),new WaitStmt(value - 1)));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Wait(" + value +')';
    }
}
