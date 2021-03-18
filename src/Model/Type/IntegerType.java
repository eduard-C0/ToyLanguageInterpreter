package Model.Type;

import Model.ValueType.IntegerValue;
import Model.ValueType.Value;

public class IntegerType implements Type {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntegerType;
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Value defaultValue() {
        return new IntegerValue(0);
    }
}
