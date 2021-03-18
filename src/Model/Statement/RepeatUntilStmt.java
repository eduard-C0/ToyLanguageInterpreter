package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.Expression.NotExpression;
import Model.PrgState;
import Model.Type.Type;
import Exception.*;
public class RepeatUntilStmt implements IStmt{
    private final IStmt statement;
    private final Exp expression;

    public RepeatUntilStmt(IStmt statement, Exp expression)
    {
        this.expression = expression;
        this.statement = statement;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        IStmt repeat = new CompStmt(statement, new WhileStmt(new NotExpression(expression), statement));
        stack.push(repeat);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Repeat{" + statement.toString() +"} until( " +expression.toString() + ')';
    }
}
