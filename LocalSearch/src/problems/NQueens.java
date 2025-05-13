package problems;

import java.util.Arrays;
import java.util.Random;

/**
 * Defines the N-Queens problem.
 *
 * Chromosomes are integer arrays where each index represents a column,
 * and the value represents the row position of the queen.
 */
public class NQueens implements Problem<int[]>{
    //number of queens
    private final int N;

    /**
     * Constructor for NQueens.
     *
     * @param n number of queens
     */
    public NQueens(int n){
        N = n;
    }

    /**
     * Generate a new neighbor state by randomly moving one queen
     * to a different row in its column.
     *
     * @param current current state
     * @return new neighbor state
     */
    @Override
    public int[] generateNeighbor(int[] current){
        Random rand = new Random();
        int column = rand.nextInt(N);
        int currentRow = current[column];
        int newRow;
        do{
            newRow = rand.nextInt(N);
        }while(newRow==currentRow);
        int[] newState = Arrays.copyOf(current, current.length);
        newState[column] = newRow;
        return newState;
    }

    /**
     * Compute the cost (number of conflicts) for a given state.
     *
     * Conflicts occur when two queens share the same row
     * or are on the same diagonal.
     *
     * @param state the state to evaluate
     * @return number of conflicts
     */
    @Override
    public int cost(int[] state){
        int conflicts = 0;
        for(int i=0; i<N-1; i++){
            for(int j=i+1; j<N; j++){
                //checking all the row conflicts
                if(state[i] == state[j]){
                    conflicts++;
                }
                //check all the diagonal conflicts
                if(Math.abs(state[i]-state[j]) == Math.abs(i-j)){
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    /**
     * Generate a random initial state.
     *
     * Each queen is randomly placed in a row for each column.
     *
     * @return random initial state
     */
    @Override
    public int[] getInitState(){
        Random rand = new Random();
        int[] state = new int[N];
        for(int i=0; i<N; i++){
            state[i] = rand.nextInt(N);
        }
        return state;
    }

    /**
     * Print the current state as a board.
     *
     * Queens are shown as 'Q', empty squares as '.'.
     *
     * @param state the state to print
     */
    @Override
    public void printState(int[] state){
        for(int row=0; row<N; row++){
            for(int col=0; col<N; col++){
                if(state[col] == row){
                    System.out.print(" Q ");
                }else{
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Get the number of queens.
     *
     * @return number of queens
     */
    public int getN(){
        return N;
    }
}
