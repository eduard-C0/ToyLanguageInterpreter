package Model.Type;

import Model.ValueType.StringValue;
import Model.ValueType.Value;

public class StringType implements Type{
    @Override
    public Value defaultValue() {
        return new StringValue("");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return true;
    }

    @Override
    public String toString() {
        return "string";
    }
}
