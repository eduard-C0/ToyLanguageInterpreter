package Exception;

public class MyIException extends RuntimeException{
    public MyIException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
