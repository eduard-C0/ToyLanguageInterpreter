package Model.Statement;
import Model.Type.*;
import Model.Type.Boolean;
import Model.Type.IntegerType;
import Model.ValueType.*;
import Model.ADT.*;
import Exception.*;
import Model.PrgState;

public class VarDeclaration implements IStmt{

    String name;
    Type type;

    public VarDeclaration(String string, Type t){
        name = string;
        type = t;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getStack();
        MyIDictionary<String,Value> table = state.getStackOfSymTable().peek();

        if(table.isDefined(name)){
            throw new MyException("Variable is already declared!");
        }
        else{
            //if(type.equals(new Integer())){
                //table.add(name, new IntegerValue());
          // }
            //else if(type.equals(new Boolean())){
               // table.add(name,new BooleanValue());
            //}
            //else if(type.equals(new StringType())){
               //table.add(name,new StringValue());
           //}
            //else if(type.equals(new ReferenceType()))
            //{
                //table.add(name,new ReferenceValue());
            //}
            //else {
                //throw new MyException("Type doesn't exist!");
            //}
            table.add(name,type.defaultValue());

        }
        state.setSymTable(table);
        state.setExeStack(stack);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(typeEnvironment.isDefined(name)){
            throw new StmtException("The variable is already defined!");
        }
        typeEnvironment.update(name,type);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "VarDeclaration{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
