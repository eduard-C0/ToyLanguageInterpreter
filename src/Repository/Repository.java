package Repository;
import Model.PrgState;
import Model.Statement.IStmt;
import Exception.MyException;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.util.LinkedList;
import java.util.List;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;


public class Repository implements MyIRepo {
    private List<PrgState> states;
    private IStmt originalProgram;
    private String logFilePath ;

    public String getFileName() {
        return logFilePath ;
    }

    public void setFileName(String fileName) {
        this.logFilePath  = fileName;
    }

    public Repository(PrgState prgState, String fileName) throws IOException {
        this.originalProgram = prgState.getOriginalProgram();
        this.logFilePath  = fileName;
        states = new LinkedList<>();
        File file = new File(logFilePath);
        file.createNewFile();
        try(FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write("");
        }
        catch (IOException err){
            throw new MyException(err.getMessage());
        }

    }
    public Repository(String givenFile) {
        this.logFilePath = givenFile;
        states = new LinkedList<>();
    }
    public Repository(){
        states = new LinkedList<>();
    }

    @Override
    public List<PrgState> getPrgList() {
        return states;
    }

    @Override
    public void setPrgList(List<PrgState> list) {
        states = list;
    }

    @Override
    public PrgState getCrtPrg() {
        PrgState state =states.get(0);
        states.remove(0);
        return state;
    }

    @Override
    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    @Override
    public void addState(PrgState state) {
        states.add(state);
    }


    @Override
    public void logPrgStateExec(PrgState prgstate) throws IOException {
        File file = new File(logFilePath);
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(prgstate + "\n");
        }
        catch (IOException e) {
            throw new MyException(e.getMessage());
        }
    }
}
