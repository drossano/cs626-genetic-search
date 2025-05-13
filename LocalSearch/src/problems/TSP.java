package problems;

import java.util.*;


/**
 * Defines the Traveling Salesperson Problem (TSP) as a local search problem.
 *
 * A state represents a tour visiting all cities exactly once, returning to
 * the starting city.
 *
 * This class contains only problem-specific logic, and is kept separate from
 * optimization algorithms.
 */
public class TSP implements Problem<List<Integer>> {

    private final int[][] distanceMatrix; // Distance between each pair of cities
    private final List<Integer> INIT_STATE = new ArrayList<>(); // Initial tour

    /**
     * Constructs a TSP instance with a given distance matrix.
     *
     * @param dMatrix A 2D array representing the distances between cities
     */
    public TSP(int[][] dMatrix){
        this.distanceMatrix = dMatrix;
        //creating the initial tour (0, 1, 2, ..., N-1)
        for(int i=0; i<dMatrix.length; i++) {
            INIT_STATE.add(i);
        }
    }

    /**
     * Generate a neighboring tour by randomly swapping two cities in the
     * given tour.
     *
     * @param state Current tour
     * @return A new tour obtained by swapping two cities
     */
    @Override
    public List<Integer> generateNeighbor(List<Integer> state){
        Random r = new Random();
        int city1 = r.nextInt(state.size());
        int city2;
        do{
            city2 = r.nextInt(state.size());
        }while(city2 == city1);
        List<Integer> newState = new ArrayList<>(state);
        Collections.swap(newState,city1,city2);
        return newState;
    }

    /**
     * Calculates the total distance of the tour, including returning to the
     * starting city.
     *
     * @param state Tour to evaluate
     * @return Total travel distance
     */
    @Override
    public int cost(List<Integer> state){
        int totalDistance = 0;
        for (int i = 0, j = 1; j < state.size(); i++, j++) {
            totalDistance += distanceMatrix[state.get(i)][state.get(j)];
        }
        totalDistance += distanceMatrix[state.get(state.size() - 1)][state.get(0)];
        return totalDistance;
    }

    /**
     * Returns the initial tour.
     *
     * @return Initial tour
     */
    @Override
    public List<Integer> getInitState() {
        return INIT_STATE;
    }

    /**
     * Prints the given tour to standard output.
     *
     * @param state Tour to print
     */
    @Override
    public void printState(List<Integer> state) {
        System.out.println(state);
    }

}
