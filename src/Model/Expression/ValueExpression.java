package Model.Expression;
import Exception.*;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.ValueType.Value;

public class ValueExpression implements Exp{
    Value expression;

    public ValueExpression(Value expression){
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value>heap) throws ExpressionException {
        return expression;
    }

    @Override
    public String toString() {
        return expression.toString();
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        return expression.getType();
    }
}
