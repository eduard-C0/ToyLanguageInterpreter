package Model.ValueType;

import Model.Type.StringType;
import Model.Type.Type;

import java.util.Objects;

public class StringValue implements Value{
    private final String str;

    public StringValue(String str){
        this.str = str;
    }

    public StringValue(){
        str = "";
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    public String getValue() {
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str);
    }

    @Override
    public String toString() {
        return str;

    }
}
