package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.Type;

public class SleepStmt implements IStmt{
    private Integer value;

    public SleepStmt(Integer value)
    {
        this.value = value;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        if (value != 0) {
            MyIStack<IStmt> stack = state.getStack();
            stack.push(new SleepStmt(value - 1));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Sleep(" + value.toString() +')';
    }
}
