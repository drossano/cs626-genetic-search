package solutions;

import core_algorithms.GeneticAlgorithm;
import core_algorithms.Individual;
import problems.TSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import javax.print.DocFlavor.INPUT_STREAM;

/**
 * Apply the genetic algorithm to solve the Traveling Salesperson
 * Problem (TSP).
 *
 * This class defines how reproduction, mutation, and initial
 * population generation are adapted for solving TSP instances.
 */
public class GA_TSP extends GeneticAlgorithm<List<Integer>> {
        // Random number generator
    private static final Random RANDOM = new Random();
    private final TSP problem;

    /**
     * Constructor for GA_TSP.
     *
     * @param maxGen Maximum number of generations
     * @param mRate Mutation rate
     * @param elitism Elitism rate
     * @param problem The TSP problem instance
     */
    public GA_TSP(int maxGen, double mRate, double elitism, TSP problem){
        super(maxGen, mRate, elitism);
        this.problem = problem;
    }

    /**
     * Crossover two parent tours to produce a new offspring tour.
     *
     * You should implement "ordered crossover":
     *  - First, select a random segment from parent 1's chromosome, by
     *    Randomly picking a start position and an end position (inclusive)
     *  - Next, copy this segment from Parent 1's chromosome directly into
     *    the child's chromosome.
     *  - Last, fill the remaining genes (i.e., cities) from Parent 2,
     *    in the order they appear in Parent 2's chromosome, skipping any
     *    cities already in the child.
     *
     * Note: This method must NOT modify the parent individuals.
     *
     * @param p Parent 1
     * @param q Parent 2
     * @return New offspring individual
     */
    @Override
    public Individual<List<Integer>> crossover(Individual<List<Integer>> p,
                                               Individual<List<Integer>> q){
        // Select a random portion of parent1's chromosome by
        // randomly generating the start & the end positions.
        Random r = new Random();
        int startPos = r.nextInt(p.chromosome().size());
        int endPos = r.nextInt(p.chromosome().size());
        // make sure startPos <= endPos
        // If startPos is greater than endPos, swap them.
        if (startPos > endPos) {
            int t = startPos;
            startPos = endPos;
            endPos = t;
        }
        //First, copy the entire parent 1 chromosome to child.
        List<Integer> childChromosome = new ArrayList<>();
        List<Integer> crossOvIntegers = new ArrayList<>();

        for (int i = startPos; i <= endPos; i++) {
            crossOvIntegers.add(p.chromosome().get(i));
        }

        //Next, copy those genes before startPos and after endPos from parent 2 chromosome to child.
        for (Integer gene : q.chromosome()) {
            if (crossOvIntegers.contains(gene) == false) {
                childChromosome.add(gene);
            }
        }
        childChromosome.addAll(startPos,crossOvIntegers);
        return new Individual<>(childChromosome, calcFitnessScore(childChromosome));
    }

    /**
     * Mutate an individual tour by making a small random change.
     *
     * You should perform "swap mutation":
     * - Randomly select two different cities in the tour.
     * - Swap their positions to create a slightly different tour.
     *
     * Hint: you can call generateNeighbor() from the TSP problem
     * instance to perform the random swapping automatically.
     * (This will generate a neighbor by swapping two cities.)
     *
     * This mutation helps maintain diversity in the population.
     *
     * @param indiv Individual to mutate
     * @return New mutated individual
     */
    @Override
    public Individual<List<Integer>> mutate(Individual<List<Integer>> indiv){
        List<Integer> chromosome = problem.generateNeighbor(indiv.chromosome());
        return new Individual<>(chromosome, calcFitnessScore(chromosome));
    }

    /**
     * Calculates the fitness score for a tour.
     *
     * Fitness is defined as the inverse of the total tour distance,
     * since GeneticAlgorithm assumes higher fitness is better.
     *
     * @param chromosome The tour to evaluate
     * @return Fitness score
     */
    @Override
    public double calcFitnessScore(List<Integer> chromosome){
        return 1/(double)problem.cost(chromosome);
    }


    /**
     * Generates the initial population of random tours.
     *
     * Each individual is a random permutation of the cities.
     *
     * @param popSize Number of individuals in the population
     * @param numCities Number of cities in the TSP
     * @return List of individuals forming the initial population
     */
    public List<Individual<List<Integer>>> generateInitPopulation(int popSize,
                                                                  int numCities){
        List<Individual<List<Integer>>> population = new ArrayList<>(popSize);
        for(int i=0; i<popSize; i++){
            List<Integer> chromosome = new ArrayList<>(numCities);
            for(int j=0; j<numCities; j++) {
                chromosome.add(j);
            }
            Collections.shuffle(chromosome);
            Individual<List<Integer>> indiv =
                    new Individual<>(chromosome, calcFitnessScore(chromosome));
            population.add(indiv);
        }
        return population;
    }

    /**
     * Runs the Genetic Algorithm on a list of TSP distance matrices as test cases.
     *
     * Adjusts algorithm parameters depending on problem size.
     *
     * @param distanceMatrices List of distance matrices for different TSP instances
     * @return List of best tour distances found for each test case
     */
    public static List<Integer> runTestCases(List<int[][]> distanceMatrices) {
        int MAX_GEN = 50;
        int POPULATION_SIZE = 500;
        double MUTATION_RATE = 0.1;
        double ELITISM = 0.2;

        List<Integer> results = new ArrayList<>();

        for(int i=0; i<distanceMatrices.size(); i++) {
            int[][] dMatrix = distanceMatrices.get(i);
            // Adjust GA parameters for larger problems
            if (dMatrix.length >= 15){
                MAX_GEN =80;
                POPULATION_SIZE = 1500;
            }
            if (dMatrix.length >= 20) {
                MAX_GEN = 180;
                POPULATION_SIZE = 2500;
            }
            if (dMatrix.length >= 25) {
                MAX_GEN = 200;
                POPULATION_SIZE = 3000;
            }

            TSP problem = new TSP(dMatrix);
            GA_TSP agent =
                    new GA_TSP(MAX_GEN, MUTATION_RATE, ELITISM, problem);
            System.out.println(dMatrix.length + " cities:");
            Individual<List<Integer>> best =
                    agent.evolve(agent.generateInitPopulation(POPULATION_SIZE,
                            dMatrix.length));
            System.out.print("Best solution: ");
            System.out.println(best.chromosome());
            System.out.println("total distance is: " +
                    problem.cost(best.chromosome()));
            System.out.println();
            results.add(problem.cost(best.chromosome()));
        }
        return results;
    }

    /**
     * Reads TSP test cases from text files and runs the Genetic Algorithm on them.
     *
     * @param args Command-line arguments (not used)
     * @throws Exception If file reading fails
     */
    public static void main(String[] args) throws Exception {
        System.out.println("file loc");
        System.out.println(new File(".").getAbsoluteFile());
        int COMMENT_LINES = 3;
        String FOLDER_PATH = "LocalSearch/TSP_Test_Cases/";
        String fileName = "TSP_";
        int[] numCities = {5,10,15,20, 25};
        List<int[][]> distanceMatrices = new ArrayList<>();
        for (int i = 0; i < numCities.length; i++) {
            List<int[]> rows = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(
                    FOLDER_PATH + fileName + numCities[i] + ".txt"))) {
                // Skip the comment lines
                for (int j = 0; j < COMMENT_LINES; j++) {
                    br.readLine();
                }
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue; // Skip any empty lines
                    }
                    String[] tokens = line.trim().split("\\s+");
                    int[] row = new int[tokens.length];
                    for (int j = 0; j < tokens.length; j++) {
                        row[j] = Integer.parseInt(tokens[j]);
                    }
                    rows.add(row);
                }
                int[][] dMatrix = new int[rows.size()][rows.size()];
                for (int k = 0; k < rows.size(); k++) {
                    dMatrix[k] = rows.get(k);
                }
                distanceMatrices.add(dMatrix);
            }
        }
        runTestCases(distanceMatrices);
    }
}

