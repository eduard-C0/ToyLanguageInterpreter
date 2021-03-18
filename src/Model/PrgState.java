package Model;
import Model.ValueType.StringValue;
import Model.ValueType.Value;
import Exception.MyException;
import Model.ADT.*;
import Model.Statement.IStmt;

import java.io.BufferedReader;

public class PrgState {
    private MyIStack<IStmt> exeStack;
    private MyIDictionary<String,Value>symTable;
    private MyIList<Value> out;
    private MyIDictionary<StringValue,BufferedReader> fileTable;
    private MyILockTable lockTable;
    private  IStmt originalProgram;
    private MyIHeap<Value>heap;
    private MyISemaphore semaphore;
    private MyILockTable countDownLatch;
    private MyIStack<MyIDictionary<String,Value>> stackOfSymTable;
    private MyIProcTable proceduresTable;
    private static int freeID = 0;
    private int stateID ;

    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String,Value> symTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog){
        this.exeStack=exeStack;
        this.symTable = symTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }
    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String,Value> symTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog,MyILockTable lockTable,MyISemaphore semaphore,MyILockTable countDownLatch){
        this.exeStack=exeStack;
        this.symTable = symTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }
    public PrgState(MyIStack<IStmt> stack, MyIDictionary<String,Value> stringValueMyIDictionary, MyIList<Value> valueMyIList){
        exeStack = stack;
        symTable = stringValueMyIDictionary;
        out = valueMyIList;
        heap = new MyHeap<Value>();
    }
    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String,Value> symTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog,MyILockTable lockTable){
        this.exeStack=exeStack;
        this.symTable = symTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }
    public PrgState(MyIStack<IStmt> exeStack, MyIDictionary<String,Value> symTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog,MyISemaphore semaphore){
        this.exeStack=exeStack;
        this.symTable = symTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.semaphore = semaphore;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }

    public PrgState(MyIStack<IStmt> exeStack, MyIStack<MyIDictionary<String,Value>> stackOfSymTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog,MyIProcTable proceduresTable){
        this.exeStack=exeStack;
        this.stackOfSymTable = stackOfSymTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.proceduresTable = proceduresTable;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }

    public PrgState(MyIStack<IStmt> exeStack, MyIStack<MyIDictionary<String,Value>> stackOfSymTable, MyIList<Value> out, MyIDictionary<StringValue, BufferedReader> fileTable,MyIHeap<Value> heap,IStmt prog,MyILockTable lockTable,MyISemaphore semaphore,MyILockTable countDownLatch,MyIProcTable proceduresTable){
        this.exeStack=exeStack;
        this.stackOfSymTable = stackOfSymTable;
        this.out=out;
        this.originalProgram = prog;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.semaphore = semaphore;
        this.countDownLatch = countDownLatch;
        this.proceduresTable = proceduresTable;
        this.stateID = getNewPrgStateId();
        exeStack.push(prog);
    }


    public MyIStack<IStmt> getStack(){
        return exeStack;
    }

    public MyIDictionary<String, Value>getSymTable(){
        return stackOfSymTable.peek();
    }
    public MyIList<Value> getOut(){
        return out;
    }
    public IStmt getOriginalProgram(){
        return originalProgram;
    }

    public MyIHeap<Value> getPrgHeap() {
        return heap;
    }

    public void setHeap(MyIHeap<Value> heap) {
        this.heap = heap;
    }

    public void setExeStack(MyIStack<IStmt> stack)
    {
        exeStack = stack;
    }

    public void setSymTable(MyIDictionary<String,Value> table){
        symTable = table;
    }
    public MyIDictionary<StringValue,BufferedReader> getFileTable(){
        return fileTable;
    }

    public void setFileTable(MyIDictionary<StringValue, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public void setOut(MyIList<Value>output){
        out=output;
    }
    public boolean isNotCompleted(){
        return !exeStack.isEmpty();
    }
    public PrgState oneStep() throws MyException {
        if(exeStack.isEmpty())
        {
            throw new MyException("Program stack is empty!");
        }
        IStmt currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public static synchronized int getNewPrgStateId(){
        return ++freeID;
    }

    public int getStateID(){
        return stateID;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Program state\n");
        str.append("ID: ").append(stateID).append(" \n");
        str.append("Exe Stack: ").append(exeStack).append(" \n");
        str.append("Sym Table: ").append(symTable).append(" \n");
        str.append("Output Console: ").append(out).append(" \n");
        str.append("File Table: ").append(fileTable.toString()).append(" \n");
        str.append("Heap: ").append(heap.toString()).append(" \n");
        //str.append("LockTable: ").append(lockTable.toString()).append('\n');
        //str.append("SemaphoreTable: ").append(semaphore.toString()).append('\n');
        //str.append("LatchTable: ").append(countDownLatch.toString()).append('\n');
        str.append("StackOfSymTable: ").append(stackOfSymTable.toString()).append('\n');
        str.append("ProceduresTable: ").append(proceduresTable.toString()).append('\n');
        return str.toString();
    }

    public MyILockTable getLockTable() {
        return lockTable;
    }

    public void setLockTable(MyILockTable lockTable) {
        this.lockTable = lockTable;
    }

    public MyISemaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(MyISemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public MyILockTable getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(MyILockTable countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public MyIStack<MyIDictionary<String, Value>> getStackOfSymTable() {
        return stackOfSymTable;
    }

    public void setStackOfSymTable(MyIStack<MyIDictionary<String, Value>> stackOfSymTable) {
        this.stackOfSymTable = stackOfSymTable;
    }

    public MyIProcTable getProceduresTable() {
        return proceduresTable;
    }

    public void setProceduresTable(MyIProcTable proceduresTable) {
        this.proceduresTable = proceduresTable;
    }
}
