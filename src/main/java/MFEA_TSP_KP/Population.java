package MFEA_TSP_KP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Population {
	public static int size = 100;
	private ArrayList<Individuals> population;
	private ArrayList<Double> p;
	Random random = new Random();

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

	public void init() throws Exception {
		Individuals.size();
		Individuals.readData("D:/eil51.tsp.txt", "D:/p04_w.txt", "D:/p04_s.txt");
		ArrayList<Individuals> list = new ArrayList<>();
		for (int i = 0; i < Population.size; i++) {
			list.add(Individuals.create());
		}
		this.population = list;
	}

	public void validate() {
		for (int i = 0; i < this.population.size(); i++) {
			Individuals individuals = this.population.get(i);
			ArrayList<Double> list = new ArrayList<>();
			if (individuals.getSkilFactor() == 0) {
				list.add(individuals.fitnessTSP());
				list.add(individuals.fitnessKP());
			} else if (individuals.getSkilFactor() == 1) {
				list.add(individuals.fitnessTSP());
				list.add(-9999.0);
			} else {
				list.add(9999.0);
				list.add(individuals.fitnessKP());
			}
			individuals.setFactCost(list);
		}
		Collections.sort(this.population, new Comparator<Individuals>() { // sort by fitness tsp
			@Override
			public int compare(Individuals p, Individuals q) {
				double a = p.getFactCost().get(0);
				double b = q.getFactCost().get(0);
				if (a > b)
					return 1;
				else if (a == b)
					return 0;
				return -1;
			}
		});
		for (int i = 0; i < this.population.size(); i++) { // set rank TSP
			ArrayList<Integer> list = new ArrayList<>();
			list.add(i + 1);
			this.population.get(i).setRank(list);
		}
		Collections.sort(this.population, new Comparator<Individuals>() { // sort by fitness kp
			@Override
			public int compare(Individuals p, Individuals q) {
				double a = p.getFactCost().get(1);
				double b = q.getFactCost().get(1);
				if (a > b)
					return -1;
				else if (a == b)
					return 0;
				return 1;
			}
		});
		for (int i = 0; i < this.population.size(); i++) { // set rank kp
			ArrayList<Integer> list = this.population.get(i).getRank();
			list.add(i + 1);
			this.population.get(i).setRank(list);
		}
		for (int i = 0; i < this.population.size(); i++) { // caculate scalarfitness
			double result = 1.0;
			ArrayList<Integer> list = this.population.get(i).getRank();
			if (this.population.get(i).getSkilFactor() == 0) {
				if (list.get(0) > list.get(1))
					result = result / list.get(1);
				else
					result = result / list.get(0);
			} else if (this.population.get(i).getSkilFactor() == 1)
				result = result / list.get(0);
			else
				result = result / list.get(1);
			this.population.get(i).setScalarFitness(result);
		}
		for (int i = 0; i < this.population.size(); i++) {
			ArrayList<Integer> list = this.population.get(i).getRank();
			if (list.get(0) > list.get(1))
				this.population.get(i).setSkilFactor(2);
			else
				this.population.get(i).setSkilFactor(1);
		}

	}

	public void crossoverAmutation(double pc) {
		while (this.population.size() < Population.size * 2) {
			int h = 0, k = 0;
			double x = random.nextDouble();
			for (int i = 1; i < Population.size; i++) { // get individuals by route whells.
				if (x > this.getP().get(i - 1) && x <= this.getP().get(i)) {
					h = i;
					break;
				}
			}
			x = random.nextDouble();
			for (int i = 1; i < Population.size; i++) { // get individuals by route whells.
				if (x > this.getP().get(i - 1) && x <= this.getP().get(i)) {
					k = i;
					break;
				}
			}
			Individuals individuals1 = this.population.get(h);
			Individuals individuals2 = this.population.get(k);
			x = random.nextDouble();
			if (x < pc || individuals1.getSkilFactor() == individuals2.getSkilFactor()) {
				Individuals individuals = individuals1.crossover(individuals2);
				Individuals individuals3 = individuals2.crossover(individuals1);
				h = random.nextInt();
				if (h % 2 == 0) {
					individuals.setSkilFactor(1);
					individuals3.setSkilFactor(1);
				} else {
					individuals.setSkilFactor(2);
					individuals3.setSkilFactor(2);
				}
				if (individuals.compare(individuals1) == 1 && individuals.compare(individuals2) == 1)
					this.population.add(individuals);
				if (individuals3.compare(individuals2) == 1 && individuals3.compare(individuals1) == 1)
					this.population.add(individuals3);

			} else {
				Individuals child1 = individuals1.mutation();
				child1.setSkilFactor(individuals1.getSkilFactor());
				Individuals child2 = individuals2.mutation();
				child2.setSkilFactor(individuals2.getSkilFactor());
				if (child1.compare(individuals1) == 1)
					this.population.add(child1);
				if (child2.compare(individuals2) == 1)
					this.population.add(child2);
			}

		}
	}

	public void calRouteWheel() {
		ArrayList<Double> arrayList = new ArrayList<>();
		ArrayList<Double> prob = new ArrayList<>();
		double sum = 0.0;
		for (int i = 0; i < Population.size; i++) {
			arrayList.add(1.0 / this.population.get(i).fitnessTSP()); // add fitness
			sum += arrayList.get(i); // caculate sum of fitness
		}
		prob.add(arrayList.get(0) / sum);
		for (int i = 1; i < Population.size; i++) // caculate route wheel
			prob.add(arrayList.get(i) / sum + prob.get(i - 1));
		this.p = prob;
	}

	public void select() {
		Collections.sort(this.population, new Comparator<Individuals>() {

			@Override
			public int compare(Individuals o1, Individuals o2) {
				double a = o1.getScalarFitness();
				double b = o2.getScalarFitness();
				if (a > b)
					return -1;
				else if (a == b)
					return 0;
				return 1;
			}
		});
		ArrayList<Individuals> list = new ArrayList<>();
		for (int i = 0; i < Population.size; i++)
			list.add(this.population.get(i));
		this.population = list;
	}

	public Individuals getbestTSP() {
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
//				double a = individuals.fitnessTSP(p);
//				double b = individuals.fitnessTSP(q);
				double a = p.getFactCost().get(0);
				double b = q.getFactCost().get(0);
				if (a > b)
					return 1;
				else if (a == b)
					return 0;
				else
					return -1;
			}
		});
		return this.population.get(0);
	}

	public Individuals getnestKP() {
		Collections.sort(this.population, new Comparator<Individuals>() {
			public int compare(Individuals p, Individuals q) {
//				double a = individuals.fitnessTSP(p);
//				double b = individuals.fitnessTSP(q);
				double a = p.getFactCost().get(1);
				double b = q.getFactCost().get(1);
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

	public static void main(String args[]) throws Exception {
		Population population = new Population();
		population.init();
		population.validate();
		for (int i = 0; i < 100; i++) {
			population.calRouteWheel();
			population.crossoverAmutation(0.8);
			population.select();
			population.getbestTSP().print();
			population.getnestKP().print();
			System.out.println("===========");
		}
	}
//		population.getPopulation().forEach(individuals -> {
//			individuals.getFactCost().forEach(value -> {
//				System.out.print(value + " ");
//			});
//			individuals.getRank().forEach(value -> {
//				System.out.print(value + " ");
//			});
//			System.out.print(individuals.getScalarFitness() + " " + individuals.getSkilFactor());
//			System.out.println();
//		});
//		System.out.println("==============");
//		population.calRouteWheel();
//		population.crossoverAmutation(0.8);
//		population.validate();
//		population.select();
//		population.getPopulation().forEach(individuals -> {
//			individuals.getFactCost().forEach(value -> {
//				System.out.print(value + " ");
//			});
//			individuals.getRank().forEach(value -> {
//				System.out.print(value + " ");
//			});
//			System.out.print(individuals.getScalarFitness() + " " + individuals.getSkilFactor());
//			System.out.println();
//		});
//		population.getbestTSP().print();;
//		population.getnestKP().print();
//
//	}

}
