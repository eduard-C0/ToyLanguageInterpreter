package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.Type;
import Model.ValueType.Value;
import Model.Expression.Exp;
import Model.ADT.MyIList;

public class PrintStmt implements IStmt{
    Exp expression;

    public PrintStmt(Exp expression){
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException,MyException {
        MyIStack<IStmt> stack = state.getStack();
        MyIList<Value> output = state.getOut();
        output.add(expression.eval(state.getStackOfSymTable().peek(), state.getPrgHeap()));
        state.setExeStack(stack);
        state.setOut(output);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        expression.typecheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() +")";
    }
}
