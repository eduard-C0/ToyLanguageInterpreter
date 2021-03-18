package Service;

import Model.ADT.*;
import Exception.*;
import Model.Statement.*;
import Model.Type.Type;
import Model.ValueType.ReferenceValue;
import Model.ValueType.Value;
import Repository.MyIRepo;
import Model.PrgState;

import javax.swing.text.html.ListView;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import Observer.MyObservable;

public class Controller extends MyObservable {
    private MyIRepo repository;
    private ExecutorService executor;

    public Controller(MyIRepo repository){
        this.repository = repository;
    }

    Map<Integer, Value> unsafegabageCollector(List<Integer> validAddress, Map<Integer,Value>heap){
        return heap.entrySet().stream().filter(s -> validAddress.contains(s.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    List<java.lang.Integer> getAddressesFromValues(Collection<Value>values,Collection<Value>heapValues){
        return Stream.concat(
                values.stream()
                        .filter(v -> v instanceof ReferenceValue)
                        .map(v -> {ReferenceValue v1 = (ReferenceValue) v; return v1.getAddress();}),
                heapValues.stream()
                        .filter(v -> v instanceof ReferenceValue)
                        .map(v -> {ReferenceValue v1 = (ReferenceValue) v; return v1.getAddress();})
        ).collect(Collectors.toList());
    }

    public void gabageCollector(List<PrgState> prgList){
        List<Integer> adresses = Objects.requireNonNull(prgList.stream()
                .map(p -> getAddressesFromValues(
                        p.getSymTable().getContent().values(),
                        p.getPrgHeap().getContent().values()))
                .map(Collection::stream)
                .reduce(Stream::concat).orElse(null)).collect(Collectors.toList());
        prgList.forEach(programState -> {
            programState.getPrgHeap().setContent(
                    unsafegabageCollector(
                            adresses,
                            prgList.get(0).getPrgHeap().getContent()
                    ));
        });
        this.notyfiObservers();
    }
    public void oneStepForAllPrg(List<PrgState> prgList) throws MyException{
        prgList.forEach(prg-> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException | IOException e) {
                e.printStackTrace();
            }
        });
        List <Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p)->(Callable<PrgState>)(p::oneStep))
                .collect(Collectors.toList());
        try {
            List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (MyException | InterruptedException | ExecutionException e) {
                            //System.out.println(e.getMessage());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            prgList.addAll(newPrgList);
        }
        catch(InterruptedException e)
        {
            throw  new MyException(e.getMessage());
        }

        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (MyException | IOException e) {
                e.printStackTrace();
            }
        });


        repository.setPrgList(prgList);

        this.notyfiObservers();
    }

    public List<PrgState> removeCompletedPrograms(List<PrgState> prgList){
        return prgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }

    public List<PrgState> removeDuplicateStates(List<PrgState> prgList) {
        return prgList.stream().distinct().collect(Collectors.toList());
    }

    public PrgState oneStep(PrgState state)throws MyException, ExpressionException, StmtException{
        MyIStack<IStmt> stack = state.getStack();

        if(stack.isEmpty()){
            throw new MyException("Stack is empty!");
        }
        IStmt currentStatement = stack.pop();
        //this.notyfiObservers();
        return currentStatement.execute(state);
    }

    public void allSteps() throws MyException, IOException {
        executor = Executors.newFixedThreadPool(8);
        List<PrgState>  prgList=removeCompletedPrograms(repository.getPrgList());
        while(prgList.size() > 0){
            gabageCollector(prgList);
            prgList=removeCompletedPrograms(repository.getPrgList());
            prgList=removeDuplicateStates(prgList);
            oneStepForAllPrg(prgList);
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);
    }


    public void typecheckOriginalProgram() throws StmtException, ExpressionException {
        MyDictionary<String, Type> typeEnvironment = new MyDictionary<String, Type>();
        IStmt originalProgram = repository.getOriginalProgram();
        originalProgram.typecheck(typeEnvironment);
    }

    public void addState(PrgState state){
        repository.addState(state);
    }

    public List<PrgState> getPrograms() {
        return this.repository.getPrgList();
    }

    public Optional<PrgState> getProgram(Integer id){
        return this.repository.getPrgList().stream()
                .filter(p -> Objects.equals(p.getStateID(), id))
                .findFirst();
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executor = executorService;
    }

    public ExecutorService getExecutorService() {
        return executor;
    }

    public void setRepositoryPrograms(LinkedList<PrgState> programs) {
        this.repository.setPrgList(programs);
    }
}
