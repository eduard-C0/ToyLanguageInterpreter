package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Boolean;
import Model.Type.Type;
import Model.ValueType.BooleanValue;
import Model.ValueType.Value;
import Exception.*;
public class NotExpression implements Exp{
    private final Exp expression;

    public NotExpression(Exp expression)
    {
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value> heap) throws ExpressionException {
        Value value1 = expression.eval(table,heap);
        if(value1.getType().equals(new Boolean())) {
            BooleanValue v1 = (BooleanValue) value1;
            if(v1.equals(new BooleanValue(true)))
            {
                return new BooleanValue(false);
            }
            else
                return new BooleanValue(true);
        }
        else{
            throw new ExpressionException("The expression isn't a boolean !");
        }
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        return null;
    }

    @Override
    public String toString() {
        return "!" + expression ;
    }
}
