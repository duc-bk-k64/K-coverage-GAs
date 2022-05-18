package TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
	public static int size = 200;
	private ArrayList<Chromosome> population;
	private Chromosome chromosome = new Chromosome();
	private Random random = new Random();

	public Population() {
	}

	public Population(ArrayList<Chromosome> population) {
		this.population = population;
	}

	public void inti() throws Exception {
		ArrayList<Chromosome> listChromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < Population.size; i++) {
			listChromosomes.add(chromosome.create());
		}
		this.population = listChromosomes;
		chromosome.loadVertex();
	}

	public void crossover(double rc, double rm) {
		// for (int i = 0; i < size / 4; i++) {
		while (this.population.size() < 2 * Population.size) {
			double pc = random.nextDouble();
			if (pc < rc) {
				int h = random.nextInt(Population.size - 1);
				int k = random.nextInt(Population.size - 1);
				Chromosome childChromosome1 = chromosome.crossoverCycle(this.population.get(h), this.population.get(k));
				h = random.nextInt(Population.size - 1);
				k = random.nextInt(Population.size - 1);
				Chromosome childChromosome2 = chromosome.crossoverCycle(this.population.get(k), this.population.get(h));
				if (childChromosome1.getVertex().size() != 0 && childChromosome2.getVertex().size() != 0) {
					this.population.add(childChromosome1);
					this.population.add(childChromosome2);
				}
			} else {

				double pm = random.nextDouble();
				if (pm < rm) {
					int h = random.nextInt(Population.size - 1);
					Chromosome childChromosome = chromosome.mutation(this.population.get(h));
					if (childChromosome.getVertex().size() != 0) {
						this.population.add(childChromosome);
					}
				}
			}

		}
	}



	public Chromosome getBest() {
		Chromosome bestChromosome = this.population.get(0);
//		double best = chromosome.fitness(bestChromosome);
//		for (int i = 1; i < this.population.size(); i++) {
//			if (chromosome.fitness(this.population.get(i)) > best) {
//				bestChromosome = this.population.get(i);
//				best = chromosome.fitness(bestChromosome);
//			}
//		}
		return bestChromosome;
	}

	public void select() {
		Collections.sort(this.population, new Comparator<Chromosome>() {
			public int compare(Chromosome p, Chromosome q) {
				Double f1 = p.fitness(p);
				Double f2 = q.fitness(q);
				if (f1.compareTo(f2) > 0)
					return -1;
				else if (f1.compareTo(f2) == 0)
					return 0;
				else
					return 1;
			}
		});
		ArrayList<Chromosome> childPopulation = new ArrayList<Chromosome>();
		for (int i = 0; i < Population.size / 2; i++) {
			childPopulation.add(this.population.get(i));
		}
		while (childPopulation.size() < Population.size) {
			int x = random.nextInt(2 * Population.size - 1);
			int y = random.nextInt(2 * Population.size - 1);
			Chromosome gen1 = this.population.get(x);
			Chromosome gen2 = this.population.get(y);
			if (chromosome.fitness(gen1) > chromosome.fitness(gen2))
				childPopulation.add(gen1);
			else
				childPopulation.add(gen2);
		}
		this.population = childPopulation;

	}

	public ArrayList<Chromosome> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Chromosome> population) {
		this.population = population;
	}

	public void printPopulation() {
		System.out.println(this.population.size());
		for (int i = 0; i < this.population.size(); i++) {
			System.out.print((i + 1) + ":");
			this.population.get(i).printf();
			System.out.println(chromosome.fitness(this.population.get(i)));
		}

	}

//	public static void main(String args[]) throws Exception {
//		Population population = new Population();
//		Chromosome chromosome = new Chromosome();
//		population.inti();
//		for (int k = 0; k < 100; k++) {
//			population.crossover(0.9);
//			population.mutation(0.5);
//			// population.printPopulation();
//			System.out.println("BEST CHROMOSOME:");
//			population.getBest().printf();
//			System.out.println(chromosome.fitness(population.getBest()));
//			population.select();
//
//		}
//	}
}
