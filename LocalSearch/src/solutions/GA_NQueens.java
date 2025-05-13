package solutions;

import core_algorithms.GeneticAlgorithm;
import core_algorithms.Individual;
import problems.NQueens;

import java.util.*;

/**
 * Apply the genetic algorithm to solve the N-Queens problem.
 *
 * Chromosomes are represented as integer arrays, where the index
 * represents the column and the value represents the row position
 * of the queen.
 */
public class GA_NQueens extends GeneticAlgorithm<int[]> {

    private final NQueens problem;

    /**
     * Constructor for GA_NQueens.
     *
     * @param maxGen maximum number of generations
     * @param mRate mutation rate
     * @param elitism elitism rate
     * @param problem the N-Queens problem instance
     */
    public GA_NQueens(int maxGen, double mRate, double elitism, NQueens problem){
        super(maxGen, mRate, elitism);
        this.problem = problem;
    }

    /**
     * Crossover two parent solutions to produce a new offspring.
     *
     * The strategy is similar to ordered crossover:
     * - Randomly select two crossover points.
     * - Copy the segment between the points from parent 1.
     * - Fill the rest with genes from parent 2, skipping duplicates.
     *
     * @param p parent 1
     * @param q parent 2
     * @return offspring individual
     */
    public Individual<int[]> crossover(Individual<int[]> p, Individual<int[]> q) {
        // Select a random portion of parent1's chromosome by
        // randomly generating the start & the end positions.
        Random r = new Random();
        int startPos = r.nextInt(p.chromosome().length);
        int endPos = r.nextInt(p.chromosome().length);
        // make sure startPos <= endPos
        // If startPos is greater than endPos, swap them.
        if (startPos > endPos) {
            int t = startPos;
            startPos = endPos;
            endPos = t;
        }
        //First, copy the entire parent 1 chromosome to child.
        int[] childChromosome = Arrays.copyOf(p.chromosome(), p.chromosome().length);
        //Next, copy those genes before startPos and after endPos from parent 2 chromosome to child.
        for (int i = 0; i < startPos; i++) {
            childChromosome[i] = q.chromosome()[i];
        }
        for (int i = endPos + 1; i<q.chromosome().length; i++) {
            childChromosome[i] = q.chromosome()[i];
        }
        return new Individual<>(childChromosome, calcFitnessScore(childChromosome));
    }

    /**
     * Mutate an individual by generating a neighbor.
     *
     * Mutation helps maintain diversity by swapping two queens.
     *
     * @param indiv individual to mutate
     * @return new mutated individual
     */
    public Individual<int[]> mutate(Individual<int[]> indiv){
        int[] chromosome = problem.generateNeighbor(indiv.chromosome());
        return new Individual<>(chromosome, calcFitnessScore(chromosome));
    }

    //The fitness score for NQueens problem is the number of pairs of non-attacking queens,
    //which is the total number of pairs minus the number of attacking pairs.
    //The total number of pairs of queens is N choose two, which is N*(N-1)/2
    /**
     * Calculate the fitness score of a chromosome.
     *
     * The fitness score for NQueens problem is the number of pairs of non-attacking queens,
     * which is the total number of pairs minus the number of attacking pairs.
     * The total number of pairs of queens is N choose two, which is N*(N-1)/2
     *
     * @param chromosome chromosome to evaluate
     * @return fitness score
     */
    public double calcFitnessScore(int[] chromosome){
        return problem.getN()*(problem.getN()-1)/(double)2 - problem.cost(chromosome);
    }

    /**
     * Generate an initial population of individuals.
     *
     * @param popSize number of individuals in the population
     * @return list of individuals
     */
    public List<Individual<int[]>> generateInitPopulation(int popSize){
        List<Individual<int[]>> population = new ArrayList<>(popSize);
        for(int i=0; i<popSize; i++){
            int[] chromosome = problem.getInitState();
            population.add(new Individual<>(chromosome, calcFitnessScore(chromosome)));
        }
        return population;
    }

    /**
     * Main method for testing the genetic algorithm on N-Queens.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        int MAX_GEN = 100;
        double MUTATION_RATE = 0.1;
        int POPULATION_SIZE = 1000;
        double ELITISM = 0.2;
        int SIZE = 8; //number of queens

        NQueens problem = new NQueens(SIZE);
        GA_NQueens agent =
                new GA_NQueens(MAX_GEN,MUTATION_RATE,ELITISM,problem);
        Individual<int[]> best =
                agent.evolve(agent.generateInitPopulation(POPULATION_SIZE));
        System.out.println("Best solution:");
        problem.printState(best.chromosome());
        System.out.println("Best cost (# of attacking pairs) is: "+problem.cost(best.chromosome()));
    }


}
