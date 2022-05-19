package MFEA_TSP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
	public static int size = 10;
	private ArrayList<Individuals> population;
	private ArrayList<Double> p;
	private Individuals individuals = new Individuals();
	Random random = new Random();

	public void init() throws Exception {
		population = new ArrayList<>();
		for (int i = 0; i < Population.size; i++) {
			Individuals gen = individuals.create();
			this.population.add(gen);
		}
		individuals.readData(null);
	}

	public void validate() {
		for (int i = 0; i < Population.size; i++) { //set factorial cost
			ArrayList<Double> list = new ArrayList<>();
			list.add(individuals.fitnessTSP(this.getPopulation().get(i)));
			list.add(individuals.fitnessTRP(this.population.get(i), 1));
			this.population.get(i).setFactCost(list);
		}
		
		
	}

	public void crossoverAmutation(double rc, double rm) {
		while (this.population.size() < Population.size * 2) {
			double x = random.nextDouble();

			if (x < rc) {
				int h = 0, k = 0;
				x = random.nextDouble();
				for (int i = 1; i < Population.size; i++) {
					if (x > this.getP().get(i - 1) && x <= this.getP().get(i)) {
						h = i;
						break;
					}
				}
				x = random.nextDouble();
				for (int i = 1; i < Population.size; i++) {
					if (x > this.getP().get(i - 1) && x <= this.getP().get(i)) {
						k = i;
						break;
					}
				}
				Individuals individuals1 = this.population.get(h);
				Individuals individuals2 = this.population.get(k);
				List<Individuals> list = individuals.crossover(individuals1, individuals2);
				this.population.add(list.get(0));
				this.population.add(list.get(1));
			} else {
				x = random.nextDouble();
				if (x < rm) {
					int h = random.nextInt(Population.size - 1);
					Individuals individual1 = individuals.mutation(this.population.get(h));
					this.population.add(individual1);
				}
			}
		}

	}

	public void calRouteWheel() {
		ArrayList<Double> arrayList = new ArrayList<>();
		ArrayList<Double> prob = new ArrayList<>();
		double sum = 0.0;
		for (int i = 0; i < Population.size; i++) {
			arrayList.add(1.0 / individuals.fitnessTSP(this.getPopulation().get(i)));
			sum += 1.0 / individuals.fitnessTSP(this.getPopulation().get(i));
		}
		prob.add(arrayList.get(0) / sum);
		for (int i = 1; i < Population.size; i++)
			prob.add(arrayList.get(i) / sum + prob.get(i - 1));
		this.p = prob;
		System.out.print(this.getP().get(Population.size - 1));
		System.out.print(" " + this.getP().get(Population.size - 2));

	}

	public ArrayList<Double> getP() {
		return p;
	}

	public void setP(ArrayList<Double> p) {
		this.p = p;
	}

	public ArrayList<Individuals> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Individuals> population) {
		this.population = population;
	}

	public static void main(String args[]) throws Exception {
		Population population = new Population();
		population.init();
		population.calRouteWheel();
	}

}
