package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.Expression.RelationalExpression;
import Model.Expression.VarExpression;
import Model.PrgState;
import Model.Type.Type;
import javafx.beans.binding.BooleanExpression;

public class ForStmt implements IStmt{
    private final String variable;
    private final Exp initialization;
    private final Exp condition;
    private final Exp increment;
    private final IStmt statement;

    public ForStmt(String variable ,Exp initialization,Exp condition,Exp increment, IStmt statement)
    {
        this.variable = variable;
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.statement = statement;
    }


    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        IStmt forStatement = new CompStmt(new AssignStmt(variable,initialization),new WhileStmt(new RelationalExpression(new VarExpression("v"),1,condition),new CompStmt(statement,new AssignStmt(variable,increment))));
        stack.push(forStatement);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "for( " + variable + "=" + initialization.toString() + "; " + variable + "<" + condition.toString() + "; " + variable + "=" + increment.toString() + " ) \n " + statement.toString();
    }
}
