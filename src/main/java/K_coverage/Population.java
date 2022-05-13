package K_coverage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
	public ArrayList<Individual> population;
	public static int size = 60;
	public Individual individual = new Individual();
	public Random random = new Random();
	public ArrayList<Double> p;

	public void init() {
		ArrayList<Individual> list = new ArrayList<Individual>();
		for (int i = 0; i < size; i++)
			list.add(individual.create());
		this.population = list;
		individual.readData(null);

	}

	public void crossover(double pc) {
		for (int t = 0; t < size / 4; t++) {
			double x = random.nextDouble();
			if (x < pc) {
				x = random.nextDouble();
				int h = 0, k = 0; // h,k denote individual are selected

				for (int i = 1; i < size; i++) {
					if (x > this.p.get(i - 1) && x <= this.p.get(i))
						h = i;
				}

				x = random.nextDouble();

				for (int i = 1; i < size; i++) {
					if (x > this.p.get(i - 1) && x <= this.p.get(i))
						k = i;
				}

				Individual individual1 = this.population.get(h);
				Individual individual2 = this.population.get(k);
				this.population.add(individual1.crossover(individual2));
				this.population.add(individual2.crossover(individual1));
			}
		}

	}

	public void mutation(double pm) {
		for (int i = 0; i < size / 4; i++) {
			double x = random.nextDouble();
			if (x < pm) {
				int h = random.nextInt(size - 1);
				Individual individual1 = this.population.get(h);
				this.population.add(individual1.mutation());
			}
		}
	}

	public void caculateProbaly() {
		ArrayList<Double> probaly = new ArrayList<Double>();
		double sum = 0;
		for (int i = 0; i < this.population.size(); i++)
			sum += this.population.get(i).fitness();
		probaly.add(this.population.get(0).fitness() / sum);
		for (int i = 1; i < this.population.size(); i++) {
			probaly.add(probaly.get(i - 1) + this.population.get(i).fitness() / sum);
		}
		this.p = probaly;
	}

	public void select() {
		ArrayList<Individual> fesibale = this.fesibleFamilis();
		ArrayList<Individual> infesible = this.infesibleFamilis();
		Collections.sort(fesibale, new Comparator<Individual>() {
			public int compare(Individual p, Individual q) {
				Double a = p.fitness();
				Double b = q.fitness();
				if (a.compareTo(b) > 0)
					return -1;
				else if (a.compareTo(b) == 0)
					return 0;
				else
					return 1;
			}

		});
		Collections.sort(infesible, new Comparator<Individual>() {
			public int compare(Individual p, Individual q) {
				int NV1 = p.check();
				int NV2 = q.check();
				if (NV1 < NV2)
					return -1;
				else if (NV1 == NV2) {
					int CV1 = p.amount();
					int CV2 = q.amount();
					if (CV1 < CV2)
						return -1;
					else if (CV1 == CV2)
						return 0;
					else
						return 1;
				}

				else
					return 1;
			}

		});
		ArrayList<Individual> childPopulation = new ArrayList<Individual>(); // select best individual for child
																				// population
		if (fesibale.size() >= size - 2) {
			for (int i = 0; i < size - 2; i++)
				childPopulation.add(fesibale.get(i));
			childPopulation.add(infesible.get(0));
			childPopulation.add(infesible.get(1));
		} else {
			for (int i = 0; i < fesibale.size(); i++) {
				childPopulation.add(fesibale.get(i));
			}
			for (int i = 0; i < Population.size - fesibale.size(); i++) {
				childPopulation.add(infesible.get(i));
			}

		}

		this.population = childPopulation;

	}

	public ArrayList<Individual> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Individual> population) {
		this.population = population;
	}

	public Individual getBest() {
		Individual bestIndividual = this.population.get(0);
		double best = bestIndividual.fitness();
		for (int i = 0; i < this.population.size(); i++) {
			if (this.population.get(i).fitness() > best) {
				bestIndividual = this.population.get(i);
				best = bestIndividual.fitness();
			}
		}
		return bestIndividual;
	}

	public void print() {
		System.out.println("Size:" + this.population.size());
		for (int i = 0; i < this.population.size(); i++)
			this.population.get(i).print();
	}

	public void printCoverage() {
		System.out.println("Coverage:");
		for (int i = 0; i < Individual.N; i++) {
			System.out.print(this.getBest().Cov(i) + ", ");
		}
		System.out.println();
	}

	public void printConnected() {
		System.out.println("Conected:");
		for (int i = 0; i < Individual.K; i++) {
			if (this.getBest().getChromosome().get(i) != 0) {
				System.out.print(this.getBest().Com(i) + ", ");
			}
		}
		System.out.println();

	}

	public ArrayList<Individual> fesibleFamilis() {
		ArrayList<Individual> list = new ArrayList<Individual>();
		for (int i = 0; i < this.population.size(); i++) {
			if (this.population.get(i).check() == 0) {
				list.add(this.population.get(i));
			}
		}
		return list;
	}

	public ArrayList<Individual> infesibleFamilis() {
		ArrayList<Individual> list = new ArrayList<Individual>();
		for (int i = 0; i < this.population.size(); i++) {
			if (this.population.get(i).check() != 0) {
				list.add(this.population.get(i));
			}
		}
		return list;
	}

}
