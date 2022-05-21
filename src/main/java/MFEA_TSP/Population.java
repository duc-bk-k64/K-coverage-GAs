package MFEA_TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Population {
	public static int size = 60;
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
		individuals.readData("D:/eil51.tsp.txt");
	}

	public void validate(int x) {
		for (int i = 0; i < this.population.size(); i++) { // set factorial cost
			ArrayList<Double> list = new ArrayList<>();
			list.add(individuals.fitnessTSP(this.population.get(i)));
			list.add(individuals.fitnessTRP(this.population.get(i), 1));
			this.population.get(i).setFactCost(list);
		}
		Collections.sort(this.population, new Comparator<Individuals>() { // caculate rank for TSP
			public int compare(Individuals p, Individuals q) {
				double a = individuals.fitnessTSP(p);
				double b = individuals.fitnessTSP(q);
				if (a > b)
					return 1;
				else if (a == b)
					return 0;
				else
					return -1;
			}
		});
		for (int i = 0; i < this.population.size(); i++) {
			ArrayList<Integer> list = new ArrayList<>();
			list.add(i + 1);
			this.population.get(i).setRank(list);
		}
		Collections.sort(this.population, new Comparator<Individuals>() { // caculate rank for TRP
			public int compare(Individuals p, Individuals q) {
				double a = individuals.fitnessTRP(p, x);
				double b = individuals.fitnessTRP(q, x);
				if (a > b)
					return 1;
				else if (a == b)
					return 0;
				else
					return -1;
			}
		});
		for (int i = 0; i < this.population.size(); i++) {
			ArrayList<Integer> list = this.population.get(i).getRank();
			list.add(i + 1);
			this.population.get(i).setRank(list);
		}
		for (int i = 0; i < this.population.size(); i++) { // calutate Scalar-fitness
			Individuals individuals1 = this.population.get(i);
			double result = 1.0;
			ArrayList<Integer> list = individuals1.getRank();
			if (individuals1.getSkillFactor() == 0) {
				if (list.get(0) > list.get(1)) {
					result = result / list.get(1);
				} else
					result = result / list.get(0);
			} else if (individuals1.getSkillFactor() == 1) {
				result = result / list.get(0);
			} else {
				result = result / list.get(1);
			}
			individuals1.setScalarfitness(result);
		}
		for (int i = 0; i < this.population.size(); i++) {// caculate Skill-factor
			Individuals individuals1 = this.population.get(i);
			if (individuals1.getSkillFactor() == 0) {
				int result = 0;
				ArrayList<Integer> list = individuals1.getRank();
				if (list.get(0) > list.get(1)) {
					result = 2;
				} else
					result = 1;
				individuals1.setSkillFactor(result);
			}
		}

//		for (int i = 0; i < this.population.size(); i++) {
//			this.population.get(i).getFactCost().forEach(cost -> {
//				System.out.print(cost + " ");
//			});
//
//			this.population.get(i).getRank().forEach(rank -> {
//				System.out.print(rank + " ");
//			});
//			System.out.print(this.population.get(i).getScalarfitness() + " ");
//			System.out.print(this.population.get(i).getSkillFactor());
//			System.out.println();
//		}

	}

	public void print() {
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
				double a = p.getScalarfitness();
				double b = q.getScalarfitness();
				if (a > b)
					return -1;
				else if (a == b)
					return 0;
				return 1;
			}
		});
		for (int i = 0; i < this.population.size(); i++) {
			this.population.get(i).getFactCost().forEach(cost -> {
				System.out.print(cost + " ");
			});

			this.population.get(i).getRank().forEach(rank -> {
				System.out.print(rank + " ");
			});
			System.out.print(this.population.get(i).getScalarfitness() + " ");
			System.out.print(this.population.get(i).getSkillFactor());
			System.out.println();
		}

	}

	public void crossoverAmutation(double rc, double rm) {
		while (this.population.size() < Population.size * 2) {
			int h = 0, k = 0;
			double x = random.nextDouble();
			for (int i = 1; i < Population.size; i++) { // get individuals by route whells.
				if (x > this.getP().get(i - 1) && x <= this.getP().get(i)) {
					h = i;
					break;
				}
			}
			double y = random.nextDouble();
			for (int i = 1; i < Population.size; i++) {
				if (y > this.getP().get(i - 1) && y <= this.getP().get(i)) {
					k = i;
					break;
				}
			}
			// System.out.println(h + " " + k);
			Individuals individuals1 = this.population.get(h);
			Individuals individuals2 = this.population.get(k);
			x = random.nextDouble();

			if (x < rc || individuals1.getSkillFactor() == individuals2.getSkillFactor()) {

				List<Individuals> list = individuals.crossover(individuals1, individuals2);
				Individuals individuals3 = list.get(0);
				Individuals individuals4 = list.get(1);
				int rd = random.nextInt();
				if (rd % 2 == 0) {
					individuals3.setSkillFactor(individuals1.getSkillFactor()); // set skill factor for child random
					individuals4.setSkillFactor(individuals1.getSkillFactor());
				} else {
					individuals3.setSkillFactor(individuals2.getSkillFactor());
					individuals4.setSkillFactor(individuals2.getSkillFactor());
				}
				this.population.add(individuals3);
				this.population.add(individuals4);
			} else {
				x = random.nextDouble();
				if (x < rm) {
					Individuals individual3 = individuals.mutation(individuals1);
					individual3.setSkillFactor(individuals1.getSkillFactor());
					Individuals individual4 = individuals.mutation(individuals2);
					individual4.setSkillFactor(individuals2.getSkillFactor());

					this.population.add(individual3);
					this.population.add(individual4);
				}
			}
		}
//		System.out.println();

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

	}

	public void select() {
		ArrayList<Individuals> list = new ArrayList<>();
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
				double a = p.getScalarfitness();
				double b = q.getScalarfitness();
				if (a > b)
					return -1;
				else if (a == b)
					return 0;
				return 1;
			}
		});
		for (int i = 0; i < Population.size; i++)
			list.add(this.population.get(i));
		this.population = list;
	}

	public void RVND(int x) {
		Individuals individuals1 = this.getPopulation().get(0);
		Individuals individuals2 = individuals1.RVND(x);
		if (individuals2 != null) {
			individuals2.setSkillFactor(0);
			this.population.add(individuals2);
		}
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
				double a = individuals.fitnessTSP(p);
				double b = individuals.fitnessTSP(q);
				if (a > b)
					return 1;
				else if (a == b)
					return 0;
				else
					return -1;
			}
		});
		individuals1 = this.getPopulation().get(0);
		individuals2 = individuals1.RVND(x);
		if (individuals2 != null) {
			individuals2.setSkillFactor(0);
			this.population.add(individuals2);
		}

	}

	public Individuals getbest() {
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
//				Double a = individuals.fitnessTSP(p);
//				Double b = individuals.fitnessTSP(q);
				double a = p.getScalarfitness();
				double b = q.getScalarfitness();
				if (a > b)
					return -1;
				else if (a == b)
					return 0;
				else
					return 1;
			}
		});
		return this.population.get(0);

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
		population.validate(1);
		Individuals individuals = population.getbest();
		System.out.println(individuals.fitnessTSP(individuals) + " " + individuals.fitnessTRP(individuals, 1) + " "
				+ individuals.getScalarfitness());
		System.out.println("===================");
		for (int i = 0; i < 30; i++) {
			population.calRouteWheel();
			population.crossoverAmutation(0.8, 0.1);
			population.RVND(1);
		//	population.select();
			population.validate(1);
			population.select();
			individuals = population.getbest();
			System.out.println(individuals.fitnessTSP(individuals) + " " + individuals.fitnessTRP(individuals, 1) + " "
					+ individuals.getScalarfitness());

			System.out.println("===================");
			// population.print();
		}
		// population.getPopulation().forEach();
	}

}
