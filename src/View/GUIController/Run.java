package View.GUIController;

import Model.ADT.*;
import Model.PrgState;
import Model.Statement.IStmt;
import Model.ValueType.StringValue;
import Model.ValueType.Value;
import Service.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;
import Exception.*;

import java.io.BufferedReader;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import Observer.MyObserver;

public class Run extends MyObserver {
    @FXML
    TableView <Pair<Integer, Integer>>LatchTable;
    @FXML
    TableColumn <Pair<Integer, Integer>,Integer>LatchVar;
    @FXML
    TableColumn <Pair<Integer, Integer>,Integer>LatchExp;
    @FXML
    TableView <Triple<String,ObservableList<String>,IStmt>> ProcedureTable;
    @FXML
    TableColumn <Triple<String,ObservableList<String>,IStmt>,String> ProcedureName;
    @FXML
    TableColumn <Triple<String,ArrayList<String>,IStmt>,String>ProcedureVariables;
    @FXML
    TableColumn <Triple<String,ArrayList<String>,IStmt>,IStmt> ProcedureStatemente;
    @FXML
    javafx.scene.control.Button runOneStepButton;
    @FXML
    javafx.scene.control.Button runAnotherProgramButton;
    @FXML
    TableColumn<Pair<Integer, Value>, Integer> heapAddressColumn;
    @FXML
    TableColumn<Pair<Integer, Value>, Value> heapValueColumn;
    @FXML
    TableColumn<Pair<String, Value>, String> symbolsTableNameColumn;
    @FXML
    TableColumn<Pair<String, Value>, Value> symbolsTableValueColumn;
    @FXML
    Label numberOfProgramStatesLabel;
    @FXML
    TableView<Pair<java.lang.Integer, Value>> heapTableView;
    @FXML
    ListView<java.lang.Integer> idsListView;
    @FXML
    ListView<IStmt> executionStackListView;
    @FXML
    ListView<String> outListView;
    @FXML
    ListView<StringValue> fileTableListView;
    @FXML
    TableView<Pair<String, Value>> symbolsTableView;

    Stage parentStage;
    Scene selectProgramsScene;

    Controller controller;

    @FXML
    void initialize(){

        this.idsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.symbolsTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.symbolsTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.heapAddressColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.heapValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        this.ProcedureName.setCellValueFactory(new PropertyValueFactory<>("first"));
        this.ProcedureVariables.setCellValueFactory(new PropertyValueFactory<>("second"));
        this.ProcedureStatemente.setCellValueFactory(new PropertyValueFactory<>("third"));
        this.LatchVar.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.LatchExp.setCellValueFactory(new PropertyValueFactory<>("value"));

    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void setSelectProgramsScene(Scene selectProgramsScene) {
        this.selectProgramsScene = selectProgramsScene;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.controller.setExecutorService(Executors.newFixedThreadPool(8));
    }

    public void update(){
        try {
            this.setStatesLabel();
            this.setIdsList();
            this.populateExecutionStack();
            this.populateSymbolsTable();
            this.populateHeapTable();
            this.populateFileTable();
            this.populateOut();
            this.populateProcedureTable();
            this.populateCountDownLatch();
        }
        catch (MyException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(exception.getMessage());
            alert.showAndWait();
        }
    }

    void setStatesLabel(){
        this.numberOfProgramStatesLabel.setText("states: " + this.controller.getPrograms().size());
    }

    void setIdsList(){
        ObservableList<Integer> programIds = FXCollections.observableArrayList();

        programIds.addAll(this.controller.getPrograms().stream().map(PrgState::getStateID).collect(Collectors.toList())
        );

        this.idsListView.setItems(programIds);
    }

    PrgState getSelectedProgram() throws MyException{
        Integer id = this.idsListView.getSelectionModel().getSelectedItem();

        if(id == null){
            this.idsListView.getSelectionModel().selectIndices(0);
            id = this.idsListView.getItems().get(0);

        }
        Optional<PrgState> program = this.controller.getProgram(id);
        if(program.isEmpty())
            throw new MyException("Done!");
        return program.get();
    }

    public void BackToMenu() {
        this.closeProgram();
        this.parentStage.setScene(this.selectProgramsScene);
    }

    void populateProcedureTable(){
        PrgState program = this.getSelectedProgram();

        MyIProcTable procTable = program.getProceduresTable();

        ArrayList<Triple<String,ObservableList<String>,IStmt>> entries = new ArrayList<>();

        for (Map.Entry<String,Pair<List<String>, IStmt>> entry: procTable.getContent().entrySet()) {
            entries.add(new Triple<String,ObservableList<String>,IStmt>(entry.getKey(),FXCollections.observableArrayList(entry.getValue().getKey()),entry.getValue().getValue()));
        }
        this.ProcedureTable.setItems(FXCollections.observableArrayList(entries));
    }

    void populateCountDownLatch(){
        PrgState program = this.getSelectedProgram();

        MyILockTable countDownLatchTable = program.getCountDownLatch();

        ArrayList<Pair<Integer, Integer>> entries = new ArrayList<Pair<Integer, Integer>>();
        for (Map.Entry<Integer, Integer> entry: countDownLatchTable.getContent().entrySet()) {
            entries.add(new Pair<Integer, Integer>(entry.getKey(), entry.getValue()));
        }
        this.LatchTable.setItems(FXCollections.observableArrayList(entries));
    }

    void populateExecutionStack(){
        PrgState program = this.getSelectedProgram();

        ObservableList<IStmt> statements = FXCollections.observableArrayList();

        statements.addAll(program.getStack().stream().collect(Collectors.toList()));
        //System.out.println(program.getStack());
        this.executionStackListView.setItems(statements);
    }

    void populateSymbolsTable(){
        PrgState program = this.getSelectedProgram();
        MyIDictionary<String, Value> symTable = program.getSymTable();
        ArrayList<Pair<String,Value>> entries = new ArrayList<Pair<String,Value>>();
        for (Map.Entry<String, Value> entry: symTable.getContent().entrySet()) {
            entries.add(new Pair<String,Value>(entry.getKey(), entry.getValue()));
        }
        this.symbolsTableView.setItems(FXCollections.observableArrayList(entries));
    }
    void populateHeapTable(){
        PrgState program = this.getSelectedProgram();
        MyIHeap<Value> heap = program.getPrgHeap();
        ArrayList<Pair<java.lang.Integer, Value>> entries = new ArrayList<Pair<java.lang.Integer, Value>>();
        for (Map.Entry<java.lang.Integer, Value> entry: heap.getContent().entrySet()) {
            entries.add(new Pair<Integer, Value>(entry.getKey(), entry.getValue()));
        }
        this.heapTableView.setItems(FXCollections.observableArrayList(entries));
    }
    void populateFileTable(){
        PrgState program = this.getSelectedProgram();

        MyIDictionary<StringValue, BufferedReader> fileTable = program.getFileTable();
        System.out.println(program.getFileTable());
        List<StringValue> list = new ArrayList<>(fileTable.getContent().keySet());
        fileTableListView.setItems(FXCollections.observableArrayList(list));
    }

    void populateOut(){
        PrgState program = this.getSelectedProgram();

        ObservableList<String> out = FXCollections.observableArrayList();

        out.addAll(program.getOut().stream().map(Value::toString).collect(Collectors.toList()));
        this.outListView.setItems(out);
    }

    public void GuiSelectProgram() {
        this.update();
    }

    public void handleRunOneStep() {
        List<PrgState> programs = this.controller.removeCompletedPrograms(this.controller.getPrograms());
       // PrgState state ;
        if(programs.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Done!");
            alert.showAndWait();
            return;
        }
        this.controller.gabageCollector(programs);

        try{
            this.controller.oneStepForAllPrg(programs);
            //state = this.getSelectedProgram();
            //this.controller.oneStep(state);
            //update();
        }
        catch (MyException exception){
            this.closeProgram();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(exception.getMessage());
            alert.showAndWait();
            return;
        }
        programs = this.controller.removeCompletedPrograms(programs);

        if(programs.isEmpty()){
            this.closeProgram();
        }
    }

    void closeProgram(){
        this.controller.getExecutorService().shutdownNow();
        this.controller.setRepositoryPrograms(new LinkedList<>());
        this.idsListView.getItems().clear();
        this.setStatesLabel();
    }


}
