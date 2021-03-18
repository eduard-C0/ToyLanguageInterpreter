package Model.Type;

import Model.ValueType.ReferenceValue;
import Model.ValueType.Value;

import java.util.Objects;

public class ReferenceType implements Type{
    Type inner;

    public ReferenceType(){
        inner = new IntegerType();
    }

    public ReferenceType(Type inner){
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return "Reference(" + inner.toString() + ") ";
    }

    @Override
    public Value defaultValue() {
        return new ReferenceValue(0,inner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceType that = (ReferenceType) o;
        return Objects.equals(inner, that.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inner);
    }
}
