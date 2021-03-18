package Model.Type;


import Model.ValueType.BooleanValue;
import Model.ValueType.Value;

public class Boolean implements Type {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Boolean;
    }

    @Override
    public String toString() {
        return "Boolean";
    }

    @Override
    public Value defaultValue() {
        return new BooleanValue(false);
    }
}
