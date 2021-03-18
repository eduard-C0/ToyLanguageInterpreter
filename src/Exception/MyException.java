package Exception;

public class MyException extends RuntimeException {

    public MyException(String message)
    {
        super(message);
    }

    @Override
    public String toString() {
        return "Error: " + super.getMessage();
    }
}
