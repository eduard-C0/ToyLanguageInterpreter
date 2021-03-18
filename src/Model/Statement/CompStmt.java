package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.Type;

public class CompStmt implements IStmt{
    private IStmt statement1;
    private IStmt statement2;

    public CompStmt(IStmt statement1, IStmt statement2)
    {
        this.statement1 = statement1;
        this.statement2 = statement2;

    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        MyIStack<IStmt> stack = state.getStack();
        stack.push(statement2);
        stack.push(statement1);
        state.setExeStack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
            return statement2.typecheck(statement1.typecheck(typeEnvironment));
    }

    @Override
    public String toString() {
        return "(" + statement1 + ";" + statement2 + ")";
    }

    public static String recursivePrint(IStmt statement)
    {
        StringBuilder show = new StringBuilder();
        if (statement instanceof CompStmt) {
            CompStmt compound = (CompStmt) statement;
            show.append(recursivePrint(compound.getFirst()));
            show.append(recursivePrint(compound.getSecond()));
        } else {
        show.append(statement).append("\n");
    }
        return show.toString();

    }

    private IStmt getSecond() {
        return this.statement2;
    }

    private IStmt getFirst() {
        return this.statement1;
    }
}
