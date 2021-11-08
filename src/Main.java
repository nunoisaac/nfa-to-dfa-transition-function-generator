public class Main {
    public static void main(String[] args){
        TransitionFunction nfa = new TransitionFunction();
        nfa.readFromFile();
        nfa.printDFASet(nfa.equivalentDFA());
        nfa.computeE();
    }
}
