package Model.Statement;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Model.ValueType.Value;

public class AssignStmt implements IStmt {
    String id;
    Exp expression;

    public AssignStmt(String string, Exp expression){
        id = string;
        this.expression=expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, StmtException {
        MyIStack<IStmt> exeStack = state.getStack();
        MyIDictionary<String,Value> symTable = state.getStackOfSymTable().peek();
        Value value = expression.eval(symTable,state.getPrgHeap());

        if(symTable.isDefined(id))
        {
            Type type = (symTable.lookup(id)).getType();
            if(value.getType().equals(type)){
                symTable.update(id,value);
            }
            else{
                throw new MyException("Declared type and assigned expression don't match!");
            }
        }
        else{
            throw new MyException("The variable" + id + "wasn't declared before.");
        }
        state.setExeStack(exeStack);
        state.setSymTable(symTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(!typeEnvironment.isDefined(id)){
            throw new StmtException("The variable " + id + " is not defined in the assignment statement ");
        }
        else{
            Type varType = typeEnvironment.lookup(id);
            Type expressionType = expression.typecheck(typeEnvironment);
            if(varType.equals(expressionType))
            {
                return typeEnvironment;
            }
            else {
                throw new StmtException("The right side and left side of the assignment " + this.toString() + " have different types");
            }
        }
    }

    @Override
    public String toString() {
        return id+"="+ expression.toString();
    }
}
