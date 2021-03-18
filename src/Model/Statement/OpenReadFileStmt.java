package Model.Statement;
import Model.ADT.*;
import Model.PrgState;
import Model.Expression.Exp;
import Exception.*;
import Model.ValueType.*;
import Model.Type.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.FileNotFoundException;


public class OpenReadFileStmt implements IStmt{

    private Exp expression;

    public OpenReadFileStmt(Exp expression){
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIDictionary<String,Value> symtable = state.getStackOfSymTable().peek();
        MyIDictionary<StringValue,BufferedReader> fileTable = state.getFileTable();
        MyIStack<IStmt> stack = state.getStack();

        Value val = expression.eval(symtable, state.getPrgHeap());

        if(val.getType().equals(new StringType())){

            StringValue stringVal = (StringValue) val;
            if(!fileTable.isDefined(stringVal)){
                try{
                    Reader reader = new FileReader(stringVal.getValue());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    fileTable.update(stringVal,bufferedReader);
                }
                catch (FileNotFoundException err)
                {
                    throw new StmtException(err.getMessage());
                }
            }
            else{
                throw new StmtException("The file is already in use!");
            }
        }
        else{
            throw new StmtException("Expression couldn't be evaluated to a string value in File Open!");
        }
        state.setExeStack(stack);
        state.setSymTable(symtable);
        state.setFileTable(fileTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type expressionType = expression.typecheck(typeEnvironment);
        if(expressionType.equals(new StringType()))
        {
            return typeEnvironment;
        }
        else {
            throw new StmtException("The close file expression is not a string!");
        }
    }

    @Override
    public String toString() {
        return "OpenReadFile(" + expression.toString() + ")";
    }
}
