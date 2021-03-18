package Model.Expression;
import Exception.ExpressionException;
import Model.ADT.MyHeap;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.IntegerType;
import Model.Type.Type;
import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

public class ArithmeticExpression implements Exp{
    Exp expression1;
    Exp expression2;

    int operation;

    public ArithmeticExpression(Exp lhs, char op, Exp rhs){
        this.expression1 = lhs;
        this.expression2 = rhs;

        if(op == '+')
            this.operation = 1;
        if(op == '-')
            this.operation = 2;
        if(op == '*')
            this.operation = 3;
        if(op == '/')
            this.operation = 4;
    }


    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value> heap) throws ExpressionException {
        Value val1;
        Value val2;
        val1 = expression1.eval(table,heap);
        if(val1.getType().equals(new IntegerType())){
            val2 = expression2.eval(table,heap);
            if(val2.getType().equals(new IntegerType())){
                IntegerValue i1 = (IntegerValue)val1;
                IntegerValue i2 = (IntegerValue)val2;
                int n1 = i1.getValue();
                int n2 = i2.getValue();
                if(operation == 1)
                {
                    return new IntegerValue(n1+n2);
                }
                else if(operation == 2)
                {
                    return new IntegerValue(n1-n2);
                }
                else if(operation == 3)
                {
                    return new IntegerValue(n1*n2);
                }
                else if(operation == 4)
                {
                    if(n2 == 0)
                    {
                        throw new ExpressionException("Error! Division by zero!");
                    }
                    else{
                        return new IntegerValue(n1/n2);
                    }

                }
            }
            else{
                throw new ExpressionException("The right hand side operand isn't an integer!");
            }
        }
        else {
            throw new ExpressionException("The left hand side operand isn't an integer!");
        }
        return new IntegerValue(0);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type1 = expression1.typecheck(typeEnv);
        Type type2 = expression2.typecheck(typeEnv);

        if(type1.equals(new IntegerType()))
        {
            if(type2.equals(new IntegerType()))
            {
                return new IntegerType();
            }
            else {
                throw new ExpressionException("The second operand is not an integer");
            }
        }
        else
        {
            throw new ExpressionException("The first operand is not an integer");
        }
    }

    @Override
    public String toString() {
        switch (operation) {
            case 1:
                return expression1.toString() + "+" + expression2.toString();
            case 2:
                return expression1.toString() + "-" + expression2.toString();
            case 3:
                return expression1.toString() + "*" + expression2.toString();
            case 4:
                return expression1.toString() + '/' + expression2.toString();
            default:
                return "";
        }
    }
}
