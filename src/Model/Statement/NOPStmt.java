package Model.Statement;
import Model.ADT.MyIDictionary;
import Model.PrgState;
import Exception.*;
import Model.Type.Type;

public class NOPStmt implements IStmt{
    @Override
    public PrgState execute(PrgState state) throws MyException {
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "NOP";
    }
}
