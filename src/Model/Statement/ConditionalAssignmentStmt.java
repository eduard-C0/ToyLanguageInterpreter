package Model.Statement;

import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIStack;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.Boolean;
import Model.Type.IntegerType;
import Model.Type.Type;
import Model.ValueType.IntegerValue;

public class ConditionalAssignmentStmt implements IStmt {
    private final String variable;
    private final Exp condition;
    private final Exp trueCondition;
    private final Exp falseCondition;

    public ConditionalAssignmentStmt(String variable,Exp condition,Exp trueCondition,Exp falseCondition)
    {
        this.variable = variable;
        this.condition = condition;
        this.trueCondition = trueCondition;
        this.falseCondition = falseCondition;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        IStmt conditional = new IfStmt(condition,new AssignStmt(variable,trueCondition),new AssignStmt(variable,falseCondition));
        stack.push(conditional);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        Type typecond = condition.typecheck(typeEnvironment);
        Type typeTrueCond = trueCondition.typecheck(typeEnvironment);
        Type typeFalseCond = falseCondition.typecheck(typeEnvironment);
        Type typevar = typeEnvironment.lookup(variable);

        if (!(typecond instanceof Boolean))
            throw new StmtException(typecond.toString());

        if (!(typevar.equals(typeTrueCond)))
            throw new StmtException("CondAssignStmt: invalid types1!");

        if (!(typevar.equals(typeFalseCond)))
            throw new StmtException("CondAssignStmt: invalid types2!");

        return typeEnvironment;
    }

    @Override
    public String toString() {
        return variable + " = " + condition.toString() + " ? " + trueCondition.toString() + " : " + falseCondition.toString();
    }
}
