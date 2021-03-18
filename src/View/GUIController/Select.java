package View.GUIController;
import Exception.*;
import Model.ADT.*;
import Model.Expression.*;
import Model.PrgState;
import Model.Statement.*;
import Model.Type.Boolean;
import Model.Type.IntegerType;
import Model.Type.ReferenceType;
import Model.Type.StringType;
import Model.ValueType.BooleanValue;
import Model.ValueType.IntegerValue;
import Model.ValueType.StringValue;
import Model.ValueType.Value;
import Repository.MyIRepo;
import Repository.Repository;
import Service.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Select {

    @FXML
    private ListView<String> programsListView;
    private Map<String, IStmt> TextOfPrograms;
    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    @FXML
    public void initialize(){
        this.setupPrograms();

        ObservableList<String> ListOfPrograms = FXCollections.observableArrayList();

        AtomicInteger currentIndex = new AtomicInteger(1);
        this.TextOfPrograms.forEach((key, value) -> ListOfPrograms.add((currentIndex.getAndIncrement()) + ". " + key));
        //ObservableList<String> smth = FXCollections.observableArrayList();
        //smth.add("Ana");
        //smth.add("are");
        //smth.add("mere");
        this.programsListView.setItems(ListOfPrograms);
        this.programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void chooseProgram(MouseEvent mouseEvent){
        if(mouseEvent.getClickCount() == 2){
            try
            {
                String key = this.programsListView.getSelectionModel().getSelectedItem();
                IStmt statement = this.TextOfPrograms.get(key.substring(key.indexOf(' ') + 1));
                System.out.println(statement);
                try{
                    //statement.typecheck(new MyDictionary<>());
                }
                catch (RuntimeException exception){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Type check error");
                    alert.setContentText(exception.getMessage());
                    alert.showAndWait();
                    return;
                }
                MyIStack<IStmt> executionStack = new MyStack<>();
                //System.out.println(executionStack);
                //System.out.println();
                MyIDictionary<String, Value> symbolsTable = new MyDictionary<>();
                MyIList<Value> out = new MyList<>();
                MyIDictionary<StringValue, BufferedReader> fileTable = new MyDictionary<>();
                MyHeap heap = new MyHeap();
                MyILockTable lockTable = new MyLockTable();
                MyISemaphore semaphore = new MySemaphore();
                MyILockTable countDownLatch = new MyLockTable();
                MyIProcTable procTable = new MyProcTable();
                MyIStack<MyIDictionary<String,Value>>stackOfSymbolTables = new MyStack<>();
                stackOfSymbolTables.push(new MyDictionary<>());
                PrgState newProgram = new PrgState(executionStack, stackOfSymbolTables, out, fileTable, heap, statement,lockTable,semaphore,countDownLatch,procTable);

                MyIRepo repository = new Repository("src/log" + (this.programsListView.getSelectionModel().getSelectedIndices().get(0) + 1) + ".txt");
                Controller statmentController = new Controller(repository);
                statmentController.addState(newProgram);

                Parent root;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../FXML/RunProgram.fxml"));
                root = loader.load();

                Run controller = loader.getController();

                controller.setParentStage(this.parentStage);
                controller.setSelectProgramsScene(this.parentStage.getScene());
                controller.setController(statmentController);

                statmentController.addObserver(controller);
                statmentController.notyfiObservers();

                Scene scene = new Scene(root, 1024, 720);

                this.parentStage.setScene(scene);
                this.parentStage.show();

            }
            catch (Exception err)
            {
                err.printStackTrace();
                System.exit(1);
            }
        }
    }
    void setupPrograms(){
        this.TextOfPrograms = new LinkedHashMap<>();

        IStmt ex1 = new CompStmt(new VarDeclaration("x", new IntegerType()), new CompStmt(new AssignStmt("x", new ValueExpression(new IntegerValue(17))), new PrintStmt(new VarExpression("x"))));

        IStmt ex2 = new CompStmt(
                new VarDeclaration("s", new Boolean()),
                new CompStmt(new VarDeclaration("x", new IntegerType()),
                        new CompStmt(
                                new AssignStmt("s", new ValueExpression(new BooleanValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExpression("s"),
                                                new AssignStmt("x", new ValueExpression(new StringValue("a"))),
                                                new AssignStmt("x", new ValueExpression(new IntegerValue(20)))
                                        ),
                                        new PrintStmt(new VarExpression("x"))
                                )
                        )
                )
        );

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
        IStmt ex4= new CompStmt(
                new VarDeclaration("fileName", new StringType()),
                new CompStmt(new AssignStmt("fileName", new ValueExpression(new StringValue("C:\\Users\\User\\Desktop\\An2\\Semestrul3\\hello\\src\\test.txt"))),
                        new CompStmt(new OpenReadFileStmt(new VarExpression("fileName")),
                                new CompStmt(new VarDeclaration("x", new IntegerType()),
                                        new CompStmt(new ReadFileStmt(new VarExpression("fileName"), "x"),
                                                new CompStmt(new PrintStmt(new VarExpression("x")),
                                                        new CompStmt(new ReadFileStmt(new VarExpression("fileName"), "x"),
                                                                new CompStmt(new PrintStmt(new VarExpression("x")),
                                                                        new CloseReadFileStmt(new VarExpression("fileName"))))))))));


        IStmt ex5 = new CompStmt(new VarDeclaration("a", new IntegerType()),
                new CompStmt(new AssignStmt("a", new ValueExpression(new IntegerValue(6))),
                        new CompStmt(new VarDeclaration("b", new IntegerType()),
                                new CompStmt(new AssignStmt("b", new ValueExpression(new IntegerValue(7))),
                                        new PrintStmt(new RelationalExpression(new VarExpression("a"),2,new VarExpression("b")))))));


        IStmt ex6 = new CompStmt(new VarDeclaration("v", new ReferenceType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExpression(new IntegerValue(20))),
                        new CompStmt(new VarDeclaration("a", new ReferenceType(new ReferenceType(new IntegerType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExpression("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v"))),
                                                new PrintStmt(new ArithmeticExpression(new ReadHeapExpression(new ReadHeapExpression(new VarExpression("a"))),
                                                        '+',new ValueExpression(new IntegerValue(10)))))))));

        IStmt ex7 = new CompStmt(new VarDeclaration("x", new IntegerType()),
                new CompStmt(new AssignStmt("x", new ValueExpression(new IntegerValue(10))),
                        new CompStmt(new WhileStmt(new RelationalExpression(new VarExpression("x"),5, new ValueExpression(new IntegerValue(0))), new CompStmt(new PrintStmt(new VarExpression("x")), new AssignStmt("x", new ArithmeticExpression(new VarExpression("x"),'-', new ValueExpression(new IntegerValue(1)))))),
                                new PrintStmt(new VarExpression("x")))));

        IStmt ex8 = new CompStmt(new VarDeclaration("v", new ReferenceType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExpression(new IntegerValue(20))),
                        new CompStmt(new VarDeclaration("a", new ReferenceType(new IntegerType())),
                                new CompStmt(new NewHeapStmt("a", new ValueExpression(new IntegerValue(25))),
                                        new CompStmt(new NewHeapStmt("v", new ValueExpression(new IntegerValue(30))),
                                                new PrintStmt(new ReadHeapExpression((new VarExpression("v")))))))));


        IStmt ex9 = new CompStmt(new VarDeclaration("v", new ReferenceType(new IntegerType())),
                new CompStmt(new NewHeapStmt("v", new ValueExpression(new IntegerValue(20))),
                        new CompStmt(new VarDeclaration("a", new ReferenceType(new ReferenceType(new IntegerType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExpression("v")),
                                        new CompStmt(new NewHeapStmt("v", new ValueExpression(new IntegerValue(30))),
                                                new PrintStmt(new ReadHeapExpression(new ReadHeapExpression(new VarExpression("a")))))))));


        IStmt ex10 = new CompStmt(new VarDeclaration("v", new IntegerType()),
                new CompStmt(new VarDeclaration("a", new ReferenceType(new IntegerType())),
                        new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(10))),
                                new CompStmt(new NewHeapStmt("a", new ValueExpression(new IntegerValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("a", new ValueExpression(new IntegerValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExpression("v")),
                                                                new PrintStmt(new ReadHeapExpression(new VarExpression("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExpression("v")),
                                                        new PrintStmt(new ReadHeapExpression(new VarExpression("a")))))))));

        IStmt ex11 =  new CompStmt( new VarDeclaration("a",new IntegerType()),
                new CompStmt(new VarDeclaration("b",new IntegerType()),
                        new CompStmt(new AssignStmt("a", new ArithmeticExpression(new ValueExpression(new IntegerValue(2)),'+',new
                                ArithmeticExpression(new ValueExpression(new StringValue("a")),'+', new ValueExpression(new IntegerValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithmeticExpression(new VarExpression("a"),'+', new
                                        ValueExpression(new IntegerValue(1)))), new PrintStmt(new VarExpression("b"))))));

        IStmt ex12 =new CompStmt( new VarDeclaration("v",new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(10))),
                new CompStmt(new ForkStmt(new CompStmt(new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'-',new ValueExpression(new IntegerValue(1)))),
                        new CompStmt(new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'-', new ValueExpression(new IntegerValue(1)))),
                                new PrintStmt(new VarExpression("v"))))),
        new CompStmt(new SleepStmt(10), new PrintStmt(new ArithmeticExpression(new VarExpression("v"), '*',new ValueExpression(new IntegerValue(10))))))));

        IStmt ex13 =new CompStmt( new VarDeclaration("v",new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(20))),
                new CompStmt(new ForStmt("v", new ValueExpression(new IntegerValue(0)), new ValueExpression(new IntegerValue(3)), new ArithmeticExpression(new VarExpression("v"),'+', new ValueExpression(new IntegerValue(1))),
                        new ForkStmt(new CompStmt(new PrintStmt(new VarExpression("v")),
                                new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'+', new ValueExpression(new IntegerValue(1))))))),
                        new PrintStmt(new ArithmeticExpression(new VarExpression("v"),'*', new ValueExpression(new IntegerValue(10)))))));

        IStmt ex14 =new CompStmt( new VarDeclaration("a",new IntegerType()),
                new CompStmt( new VarDeclaration("b",new IntegerType()),
                new CompStmt( new VarDeclaration("c",new IntegerType()),
                new CompStmt(new AssignStmt("a", new ValueExpression(new IntegerValue(1))),
                new CompStmt(new AssignStmt("b",  new ValueExpression(new IntegerValue(2))),
                        new CompStmt(new AssignStmt("c",  new ValueExpression(new IntegerValue(5))),
                                new CompStmt(new SwitchStmt(new ArithmeticExpression(new VarExpression("a"),'*' ,new ValueExpression(new IntegerValue(10))),
                                        new ArithmeticExpression(new VarExpression("b"),'*', new VarExpression("c")), new CompStmt(new PrintStmt(new VarExpression("a")), new PrintStmt(new VarExpression("b"))),
                                        new ValueExpression(new IntegerValue(10)), new CompStmt(new PrintStmt( new ValueExpression(new IntegerValue(100))), new PrintStmt( new ValueExpression(new IntegerValue(300)))),
                                        new PrintStmt( new ValueExpression(new IntegerValue(300)))),
                                        new PrintStmt( new ValueExpression(new IntegerValue(300))))))))));


        IStmt ex15 =new CompStmt( new VarDeclaration("v",new IntegerType()),
                new CompStmt( new VarDeclaration("x",new IntegerType()),
                new CompStmt( new VarDeclaration("y",new IntegerType()),
                new CompStmt( new VarDeclaration("z",new IntegerType()),
                new CompStmt( new VarDeclaration("w",new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(0))),
                        new CompStmt(new RepeatUntilStmt(new CompStmt(new ForkStmt(new CompStmt(new PrintStmt(new VarExpression("v")), new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'-' ,new ValueExpression(new IntegerValue(1)))))), new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'+' ,new ValueExpression(new IntegerValue(1))))), new RelationalExpression(new VarExpression("v"),3 ,new ValueExpression(new IntegerValue(3)))),
                        new CompStmt(new AssignStmt("x", new ValueExpression(new IntegerValue(1))),
                                new CompStmt(new AssignStmt("y",new ValueExpression(new IntegerValue(2))),
                                        new CompStmt(new AssignStmt("z", new ValueExpression(new IntegerValue(3))),
                                                new CompStmt(new AssignStmt("w", new ValueExpression(new IntegerValue(4))),
                                                        new PrintStmt(new ArithmeticExpression(new VarExpression("v"),'*' ,new ValueExpression(new IntegerValue(10)))))))))))))));

        IStmt ex16 =new CompStmt( new VarDeclaration("a",new IntegerType()),
                new CompStmt( new VarDeclaration("b",new IntegerType()),
                new CompStmt( new VarDeclaration("c",new IntegerType()),
                new CompStmt(
                new AssignStmt("a", new ValueExpression(new IntegerValue(1))), new CompStmt(
                new AssignStmt("b", new ValueExpression(new IntegerValue(2))), new CompStmt(
                new ConditionalAssignmentStmt("c", new VarExpression("a"), new ValueExpression(new IntegerValue(100)), new ValueExpression(new IntegerValue(200))), new CompStmt(
                new PrintStmt(new VarExpression("c")), new CompStmt(
                new ConditionalAssignmentStmt("c", new ArithmeticExpression(new VarExpression("b"), '-',new ValueExpression(new IntegerValue(2))), new ValueExpression(new IntegerValue(100)), new ValueExpression(new IntegerValue(200))),
                new PrintStmt(new VarExpression("c")))
        )))))));
        IStmt ex17 = new CompStmt(new VarDeclaration("v",new Boolean()),
                new CompStmt(new ConditionalAssignmentStmt("v",new ValueExpression(new BooleanValue(true)),new ValueExpression(new BooleanValue(false)),new ValueExpression(new BooleanValue(true))),
                        new PrintStmt(new VarExpression("v"))));


        IStmt ex18 =new CompStmt( new VarDeclaration("v1",new ReferenceType()),
                new CompStmt( new VarDeclaration("v2",new ReferenceType()),
                new CompStmt( new VarDeclaration("x",new IntegerType()),
                new CompStmt( new VarDeclaration("q",new IntegerType()),
                        new CompStmt(new NewHeapStmt("v1", new ValueExpression(new IntegerValue(20))),
                                new CompStmt(new NewHeapStmt("v2",new ValueExpression(new IntegerValue(30))),
                                        new CompStmt(new NewLockStmt("x"),
                                                new CompStmt(new ForkStmt(new CompStmt(new ForkStmt(new CompStmt(new LockStmt("x"),
                                                        new CompStmt(new WriteHeapStmt("v1",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")),'-',new ValueExpression(new IntegerValue(1)))),new UnlockStmt("x")))),
                                                        new CompStmt(new LockStmt("x"),new CompStmt(new WriteHeapStmt("v1",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")),'*',new ValueExpression(new IntegerValue(10)))),new UnlockStmt("x"))))),
                                                        new CompStmt(new NewLockStmt("q"),new CompStmt(new ForkStmt(new CompStmt(new ForkStmt(new CompStmt(new LockStmt("q"),new CompStmt(new WriteHeapStmt("v2",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v2")),'+',new ValueExpression(new IntegerValue(5)))),new UnlockStmt("q")))),
                                                                new CompStmt(new LockStmt("q"),new CompStmt(new WriteHeapStmt("v2",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v2")),'*',new ValueExpression(new IntegerValue(10)))),new UnlockStmt("q"))))),
                                                                new CompStmt(new NOPStmt(),new CompStmt(new NOPStmt(),new CompStmt(new NOPStmt(),new CompStmt(new NOPStmt(),new CompStmt(new LockStmt("x"),new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v1"))),new CompStmt(new UnlockStmt("x"),new CompStmt(new LockStmt("q"),
                                                                        new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v2"))),new UnlockStmt("q"))))))))))))))))))));
        IStmt ex19 =new CompStmt( new VarDeclaration("v1",new ReferenceType()),
            new CompStmt(new VarDeclaration("x",new IntegerType()),new NewLockStmt("x")));

        IStmt ex20 = new CompStmt(new VarDeclaration("v1",new ReferenceType()),
                new CompStmt(new VarDeclaration("cnt", new IntegerType()),
                        new CompStmt(new NewHeapStmt("v1",new ValueExpression(new IntegerValue(1))),
                                new CompStmt(new NewSemaphoreStmt("cnt",new ReadHeapExpression(new VarExpression("v1"))),
                                        new CompStmt(new ForkStmt(new CompStmt(new AcquireStmt("cnt"),new CompStmt(new WriteHeapStmt("v1",new ArithmeticExpression( new ReadHeapExpression(new VarExpression("v1")),'*',new ValueExpression(new IntegerValue(10)))),new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v1"))),new ReleaseStmt("cnt"))))),
                                                new CompStmt(new ForkStmt(new CompStmt(new AcquireStmt("cnt"),new CompStmt(new WriteHeapStmt("v1",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")),'*',new ValueExpression(new IntegerValue(10)))),new CompStmt(new WriteHeapStmt("v1",new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")),'*',new ValueExpression(new IntegerValue(2)))),new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v1"))),new ReleaseStmt("cnt")))))),new CompStmt(new AcquireStmt("cnt"),new CompStmt(new PrintStmt(new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")),'-',new ValueExpression(new IntegerValue(1)))),new ReleaseStmt("cnt")))))))));



        IStmt ex22 =new CompStmt( new VarDeclaration("v",new IntegerType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntegerValue(10))),
                        new CompStmt(new ForkStmt(new CompStmt(new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'-',new ValueExpression(new IntegerValue(1)))),
                                new CompStmt(new AssignStmt("v", new ArithmeticExpression(new VarExpression("v"),'-', new ValueExpression(new IntegerValue(1)))),
                                        new PrintStmt(new VarExpression("v"))))),
                                new CompStmt(new WaitStmt(10), new PrintStmt(new ArithmeticExpression(new VarExpression("v"), '*',new ValueExpression(new IntegerValue(10))))))));
        IStmt sum = new PrintStmt(new ArithmeticExpression(new VarExpression("a"),'+',new VarExpression("b")));
        IStmt product = new PrintStmt(new ArithmeticExpression(new VarExpression("a"),'*',new VarExpression("b")));
        List<String> arr = Arrays.asList("a","b");
        IStmt ex23 =new CompStmt(new VarDeclaration("a",new IntegerType()),
                new CompStmt(new VarDeclaration("b",new IntegerType()),
                        new CompStmt(new VarDeclaration("v",new IntegerType()),
                                new CompStmt(new VarDeclaration("w",new IntegerType()),
                                        new CompStmt(new ProcedureDeclaration("sum", arr, sum),
                                                new CompStmt(new ProcedureDeclaration("product", arr,product),
                                                        new CompStmt(new AssignStmt("v",new ValueExpression(new IntegerValue(2))),
                                                                new CompStmt(new AssignStmt("w",new ValueExpression(new IntegerValue(5))),
                                                                        new CompStmt(new CallStmt("sum",new ArrayList<Exp>(Arrays.asList(new ArithmeticExpression(new VarExpression("v"),'*', new ValueExpression(new IntegerValue(10))),new VarExpression("w")))),new CompStmt(new PrintStmt(new VarExpression("v")),
                                                                        new ForkStmt(new CompStmt(new CallStmt("product",new ArrayList<Exp>(Arrays.asList(new VarExpression("v"),new VarExpression("w")))),new ForkStmt(new CallStmt("sum",new ArrayList<Exp>(Arrays.asList(new VarExpression("v"),new VarExpression("w")))))))))))))))));

        IStmt ex24 = new CompStmt(new VarDeclaration("a",new ReferenceType()),
                new CompStmt(new VarDeclaration("b",new ReferenceType()),
                        new CompStmt(new VarDeclaration("v",new IntegerType()),
                                new CompStmt(new NewHeapStmt("a",new ValueExpression(new IntegerValue(0))),
                                        new CompStmt(new NewHeapStmt("b",new ValueExpression(new IntegerValue(0))),
                                                new CompStmt(new WriteHeapStmt("a",new ValueExpression(new IntegerValue(1))),
                                                        new CompStmt(new WriteHeapStmt("b",new ValueExpression(new IntegerValue(2))),
                                                                new CompStmt(new ConditionalAssignmentStmt("v",new RelationalExpression(new ReadHeapExpression(new VarExpression("a")),1,new ReadHeapExpression(new VarExpression("b"))),new ValueExpression(new IntegerValue(100)),new ValueExpression(new IntegerValue(200))),
                                                                        new CompStmt(new PrintStmt(new VarExpression("v")),
                                                                                new CompStmt(new ConditionalAssignmentStmt("v",new RelationalExpression(new ArithmeticExpression(new ReadHeapExpression(new VarExpression("b")),'-',new ValueExpression(new IntegerValue(2))),5,new ReadHeapExpression(new VarExpression("a"))),new ValueExpression(new IntegerValue(100)),new ValueExpression(new IntegerValue(200))),
                                                                                        new PrintStmt(new VarExpression("v"))))))))))));

        IStmt ex21 =new CompStmt(new VarDeclaration("cnt",new IntegerType()),
                new CompStmt(new VarDeclaration("v1",new ReferenceType()),
                        new CompStmt(new VarDeclaration("v2",new ReferenceType()),
                                new CompStmt(new VarDeclaration("v3",new ReferenceType()),
                                        new CompStmt(new NewHeapStmt("v1", new ValueExpression(new IntegerValue(2))),
                                                new CompStmt(new NewHeapStmt("v2", new ValueExpression(new IntegerValue(3))),
                                                        new CompStmt(new NewHeapStmt("v3", new ValueExpression(new IntegerValue(4))),
                                                                new CompStmt(new NewLatchStmt("cnt", new ReadHeapExpression(new VarExpression("v2"))),
                                                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("v1", new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v1")), '*' ,new ValueExpression(new IntegerValue(10)))), new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v1"))),new CountDownStmt("cnt")))),
                                                                                new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("v2", new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v2")),'*' ,new ValueExpression(new IntegerValue(10)))), new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v2"))),new CountDownStmt("cnt")))),
                                                                                        new CompStmt(new ForkStmt(new CompStmt(new WriteHeapStmt("v3", new ArithmeticExpression(new ReadHeapExpression(new VarExpression("v3")),'*' ,new ValueExpression(new IntegerValue(10)))), new CompStmt(new PrintStmt(new ReadHeapExpression(new VarExpression("v3"))),new CountDownStmt("cnt")))),
                                                                                                new CompStmt(new AwaitLatchStmt("cnt"),
                                                                                                        new CompStmt(new PrintStmt(new ValueExpression(new IntegerValue(100))),
                                                                                                                new CompStmt(new CountDownStmt("cnt"), new PrintStmt(new ValueExpression(new IntegerValue(100)))))))))))))))));
        //System.out.println(ex1.toString());
        this.TextOfPrograms.put(ex1.toString(),ex1);
        this.TextOfPrograms.put(ex2.toString(),ex2);
        this.TextOfPrograms.put(ex3.toString(),ex3);
        this.TextOfPrograms.put(ex4.toString(),ex4);
        this.TextOfPrograms.put(ex5.toString(),ex5);
        this.TextOfPrograms.put(ex6.toString(),ex6);
        this.TextOfPrograms.put(ex7.toString(),ex7);
        this.TextOfPrograms.put(ex8.toString(),ex8);
        this.TextOfPrograms.put(ex9.toString(),ex9);
        this.TextOfPrograms.put(ex10.toString(),ex10);
        this.TextOfPrograms.put(ex11.toString(),ex11);
        this.TextOfPrograms.put(ex12.toString(),ex12);
        this.TextOfPrograms.put(ex13.toString(),ex13);
        this.TextOfPrograms.put(ex14.toString(),ex14);
        this.TextOfPrograms.put(ex15.toString(),ex15);
        this.TextOfPrograms.put(ex16.toString(),ex16);
        this.TextOfPrograms.put(ex17.toString(),ex17);
        this.TextOfPrograms.put(ex18.toString(),ex18);
        this.TextOfPrograms.put(ex19.toString(),ex19);
        this.TextOfPrograms.put(ex20.toString(),ex20);
        this.TextOfPrograms.put(ex22.toString(),ex22);
        this.TextOfPrograms.put(ex23.toString(),ex23);
        this.TextOfPrograms.put(ex24.toString(),ex24);
        this.TextOfPrograms.put(ex21.toString(),ex21);
    }



}
