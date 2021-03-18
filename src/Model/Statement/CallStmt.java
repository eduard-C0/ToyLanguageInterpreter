package Model.Statement;

import Model.ADT.MyDictionary;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIProcTable;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.Value;
import javafx.scene.effect.Bloom;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CallStmt implements IStmt{
    private String name;
    private List<Exp> expressionList;
    public CallStmt(String name , List<Exp> expressionList)
    {
        this.name = name;
        this.expressionList = expressionList;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        MyIProcTable procedureTable = state.getProceduresTable();
        MyIStack<MyIDictionary<String, Value>> stackOfSymTable = state.getStackOfSymTable();
        if(!procedureTable.isDefined(name))
        {
            throw new MyException("The procedure doesn't exist in the procedure table.");
        }
        Pair<List<String>, IStmt> values = procedureTable.lookup(name);
        List<String> variables = values.getKey();
        IStmt stmt = values.getValue();
        MyIDictionary<String,Value> symbolTable = stackOfSymTable.peek();
        MyIDictionary<String,Value> newSymbolTable = new MyDictionary<>();
        int i = 0;
        for(Exp e : expressionList)
        {
            Value value = e.eval(symbolTable,state.getPrgHeap());
            newSymbolTable.add(variables.get(i),value);
            i = i + 1;
        }

        state.getStackOfSymTable().push(newSymbolTable);
        stack.push(new ReturnStmt());
        stack.push(stmt);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Call(" + name + ')' + '{' + expressionList + '}';
    }
}
