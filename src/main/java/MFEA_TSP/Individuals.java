package MFEA_TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Individuals {
	public static int size = 4;
	public static ArrayList<Point> data;
	private ArrayList<Integer> chromosome;
	public Point point = new Point();
	Random random = new Random();
	private ArrayList<Double> FactCost;
	private ArrayList<Integer> rank;
	private double Scalarfitness;
	private int SkillFactor;
	
	public ArrayList<Double> getFactCost() {
		return FactCost;
	}

	public void setFactCost(ArrayList<Double> factCost) {
		FactCost = factCost;
	}

	public ArrayList<Integer> getRank() {
		return rank;
	}

	public void setRank(ArrayList<Integer> rank) {
		this.rank = rank;
	}

	public double getScalarfitness() {
		return Scalarfitness;
	}

	public void setScalarfitness(double scalarfitness) {
		Scalarfitness = scalarfitness;
	}

	public int getSkillFactor() {
		return SkillFactor;
	}

	public void setSkillFactor(int skillFactor) {
		SkillFactor = skillFactor;
	}

	public Individuals create() {
		ArrayList<Integer> litsArrayList = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++)
			litsArrayList.add(i);
		Collections.shuffle(litsArrayList);
		Individuals individuals = new Individuals();
		individuals.setChromosome(litsArrayList);
		return individuals;
	}

	public void readData(String url) throws Exception {
		// Individuals.data = point.readData(url);
		ArrayList<Point> list = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++) {
			Point point = new Point((double) (i), (double) i);
			list.add(point);
		}
		Point zPoint=new Point(1.0,0.0);
		list.add(zPoint);
		Individuals.data = list;
	}

	public List<ArrayList<Integer>> findCycle(Individuals p, Individuals q) {
		List<ArrayList<Integer>> cycle = new ArrayList<ArrayList<Integer>>();
		int[] inq = new int[Individuals.size + 1];
		for (int i = 0; i < Individuals.size + 1; i++)
			inq[i] = 0;
		int a = -999;
		int index = 0;
		for (int i = 0; i < Individuals.size; i++) {
			if (inq[p.getChromosome().get(i)] == 0) {
				ArrayList<Integer> list = new ArrayList<Integer>();
				index = i;
				inq[p.getChromosome().get(i)] = 1;
				while (true) {
					a = p.getChromosome().get(index);
					inq[p.getChromosome().get(index)] = 1;
					list.add(a);
					for (int j = 0; j < Individuals.size; j++) {
						if (q.getChromosome().get(j) == a) {
							index = j;
							break;
						}
					}

					if (inq[p.getChromosome().get(index)] == 1)
						break;
				}
				cycle.add(list);
			}
		}
		return cycle;
	}

	public List<Individuals> crossover(Individuals p, Individuals q) { // cycle crossover
		List<ArrayList<Integer>> list = new ArrayList<>();
		list = this.findCycle(p, q);
		ArrayList<Integer> child1 = new ArrayList<>();
		ArrayList<Integer> child2 = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++) {
			child1.add(0);
			child2.add(0);
		}
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Integer> data = list.get(i);
			if (i % 2 == 0) {
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Individuals.size; j++) {
						if (data.get(k) == p.getChromosome().get(j)) {
							child1.set(j, data.get(k));
						}
					}
				}
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Individuals.size; j++) {
						if (data.get(k) == q.getChromosome().get(j)) {
							child2.set(j, data.get(k));
						}
					}
				}
			} else {
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Individuals.size; j++) {
						if (data.get(k) == q.getChromosome().get(j)) {
							child1.set(j, data.get(k));
						}
					}
				}
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Individuals.size; j++) {
						if (data.get(k) == p.getChromosome().get(j)) {
							child2.set(j, data.get(k));
						}
					}
				}
			}
		}
		Individuals individuals = new Individuals();
		individuals.setChromosome(child1);
		Individuals individuals2 = new Individuals();
		individuals2.setChromosome(child2);
		List<Individuals> listIndividuals = new ArrayList<>();
		listIndividuals.add(individuals);
		listIndividuals.add(individuals2);
		return listIndividuals;
	}

	public Individuals mutation(Individuals individuals) {
		int r = random.nextInt(Individuals.size - 1);
		int s = random.nextInt(Individuals.size - 1);
		ArrayList<Integer> chromosome = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++)
			chromosome.add(individuals.getChromosome().get(i));
		Collections.swap(chromosome, r, s);
		Individuals child = new Individuals();
		child.setChromosome(chromosome);
		return child;
	}

	public double fitnessTSP(Individuals p) {
		double sum = 0.0;
		for (int i = 1; i < Individuals.size; i++) {
			double distance = point.distance(data.get(p.getChromosome().get(i)),
					data.get(p.getChromosome().get(i - 1)));
			sum += distance;
		}
		double distance = point.distance(data.get(p.getChromosome().get(Individuals.size - 1)),
				data.get(p.getChromosome().get(0)));
		sum += distance;
		return sum;
	}

	public double fitnessTRP(Individuals p, int x) { // x is first city
		double sum = 0.0;
		int t = 0;
		int position = 0;
		for (int i = 0; i < Individuals.size; i++) { // find index of city x
			if (p.getChromosome().get(i) == x) {
				position = i;
				break;
			}
		}
		double distance = 0.0;
		while (t != (Individuals.size - 1)) {
			t++;
			distance += point.distance(data.get(p.getChromosome().get(position)),
					data.get(p.getChromosome().get(increCycle(position + 1))));
			sum += distance;
			position = increCycle(position+1);
		}
		return sum / Individuals.size;

	}
	public List<Double> FactCost(Individuals p,int x) { //x is the first city
		Double tsp=this.fitnessTSP(p);
		Double trp=this.fitnessTRP(p,x);
		List<Double> list=new ArrayList<>();
		list.add(tsp);
		list.add(trp);
		return list;
	}
	
	public int increCycle(int x) {
		if (x <= Individuals.size)
			return x;
		else
			return x - Individuals.size;
	}

	public ArrayList<Integer> getChromosome() {
		return chromosome;
	}

	public void setChromosome(ArrayList<Integer> chromosome) {
		this.chromosome = chromosome;
	}

	public static void main(String args[]) throws Exception {
		ArrayList<Integer> p = new ArrayList<>();
		for (int i = 1; i <= Individuals.size; i++)
			p.add(i);
		Individuals individuals = new Individuals();
		individuals.setChromosome(p);
		Individuals zIndividuals = new Individuals();
		zIndividuals.readData(null);
		System.out.println(zIndividuals.fitnessTSP(individuals));
		System.out.println(zIndividuals.fitnessTRP(individuals, 1));

//		Individuals zIndividuals = new Individuals();
//		ArrayList<Integer> q = new ArrayList<>();
//		q.add(2);
//		q.add(4);
//		q.add(6);
//		q.add(3);
//		q.add(1);
//		q.add(5);
//		q.add(9);
//		q.add(8);
//		q.add(7);
//		Individuals individuals = new Individuals();
//		individuals.setChromosome(p);
//		Individuals individuals2 = new Individuals();
//		individuals2.setChromosome(q);
//		zIndividuals.findCycle(individuals, individuals2).forEach(list -> {
//			for (int i = 0; i < list.size(); i++)
//				System.out.print(" " + list.get(i));
//			System.out.println();
//		});
//		// zIndividuals.crossover(individuals, individuals2);
//		for (int i = 0; i < Individuals.size; i++) {
//			System.out.print(" " + individuals.getChromosome().get(i));
//		}
//		System.out.println();
//		for (int i = 0; i < Individuals.size; i++) {
//			System.out.print(" " + individuals2.getChromosome().get(i));
//		}
	}

}
