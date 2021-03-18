package View;
import Model.ADT.*;
import Model.Expression.ArithmeticExpression;
import Model.Expression.ValueExpression;
import Model.Expression.VarExpression;
import Model.PrgState;
import Model.Statement.*;
import Model.Type.Boolean;
import Model.Type.IntegerType;
import Model.Type.StringType;
import Model.ValueType.BooleanValue;
import Model.ValueType.IntegerValue;
import Model.ValueType.StringValue;
import Model.ValueType.Value;
import Service.*;
import Repository.*;

import java.io.BufferedReader;
import java.io.IOException;

public class View {
    public void View() throws IOException {
        MyIStack<IStmt> stack1 = new MyStack<>();
        MyIStack<IStmt> stack2 = new MyStack<>();
        MyIStack<IStmt> stack3 = new MyStack<>();
        MyIStack<IStmt> stack4 = new MyStack<>();

        IStmt ex1 = new CompStmt(new VarDeclaration("x",new IntegerType()),new CompStmt(new AssignStmt("x",new ValueExpression(new IntegerValue(17))),new PrintStmt(new VarExpression("x"))));

        PrgState prg1 = new PrgState(stack1, new MyDictionary<String, Value>(),new MyList<Value>(),new MyDictionary<StringValue, BufferedReader>(),new MyHeap<>(),ex1);
        MyIRepo repo1 = new Repository(prg1,"log1.txt");
        Controller controller1 = new Controller(repo1);

        IStmt ex2 = new CompStmt(
                new VarDeclaration("s" , new Boolean()),
                new CompStmt(new VarDeclaration("x", new IntegerType()),
                        new CompStmt(
                                new AssignStmt("s", new ValueExpression(new BooleanValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExpression("s"),
                                                new AssignStmt("x", new ValueExpression(new IntegerValue(2))),
                                                new AssignStmt("x", new ValueExpression(new IntegerValue(20)))
                                        ),
                                        new PrintStmt(new VarExpression("x"))
                                )
                        )
                )
        );
        PrgState prg2 = new PrgState(stack2, new MyDictionary<String, Value>(),new MyList<Value>(),new MyDictionary<StringValue, BufferedReader>(),new MyHeap<>(),ex2);
        MyIRepo repo2 = new Repository(prg2,"log2.txt");
        Controller controller2 = new Controller(repo2);

        IStmt ex3 = new CompStmt(
                new VarDeclaration("x" , new IntegerType()),
                new CompStmt(new AssignStmt("x", new ArithmeticExpression(
                        new ValueExpression(new IntegerValue(3)),'+',
                        new ArithmeticExpression(
                                new ValueExpression(new IntegerValue(5)),'*', new ValueExpression(new IntegerValue(7))
                        )

                )
                ),
                        new PrintStmt(new VarExpression("x"))
                )
        );
        PrgState prg3 = new PrgState(stack3, new MyDictionary<String, Value>(),new MyList<Value>(),new MyDictionary<StringValue, BufferedReader>(),new MyHeap<>(),ex3);
        MyIRepo repo3 = new Repository(prg3,"log3.txt");
        Controller controller3 = new Controller(repo3);

        IStmt ex4= new CompStmt(
                new VarDeclaration("fileName", new StringType()),
                new CompStmt(new AssignStmt("fileName", new ValueExpression(new StringValue("test.txt"))),
                        new CompStmt(new OpenReadFileStmt(new VarExpression("fileName")),
                                new CompStmt(new VarDeclaration("x", new IntegerType()),
                                        new CompStmt(new ReadFileStmt(new VarExpression("fileName"), "x"),
                                                new CompStmt(new PrintStmt(new VarExpression("x")),
                                                        new CompStmt(new ReadFileStmt(new VarExpression("fileName"), "x"),
                                                                new CompStmt(new PrintStmt(new VarExpression("x")),
                                                                        new CloseReadFileStmt(new VarExpression("fileName"))))))))));

        PrgState prg4 = new PrgState(stack4, new MyDictionary<String, Value>(),new MyList<Value>(),new MyDictionary<StringValue, BufferedReader>(),new MyHeap<>(),ex4);
        MyIRepo repo4 = new Repository(prg4,"log4.txt");
        Controller controller4 = new Controller(repo4);
        TextMenu menu = new TextMenu();

        repo1.addState(prg1);
        repo2.addState(prg2);
        repo3.addState(prg3);
        repo4.addState(prg4);

        menu.addCommand(new ExitCommand("0","exit"));
        menu.addCommand(new RunExample("1",ex1.toString(),controller1));
        menu.addCommand(new RunExample("2",ex2.toString(),controller2));
        menu.addCommand(new RunExample("3",ex3.toString(),controller3));
        menu.addCommand(new RunExample("4",ex4.toString(),controller4));
        menu.show();
    }

}
