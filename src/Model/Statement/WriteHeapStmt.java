package Model.Statement;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Expression.Exp;
import Model.PrgState;
import Model.Type.ReferenceType;
import Model.Type.Type;
import Model.ValueType.*;
import Exception.*;
public class WriteHeapStmt implements IStmt{
    private String varName;
    private Exp expression;

    public WriteHeapStmt(String varName, Exp expression){
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIDictionary<String,Value> symtable = state.getStackOfSymTable().peek();
        MyIHeap<Value> heap = state.getPrgHeap();

        if(symtable.isDefined(varName)){
            if (symtable.lookup(varName).getType() instanceof ReferenceType){
                ReferenceValue referenceValue = (ReferenceValue) symtable.lookup(varName);
                if(heap.exists(referenceValue.getAddress())){
                    Value value = expression.eval(symtable,heap);
                    if(symtable.lookup(varName).getType().equals(new ReferenceType(value.getType()))){
                        int address = referenceValue.getAddress();
                        heap.put(address,value);
                    }
                    else{
                        throw new StmtException("The variable has a different type than the expression!");
                    }
                }
                else{
                    throw new StmtException("The address is not in the heap.");
                }
            }
            else{
                throw new StmtException(varName + " isn't a reference variable");
            }
        }
        else{
            throw new StmtException(varName + " isn't defined");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        if(typeEnvironment.isDefined(varName)){
            Type variableType = typeEnvironment.lookup(varName);
            Type expressionType = expression.typecheck(typeEnvironment);
            if(!(variableType instanceof ReferenceType)){
                throw new StmtException("The file name is not a string.");
            }
            if(!variableType.equals(new ReferenceType(expressionType))){
                throw new StmtException("The right side has other type than the referenced one");
            }
            return typeEnvironment;
        }
        else {
            throw new StmtException("The variable isn't defined!");
        }
    }

    @Override
    public String toString() {
        return "WriteHeap( " + varName + "," + expression + ')';
    }

}
