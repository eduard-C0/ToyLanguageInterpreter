package View;

import Service.Controller;
import Exception.*;
import java.io.IOException;

public class RunExample extends Command {
    private Controller controller;

    public RunExample(String key, String description, Controller controller){
        super(key,description);
        this.controller = controller;
    }

    @Override
    public void execute() throws IOException {
        try{
            controller.allSteps();
        }
        catch (MyException | IOException err)
        {
            System.out.println(err.toString());
        }
    }
}
