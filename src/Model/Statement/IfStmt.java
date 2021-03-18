package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Expression.Exp;
import Model.Type.Boolean;
import Model.Type.Type;
import Model.ValueType.BooleanValue;
import Model.ValueType.Value;

public class IfStmt implements IStmt {
    private Exp expression;
    private IStmt ThenStatement;
    private IStmt ElseStatement;


    public IfStmt(Exp expression, IStmt IF, IStmt ELSE){
        this.expression = expression;
        this.ThenStatement = IF;
        this.ElseStatement = ELSE;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException,ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        Value condition =  expression.eval(state.getStackOfSymTable().peek(), state.getPrgHeap());
        if(!condition.getType().equals(new Boolean())){
            throw new MyException("Condition isn't a boolean type!");
        }
        if(condition.equals(new BooleanValue(true))){
            stack.push(ThenStatement);
        }
        else{
            stack.push(ElseStatement);
        }
        state.setExeStack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type expressionType = expression.typecheck(typeEnvironment);
        if(expressionType.equals(new Boolean()))
        {
            ThenStatement.typecheck(typeEnvironment.copy());
            ElseStatement.typecheck(typeEnvironment.copy());
            return typeEnvironment;
        }
        else{
            throw new StmtException("The condition statement is not a boolean!");
        }
    }

    @Override
    public String toString() {
        return "if (" + expression + ") then {"
                + ThenStatement + "} else {"
                + ElseStatement + "}";
    }
}
