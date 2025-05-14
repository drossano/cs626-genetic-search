package core_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Abstract class for implementing a Genetic Algorithm.
 *
 * The GA assumes that a higher fitness score indicates a better
 * solution.
 * Subclasses must define how individuals are reproduced, mutated,
 * and evaluated.
 *
 * @param <G> The data type representing a chromosome (e.g., List,
 *           Array, etc.)
 */
public abstract class GeneticAlgorithm<G> {
    private final int MAX_GEN; //Maximum number of generations
    private final double MUTATION; // Mutation rate
    private final double ELITISM; // Elitism rate

    // Random number generator
    private static final Random RANDOM = new Random();

    public GeneticAlgorithm(int maxGen,
                            double mutation,
                            double elitism){
        this.MAX_GEN =maxGen;
        this.MUTATION = mutation;
        this.ELITISM = elitism;
    }

    /**
     * Selects an individual from the population based on
     * fitness-proportional selection.
     * Ensures that the selected individual is not the same
     * as the provided one.
     *
     * @param population The list of all individuals.
     * @param indiv An individual to avoid selecting (can be
     *              null if no exclusion needed).
     * @return A selected individual.
     */
    public Individual<G> select(List<Individual<G>> population,
                                Individual<G> indiv){
        double sum = 0;
        for (Individual<G> i : population) {
            sum += i.fitnessScore();
        }
        Individual<G> selected = null;
        do {
            double v = RANDOM.nextDouble(sum);
            double cumulativeSum = 0;
            for(Individual<G> i : population){
                cumulativeSum += i.fitnessScore();
                if (v <= cumulativeSum){
                    selected = i;
                    break;
                }
            }
        } while(selected == indiv);

        return selected;
    }

    /**
     * Abstract method to define crossover behavior.
     * The implementation must NOT modify the parent individuals,
     * as they may be used again in more crossovers.
     * @param p   parent 1
     * @param q   parent 2
     * @return    new offspring individual
     */
    public abstract Individual<G> crossover(Individual<G> p,
                                            Individual<G> q);

    /**
     * Abstract method to define mutation behavior.
     *
     * @param indiv Individual to mutate
     * @return Mutated individual
     */
    public abstract Individual<G> mutate(Individual<G> indiv);

    /**
     * Abstract method to calculate the fitness score of a chromosome.
     *
     * @param chromosome The chromosome to evaluate
     * @return Fitness score
     */
    public abstract double calcFitnessScore (G chromosome);

    /**
     * Run the genetic algorithm starting from the given initial population.
     *
     * The algorithm evolves the population over multiple generations by:
     * - Producing offspring through crossover and mutation
     * - Applying elitism to retain top-performing individuals
     * - Tracking and returning the best individual found
     *
     * @param initPopulation The initial population of individuals
     * @return The best individual found after evolution
     */
    public Individual<G> evolve (List<Individual<G>> initPopulation){
        List<Individual<G>> population = initPopulation;
        Collections.sort(population); //Higher fitness first
        int bestGen = 0;
        Individual<G> best = population.get(0);

        for (int generation = 1; generation <= MAX_GEN; generation++) {
            // Generate the offspring for the next generation
            List<Individual<G>> offspring = produceNextGeneration(population);
            // sort offspring by fitness
            Collections.sort(offspring);
            // Create a new population with elitism
            List<Individual<G>> newPopulation = new ArrayList<>();
            // Number of elites to retain
            int e = (int) (ELITISM * population.size());
            for (int i=0; i<e; i++) {
                // Add top elites
                newPopulation.add(population.get(i));
            }
            for (int i=0; i<population.size()-e; i++) {
                //Fill the rest with offspring
                newPopulation.add(offspring.get(i));
            }
            population = newPopulation;
            Collections.sort(population);

            //keep track of the best solution found so far
            if (population.get(0).fitnessScore() > best.fitnessScore()){
                best = population.get(0);
                bestGen = generation;
            }
        }

        System.out.println("best generation: "+bestGen);
        return population.get(0);
    }

    /**
     * Produce the next generation by generating a list of offspring.
     *
     * For each individual to be created:
     *
     * 1. Select two parent individuals from the current population.
     *    - Use the select() method provided.
     *    - The first parent can be any individual.
     *    - The second parent must be different from the first.
     *
     * 2. Perform crossover between the two selected parents.
     *    - Use the crossover() method to create a new child.
     *
     * 3. With probability equal to the mutation rate:
     *    - Apply mutation to the child using the mutate() method.
     *
     * 4. Add the resulting child to the offspring list.
     *
     * Note: You must create exactly as many offspring as there are
     * individuals in the current population. In other words, the size
     * of the offspring list must be exactly equal to the size of the
     * input population.
     *
     * @param population The current population of individuals
     * @return A new list containing the offspring individuals
     */
    protected List<Individual<G>> produceNextGeneration(List<Individual<G>> population) {
        List<Individual<G>> offspring_list = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            Individual<G> parent1 = select(population, null);
            Individual<G> parent2 = select(population, parent1);
            Individual<G>  child = crossover(parent1, parent2);
            if (RANDOM.nextFloat() <= MUTATION) {
                child = mutate(child);
            }
            offspring_list.add(child);
        }
        return offspring_list;
    }
}
