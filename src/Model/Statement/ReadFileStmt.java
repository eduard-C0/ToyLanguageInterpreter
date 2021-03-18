package Model.Statement;
import Model.ADT.*;
import Model.PrgState;
import Model.Expression.Exp;
import Exception.*;
import Model.ValueType.*;
import Model.Type.*;
import java.io.*;

public class ReadFileStmt implements IStmt{
    private Exp expression;
    private String varName;
    private String fileName;

    public ReadFileStmt(Exp expression, String varName){
       this.expression = expression;
       this.varName = varName;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIDictionary<String, Value> symtable = state.getStackOfSymTable().peek();
        MyIDictionary<StringValue, BufferedReader>fileTable = state.getFileTable();
        MyIStack<IStmt> stack = state.getStack();
        if(symtable.isDefined(varName)){
            if(symtable.lookup(varName).getType().equals(new IntegerType())){
                Value value = expression.eval(symtable, state.getPrgHeap());
                if(value.getType().equals(new StringType())){
                    StringValue stringValue = (StringValue)value;
                    if(fileTable.isDefined(stringValue)){
                        BufferedReader bufferedReader = fileTable.lookup(stringValue);
                        try {
                            String line = bufferedReader.readLine();
                            Value intValue;
                            IntegerType type = new IntegerType();
                            if(line == null){
                                intValue = type.defaultValue();
                            }else{
                                intValue = new IntegerValue(java.lang.Integer.parseInt(line));
                            }
                            symtable.update(varName,intValue);
                        }
                        catch (IOException err)
                        {
                            throw new StmtException(err.getMessage());
                        }
                    }
                    else{
                        throw new StmtException("The file " + stringValue.getValue() + " is not in the File Table!");
                    }

                }
                else{
                    throw new StmtException("The value couldn't be evaluated to a string value!");
                }
            }
            else{
                throw new StmtException(varName + " is not of type int!");
            }
        }
        else{
            throw new StmtException(varName + " is not defined in Sym Table");
        }
        state.setExeStack(stack);
        state.setSymTable(symtable);
        state.setFileTable(fileTable);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(typeEnvironment.isDefined(varName)){
            Type variableType = typeEnvironment.lookup(varName);
            Type expressionType = expression.typecheck(typeEnvironment);

            if(!variableType.equals(new IntegerType())){
                throw new StmtException("The variable is not an integer.");
            }
            if(!expressionType.equals(new StringType())){
                throw new StmtException("The file name is not a string!");
            }
            return typeEnvironment;
        }
        else{
            throw new StmtException("The variable isn't defined!");
        }
    }

    @Override
    public String toString() {
        return "ReadFileStmt(" + " expression " + expression.toString() + " into varName " + varName;
    }
}


