package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Boolean;
import Model.Type.Type;
import Model.ValueType.BooleanValue;
import Model.ValueType.Value;

public class WhileStmt implements IStmt{
    private Exp expression;
    private  IStmt statement;

    public WhileStmt(Exp expression,IStmt statement){
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        MyIDictionary<String, Value> symtable = state.getStackOfSymTable().peek();
        Value value = expression.eval(symtable,state.getPrgHeap());
        if(value.getType().equals(new Boolean())){
            BooleanValue booleanValue = (BooleanValue) value;
            if(booleanValue.getValue()){
                stack.push(new WhileStmt(expression,statement));
                stack.push(statement);
            }
        }
        else {
            throw new StmtException("The While condition isn't a boolean type");
        }
        state.setExeStack(stack);
        return null;

    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type expressionType = expression.typecheck(typeEnvironment);
        if(expressionType.equals(new Boolean())){
            statement.typecheck(typeEnvironment.copy());
            return typeEnvironment;
        }
        else{
            throw new StmtException("The condition is not a boolean!");
        }
    }

    @Override
    public String toString() {
        return "(While (" + expression+ ")" + statement + ')';
    }
    
}
