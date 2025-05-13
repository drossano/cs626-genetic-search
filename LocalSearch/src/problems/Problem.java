package problems;

/**
 * Defines a generic interface for a local search problem.
 *
 * @param <S> The data type representing a state (e.g., list, array, etc.)
 */
public interface Problem<S> {

    /**
     * Given the current state, generate a new neighboring state.
     * Typically used to explore the search space.
     *
     * @param current The current state
     * @return A new neighboring state
     */
    S generateNeighbor(S current);

    /**
     * Calculate the cost associated with a given state.
     * Lower cost typically means a better solution.
     *
     * @param state The state to evaluate
     * @return The cost of the state
     */
    int cost(S state);

    /**
     * Provide the initial state for the problem.
     *
     * @return The initial state
     */
    S getInitState();

    /**
     * Print the given state in a human-readable format.
     *
     * @param state The state to display
     */
    void printState(S state);
}
