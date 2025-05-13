package core_algorithms;

/**
 * Represents a single individual in a population for the Genetic Algorithm.
 *
 * @param <G> The data type of the chromosome (e.g., a list, array, etc.)
 */
public record Individual<G>(G chromosome, double fitnessScore)
        implements Comparable<Individual<G>> {

    /**
     * Compares individuals by fitness score in descending order.
     * Higher fitness individuals are considered "better" (i.e., come first
     * when sorted).
     *
     * @param i Another individual to compare against
     * @return Negative if this individual has higher fitness,
     *         positive if lower,
     *         0 if equal
     */
    public int compareTo(Individual<G> i) {
        return Double.compare(
                i.fitnessScore(),
                this.fitnessScore()
        );
    }
}
