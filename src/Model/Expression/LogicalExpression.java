package Model.Expression;
import Exception.*;
import Model.ADT.MyHeap;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Boolean;
import Model.Type.Type;
import Model.ValueType.BooleanValue;
import Model.ValueType.Value;


public class LogicalExpression implements Exp{
    Exp expression1;
    Exp expression2;
    int operation;

    public LogicalExpression(Exp lhs, int operation, Exp rhs)
    {
        expression1 = lhs;
        this.operation = operation;
        expression2 = rhs;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value> heap) throws ExpressionException {
        Value val1;
        Value val2;
        val1 = expression1.eval(table,heap);
        if(val1.getType().equals(new Boolean())){
            val2 = expression2.eval(table,heap);
            if(val2.getType().equals(new Boolean())){
                BooleanValue i1 = (BooleanValue) val1;
                BooleanValue i2 = (BooleanValue) val2;

                boolean element1 = i1.getValue();
                boolean element2 = i2.getValue();

                if(operation == 1){
                    return new BooleanValue(element1 && element2);
                }
                else if(operation == 2){
                    return new BooleanValue(element1 || element2);
                }
            }
            else{
                throw new ExpressionException("The right hand side operand isn't a boolean value!");
            }
        }
        else {
            throw new ExpressionException("The left hand side operand isn't a boolean value!");
        }
        return new BooleanValue(false);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type1 = expression1.typecheck(typeEnv);
        Type type2 = expression2.typecheck(typeEnv);

        if(type1.equals(new Boolean())){
            if (type2.equals(new Boolean()))
            {
                return new Boolean();
            }
            else{
                throw new ExpressionException("The second operand is not a boolean");
            }
        }
        else{
            throw new ExpressionException("The first operand is not a boolean.");
        }
    }
}
