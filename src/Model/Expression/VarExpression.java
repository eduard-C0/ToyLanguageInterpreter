package Model.Expression;
import Exception.ExpressionException;
import Model.ADT.MyIDictionary;
import Model.ADT.MyIHeap;
import Model.Type.Type;
import Model.ValueType.Value;

public class VarExpression implements Exp{
    String id;

    public VarExpression(String str){
        id = str;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> table, MyIHeap<Value>heap) throws ExpressionException {
        return table.lookup(id);
    }


    @Override
    public String toString() {
        return id;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws ExpressionException {
        if(typeEnv.isDefined(id)){
            return typeEnv.lookup(id);
        }
        else{
            throw new ExpressionException("The variable "+this.toString()+" is not defined.");
        }
    }
}
