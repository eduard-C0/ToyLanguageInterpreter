package Repository;

import Exception.MyException;
import Model.PrgState;
import Model.Statement.IStmt;

import java.io.IOException;
import java.util.List;


public interface MyIRepo {
    public List<PrgState> getPrgList();
    PrgState getCrtPrg();
    IStmt getOriginalProgram();
    void addState(PrgState state);
    void logPrgStateExec(PrgState state) throws IOException;
    void setPrgList(List<PrgState> list);
}
