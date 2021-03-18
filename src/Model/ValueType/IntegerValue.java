package Model.ValueType;
import Model.Type.IntegerType;
import Model.Type.Type;

import java.util.Objects;

public class IntegerValue implements Value{
    int v;

    public IntegerValue(int v){
        this.v = v;
    }
    public IntegerValue(){
        this.v = 0;
    }

    public int getValue()
    {
        return v;
    }

    @Override
    public String toString() {
        return java.lang.Integer.toString(v);
    }

    @Override
    public Type getType() {
        return new IntegerType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerValue that = (IntegerValue) o;
        return v == that.v;
    }

    @Override
    public int hashCode() {
        return Objects.hash(v);
    }
}
