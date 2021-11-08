import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 *Transition Function NFA to DFA
 *
 * @author Isaac J. Nuno
 * @version 0.0, 5 April 2021
 */
public class TransitionFunction {
    ArrayList<ArrayList<Integer>> table;                            //transition function table

    /**
     * Default Constructor
     */
    TransitionFunction(){
        this.table = new ArrayList<>();
    }

    /**
     *Reads input string one character at a time
     * adds states to table
     *
     */
    public void readFromFile(){
        int index=0;
        Scanner keyboard = new Scanner(System.in);
        System.out.printf("%s","Enter file path: ");
        String filePath = keyboard.nextLine();
        File inputFile = new File(filePath);
        try(FileReader fr = new FileReader(inputFile)){
            int content;
            table.add(new ArrayList<>());
            while((content = fr.read()) != -1){

                if((char) content == '\n'){
                    table.add(new ArrayList<>());                   //create a new list for each row
                    index++;
                }
                if(Character.isDigit((char) content)){
                    table.get(index).add(Integer.parseInt(String.valueOf((char)content))); //Add state to list
                }
            }
        }catch(FileNotFoundException e){System.out.printf("%s", "File not found. Check path.");}
        catch(InputMismatchException | IOException e){System.out.printf("%s", "Check file for bad data.");}
    }

    /**
     * Finds DFA states equivalent to its NFA
     *(Power set)
     *
     * @return Set of sets of type Integer
     */
    public Set<Set<Integer>> equivalentDFA(){
        Set<Set<Integer>> states = new HashSet<>();             //Set to hold power set
        ArrayList<Integer> buffer = new ArrayList<>();          //Buffer keeps track of DFA states
        for(int i=0; i<table.size(); i++){
            helpFindStates(states, buffer, i);                  //Helper method to find all DFA states
        }
        return states;
    }

    /**
     * Helper method to find DFA states
     *
     * @param states set to hold DFA states (Power set)
     * @param buffer Keeps track of DFA state during recursion
     * @param i      used to check if end of list has been reached
     */
    private void helpFindStates(Set<Set<Integer>> states, ArrayList<Integer> buffer, int i){

            if (i == table.size()-1) {
                buffer.add(table.get(i).get(0));
                states.add(new HashSet<>(buffer));
                buffer.clear();
            } else {
                buffer.add(table.get(i).get(0));
                states.add(new HashSet<>(buffer));
                ArrayList<Integer> currentList = new ArrayList<>(buffer);
                for(int j=i; j < table.size()-1; j++){
                    helpFindStates(states, buffer, j+1);
                    buffer.addAll(currentList);
                }
                buffer.clear();
            }
    }

    /**
     * Computes states reachable by an empty string
     * for all states in table
     *
     */
    public void computeE(){
        Set<Integer> e = new HashSet<>();                    //Holds states reached by empty string
        for (ArrayList<Integer> state : table) {             //For all states
            helpComputeE(state.get(0), e);                   //compute E for current state
            printE(e, state.get(0));                         //Print all states reachable with an empty string
            e.clear();
        }
    }

    /**
     * Helper method to compute states reachable from current state
     *
     * @param state current state
     * @param e     holds states reachable by the empty string
     */
    private void helpComputeE(int state, Set<Integer> e){

        Iterator<Integer> currentState = table.get(state-1).iterator();

        e.add(currentState.next());                         //Current state is added to set e
        while(currentState.hasNext()) {                     //While current state has states reachable by an empty string
            helpComputeE(currentState.next(), e);           //Check if next state has states reachable by an empty string
        }
    }

    /**
     * Prints set of helper method computeE
     * @param e set to be printed
     * @param i State computed Ex: "E(i)"
     */
    public void printE(Set<Integer> e, int i){
        System.out.printf("E(%s) = {", i);
        Iterator<Integer> set = e.iterator();

        while(set.hasNext()) {
            System.out.printf("%s", set.next());

            if(set.hasNext())
                System.out.print(", ");
        }
        System.out.print("}\n");
    }

    /**
     * Prints DFA states by size of set
     *
     * @param setList set of sets to be printed
     */
    public void printDFASet(Set<Set<Integer>> setList){

        System.out.print("State set of equivalent DFA = {empty, ");
        int i=1;
        Iterator<Set<Integer>> sets = setList.iterator();
        while(i<=table.size()){
            while(sets.hasNext()) {                         //while there are sets in setList
                Set<Integer> s = sets.next();               //For each individual set
                if (s.size() == i){                         //if size of set == i, print set
                    Iterator<Integer> states = s.iterator();
                    System.out.print("{");
                    while (states.hasNext()) {              //while set has states left, print next state
                        System.out.printf("%s", states.next());
                        if(states.hasNext())
                            System.out.print(", ");
                    }
                    System.out.printf("%s","}");
                    if(sets.hasNext())
                        System.out.print(", ");
                }
            }
            sets = setList.iterator();
            i++;
        }
        System.out.printf("%s","}\n");
    }
}
