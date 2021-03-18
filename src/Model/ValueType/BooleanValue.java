package Model.ValueType;
import Model.Type.Boolean;
import Model.Type.Type;

import java.util.Objects;

public class BooleanValue implements Value{
    boolean element;

    public BooleanValue(boolean e){
        this.element = e;
    }
    public BooleanValue(){
        element = false;
    }
    @Override
    public Type getType() {
        return new Boolean();
    }


    public boolean getValue(){
        return element;
    }

    @Override
    public String toString() {
        return  element ? "true" : "false";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BooleanValue)) return false;
        BooleanValue that = (BooleanValue) o;
        return that.element == element;
    }
}
