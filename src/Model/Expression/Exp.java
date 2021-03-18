package Model.Expression;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.ValueType.Value;

public interface Exp {
    public Value eval(MyIDictionary<String,Value> table, MyIHeap<Value>heap) throws ExpressionException;
    Type typecheck(MyIDictionary<String,Type> typeEnv) throws ExpressionException;
}
