package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.PrgState;
import Model.Type.Type;

public interface IStmt {
    public PrgState execute(PrgState state) throws MyException,ExpressionException;
    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException;

}

