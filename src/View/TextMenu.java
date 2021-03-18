package View;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class TextMenu {
    private HashMap<String,Command>commands;

    public TextMenu(){
        this.commands = new HashMap<String, Command>();
    }

    public void addCommand(Command command){
        commands.put(command.getKey(),command);
    }

    private void printMenu(){
        for (Command command: commands.values()){
            String line = String.format("%4s: %s",command.getKey(),command.getDescription());
            System.out.println(line);
        }
    }

    public void show() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();
            System.out.print("Input the option: ");
            String key = scanner.nextLine();
            Command command = commands.get(key);
            if(command == null){
                System.out.println("Invalid option!");
            }
            else {
                command.execute();
            }
        }
    }
}
