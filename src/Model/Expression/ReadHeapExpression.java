package Model.Expression;
import Model.ADT.*;
import Model.Type.ReferenceType;
import Model.Type.Type;
import Model.ValueType.*;
import Exception.*;

public class ReadHeapExpression implements Exp{
    private Exp expression;

    public ReadHeapExpression(Exp expression){
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value> heap) throws ExpressionException {
        Value value = expression.eval(table,heap);
        if(value instanceof ReferenceValue){
            ReferenceValue referenceValue = (ReferenceValue) value;
            if(heap.exists(referenceValue.getAddress())){
                return heap.get(referenceValue.getAddress());
            }
            else{
                throw new ExpressionException("Not allocated on the heap!");
            }
        }
        else{
            throw new ExpressionException("The expression couldn't be evaluated to a RefereceValue.");
        }
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type = expression.typecheck(typeEnv);
        if(type instanceof ReferenceType)
        {
            ReferenceType referenceType = (ReferenceType) type;
            return  referenceType.getInner();
        }
        else{
            throw new ExpressionException("The expression is not of a reference typ.");
        }
    }

    @Override
    public String toString() {
        return "ReadHeap(" + expression.toString() +')';
    }

}
