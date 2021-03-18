package Model.Statement;
import Model.ADT.*;
import Model.PrgState;
import Model.Expression.Exp;
import Exception.*;
import Model.ValueType.*;
import Model.Type.*;

import java.io.*;

public class CloseReadFileStmt implements IStmt{
    private Exp expression;

    public CloseReadFileStmt(Exp expression){
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIDictionary<String,Value> symtable = state.getStackOfSymTable().peek();
        MyIDictionary<StringValue,BufferedReader> fileTable = state.getFileTable();
        MyIStack<IStmt> stack = state.getStack();

        Value val = expression.eval(symtable,state.getPrgHeap());

        if (val.getType().equals(new StringType())){
            StringValue stringValue = (StringValue)val;
            if(fileTable.isDefined(stringValue)){
                BufferedReader bufferedReader = fileTable.lookup(stringValue);
                try{
                    bufferedReader.close();
                }
                catch (IOException err)
                {
                    throw new StmtException(err.getMessage());
                }
                fileTable.remove(stringValue);
            }
            else {
                throw new StmtException("The file doesn't exist in the file table!");
            }
        }
        else{
            throw new StmtException("Expression could not be evaluated to a string in File Close!");
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
            throw new StmtException("The close file expression is not a string!.");
        }
    }

    @Override
    public String toString() {
        return "CloseReadFile(" + expression.toString() + ")";
    }
}
