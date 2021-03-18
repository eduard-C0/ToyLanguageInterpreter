package Model.Statement;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Boolean;
import Model.Type.Type;
import Exception.*;
import Model.ValueType.Value;

public class SwitchStmt implements IStmt{
    private final Exp condition;
    private final Exp case1;
    private final Exp case2;
    private final IStmt statement1;
    private final IStmt statement2;
    private final IStmt statement3;

    public SwitchStmt (Exp condition, Exp case1,IStmt statement1, Exp case2,IStmt statement2,IStmt statement3){
        this.condition = condition;
        this.case1 = case1;
        this.case2 = case2;
        this.statement1 = statement1;
        this.statement2 = statement2;
        this.statement3 = statement3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> executionStack = state.getStack();
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        MyIHeap<Value> heapTable = state.getPrgHeap();

        Value exp = condition.eval(symbolTable, heapTable);
        Value exp1 = case1.eval(symbolTable, heapTable);
        Value exp2 = case2.eval(symbolTable, heapTable);
        if(exp.equals(exp1))
            executionStack.push(statement1);
        else if(exp.equals(exp2))
            executionStack.push(statement2);
        else
            executionStack.push(statement3);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        return typeEnvironment;
    }

    @Override
    public String toString(){
        return "switch( " + condition.toString() + ") \n(case( " + case1.toString() + " ) " + statement1.toString() + ")\n(case( " + case2.toString() + " ) " + statement2.toString() + ")\n(default " + statement3.toString() + " )";
    }
}
