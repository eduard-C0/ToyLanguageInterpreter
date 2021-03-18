package Model.Statement;
import Model.PrgState;
import Model.ValueType.*;
import Model.Expression.*;
import Exception.*;
import Model.Type.*;
import Model.ADT.*;

import java.lang.Integer;
import java.util.concurrent.atomic.AtomicInteger;

public class NewHeapStmt implements IStmt{
    String varname;
    Exp expression;

    public NewHeapStmt(String varname, Exp expression){
        this.varname = varname;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack stack = state.getStack();
        MyIDictionary <String,Value> symtable = state.getStackOfSymTable().peek();
        MyIHeap<Value> heap = state.getPrgHeap();

        if(symtable.isDefined(varname)){
            if(symtable.lookup(varname).getType() instanceof  ReferenceType){
                Value value = expression.eval(symtable,heap);
                Value tableValue = symtable.lookup(varname);

                if(value.getType().equals(((ReferenceType)(tableValue.getType())).getInner())){
                    Integer address = heap.allocate(value);
                    symtable.update(varname,new ReferenceValue(address,value.getType()));
                }
                else{
                    throw new StmtException("The value's type isn't reference type.");
                }
            }
            else{
                throw new StmtException("Value's type isn't reference");
            }
        }
        else{
            throw new StmtException("Value isn't declared!");
        }
        state.setExeStack(stack);
        state.setSymTable(symtable);
        state.setHeap(heap);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(!typeEnvironment.isDefined(varname)){
            throw new StmtException("The variable " + varname + " isn't defined.");
        }
        else{
            Type variableType = typeEnvironment.lookup(varname);
            Type expressionType = expression.typecheck(typeEnvironment);
            if(variableType.equals(new ReferenceType(expressionType)))
            {
                return typeEnvironment;
            }
            else {
                throw new StmtException("The right side and left side have different types.");
            }
        }
    }

    @Override
    public String toString() {
        return "New(" + varname + ", "+ expression +')';
    }
}
