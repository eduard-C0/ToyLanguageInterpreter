package Model.ValueType;
import Model.Type.IntegerType;
import Model.Type.ReferenceType;
import Model.Type.Type;
public class ReferenceValue implements Value{
    int address;
    Type locationType;

    public ReferenceValue(int address, Type locationType){
        this.address = address;
        this.locationType = locationType;
    }

    public ReferenceValue() {

    }

    public int getAddress() {
        return address;
    }

    @Override
    public Type getType() {
        return new ReferenceType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " +locationType + ")";
    }
}
