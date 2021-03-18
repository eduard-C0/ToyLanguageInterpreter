package Model.Expression;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Boolean;
import Model.Type.Type;
import Model.ValueType.*;
import Model.Type.IntegerType;
import Exception.*;

import javax.lang.model.element.TypeElement;

public class RelationalExpression implements Exp{
    private Exp expression1;
    private Exp expression2;
    private int op;

    public RelationalExpression(Exp expression1, int op, Exp expression2){
        this.expression1 = expression1;
        this.op = op;
        this.expression2 = expression2;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symtable, MyIHeap<Value>heap) throws ExpressionException {
        Value value1,value2;

        value1 = expression1.eval(symtable,heap);
        value2 = expression2.eval(symtable,heap);

        if(value1.getType().equals(new IntegerType()) && value2.getType().equals(new IntegerType())){
            IntegerValue intValue1, intValue2;
            intValue1 = (IntegerValue)value1;
            intValue2 = (IntegerValue)value2;

            int a = intValue1.getValue();
            int b = intValue2.getValue();

            switch (op){
                case 1:
                    return new BooleanValue(a < b);
                case 2:
                    return new BooleanValue(a <= b);
                case 3:
                    return new BooleanValue(a == b);
                case 4:
                    return new BooleanValue(a != b);
                case 5:
                    return new BooleanValue(a > b);
                case 6:
                    return new BooleanValue(a >= b);
            }
        }
        else{
            throw new ExpressionException("One operand is not an integer.");
        }
        return new BooleanValue(false);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type1 = expression1.typecheck(typeEnv);
        Type type2 = expression2.typecheck(typeEnv);

        if(type1.equals(new IntegerType()))
        {
            if(type2.equals(new IntegerType())){
                return new Boolean();
            }
            else{
                throw new ExpressionException("The second operand is not an integer.");
            }
        }
        else{
            throw new ExpressionException("The first operand is not an intege.");
        }
    }

    public String toString() {
        String str = switch (op) {
            case 1 -> "<";
            case 2 -> "<=";
            case 3 -> "==";
            case 4 -> "!=";
            case 5 -> ">";
            default -> ">=";
        };
        return expression1 + str + expression2;
    }
}
