package Model.Statement;
import Exception.*;
import Model.ADT.*;
import Model.PrgState;
import Model.Type.Type;
import Model.ValueType.StringValue;
import Model.ValueType.Value;

import java.io.BufferedReader;
import java.util.Map;

public class ForkStmt implements IStmt{

    private IStmt statement;

    public ForkStmt(IStmt statement){
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException, ExpressionException {
        MyIStack<IStmt> stack = state.getStack();
        MyIHeap<Value>heap = state.getPrgHeap();
        //MyIDictionary<String,Value>symTable = state.getSymTable();
        MyIList<Value> output = state.getOut();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        MyIStack<IStmt> newStack = new MyStack<>();
        MyILockTable lockTable = state.getLockTable();
        MyISemaphore semaphore = state.getSemaphore();
        MyILockTable countDownLatch = state.getCountDownLatch();
        MyIProcTable procTable = state.getProceduresTable();
        return new PrgState(newStack,new MyStack<>(state.getStackOfSymTable().copy()),output,fileTable,heap,statement,lockTable,semaphore,countDownLatch,procTable);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnvironment) throws StmtException, ExpressionException {
        statement.typecheck(typeEnvironment.copy());
        return typeEnvironment;
    }

    @Override
    public String toString() {
        return "Fork(" + statement + ')';
    }
}
