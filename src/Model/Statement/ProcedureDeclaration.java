package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIProcTable;
import Model.ADT.MyIStack;
import Model.PrgState;
import Model.Type.IntegerType;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ProcedureDeclaration implements IStmt{

    String name;
    List<String> variables;
    IStmt statement;

    public ProcedureDeclaration(String name, List<String> varaibles,IStmt statement){
        this.name = name;
        this.variables = varaibles;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {

        MyIProcTable procTable = state.getProceduresTable();
        procTable.add(name,new Pair<>(variables,statement));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(typeEnvironment.isDefined(name)){
            throw new StmtException("The procedure is already defined!");
        }
        typeEnvironment.add(name,new IntegerType());
        for(String e : variables)
        {
            if(!typeEnvironment.isDefined(e))
                throw new StmtException("The variable isn't defined!");
        }
        statement.typecheck(typeEnvironment);
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "ProcedureDeclaration"+ name + '(' + variables+ ')' + statement ;
    }
}
