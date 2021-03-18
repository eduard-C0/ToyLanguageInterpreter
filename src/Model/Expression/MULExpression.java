package Model.Expression;

import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.IntegerType;
import Model.Type.Type;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;
import Exception.*;

public class MULExpression implements Exp{
    Exp e1, e2;

    public MULExpression(Exp lhs, Exp rhs) {
        this.e1 = lhs;
        this.e2 = rhs;

    }
    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value> heap) throws ExpressionException {
        Value value1 = e1.eval(table, heap);
        Value value2 = e2.eval(table, heap);

        IntegerValue intValue1, intValue2;
        intValue1 = (IntegerValue)value1;
        intValue2 = (IntegerValue)value2;

        int a = intValue1.getValue();
        int b = intValue2.getValue();

        return new IntegerValue((a*b)-(a+b));
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type1, type2;
        type1 = e1.typecheck(typeEnv);
        type2 = e2.typecheck(typeEnv);

        if (!type1.equals(new IntegerType()))
            throw new MyException("first operand not an integer!");

        if(!type2.equals(new IntegerType()))
            throw new MyException("second operand not an integer!");

        return new IntegerType();
    }

    @Override
    public String toString() {
        return "MUL("+e1+","+e2+")";

    }
}

