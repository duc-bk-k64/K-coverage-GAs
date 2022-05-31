package MFEA_TSP_KP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Individuals {
	public static ArrayList<Point> dataTSP;
	public static ArrayList<Double> dataKP_W;
	public static ArrayList<Double> dataKP_V;
	public static int size;
	public static int sizeTSP = 51;
	public static int sizeKP = 33;
	public static int capacityKP = 524;
	private ArrayList<Double> chromosome;
	public static Point point = new Point();
	public static KP kp = new KP();
	public static Random random = new Random();
	private ArrayList<Double> FactCost;
	private ArrayList<Integer> Rank;
	private double ScalarFitness;
	private int SkilFactor;
	public static double alpha = 0.15;

	public ArrayList<Double> getChromosome() {
		return chromosome;
	}

	public ArrayList<Double> getFactCost() {
		return FactCost;
	}

	public void setFactCost(ArrayList<Double> factCost) {
		FactCost = factCost;
	}

	public ArrayList<Integer> getRank() {
		return Rank;
	}

	public void setRank(ArrayList<Integer> rank) {
		Rank = rank;
	}

	public double getScalarFitness() {
		return ScalarFitness;
	}

	public void setScalarFitness(double scalarFitness) {
		ScalarFitness = scalarFitness;
	}

	public int getSkilFactor() {
		return SkilFactor;
	}

	public void setSkilFactor(int skilFactor) {
		SkilFactor = skilFactor;
	}

	public void setChromosome(ArrayList<Double> chromosome) {
		this.chromosome = chromosome;
	}

	public static void readData(String urlTSP, String urlKP, String urlKP_Value) throws Exception {
		Individuals.dataTSP = point.readData(urlTSP);
		Individuals.dataKP_W = kp.readData(urlKP);
		Individuals.dataKP_V = kp.readValue(urlKP_Value);
	}

	public static Individuals create() {
		Individuals individuals = new Individuals();
		ArrayList<Double> list = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++) {
			double s = random.nextDouble();
			list.add(s);
		}
		individuals.setChromosome(list);
		individuals.setSkilFactor(0);
		return individuals;
	}

	public static void size() {
		if (Individuals.sizeTSP > Individuals.sizeKP)
			Individuals.size = Individuals.sizeTSP;
		else
			Individuals.size = Individuals.sizeKP;
		System.out.print(Individuals.size);
	}

	public Individuals crossover(Individuals p) {
		ArrayList<Double> list1 = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++) {
			double range = this.getChromosome().get(i) - p.getChromosome().get(i);
			if (range < 0.0) {
				range = -range;
				double min = this.getChromosome().get(i) - Individuals.alpha * range;
				double max = p.getChromosome().get(i) + Individuals.alpha * range;
//				if (min < 0.0)
//					min = 0.0;
//				if (max > 1.0)
//					max = 1.0;
				double result = random.nextDouble() * (max - min) + min;
				list1.add(result);
			} else {
				double min = p.getChromosome().get(i) - Individuals.alpha * range;
				double max = this.getChromosome().get(i) + Individuals.alpha * range;
//				if (min < 0.0)
//					min = 0.0;
//				if (max > 1.0)
//					max = 1.0;
				double result = random.nextDouble() * (max - min) + min;
				list1.add(result);
			}
		}
		Individuals child1 = new Individuals();
		child1.setChromosome(list1);

		return child1;
	}

	public Individuals mutation() {
		Individuals individuals = new Individuals();
		ArrayList<Double> list = new ArrayList<>();
		for (int i = 0; i < Individuals.size; i++)
			list.add(this.getChromosome().get(i));
		int h = random.nextInt(size - 1);
		int k = random.nextInt(size - 1);
		Collections.swap(list, h, k);
		individuals.setChromosome(list);
		return individuals;
	}

	public int compare(Individuals p) {
		double tsp = this.fitnessTSP();
		double kp = this.fitnessKP();
		if (tsp < p.getFactCost().get(0) || kp > p.getFactCost().get(1))
			return 1;
		return 0;
	}

	public double fitnessTSP() {
		ArrayList<Double> list = new ArrayList<>();
		for (int i = 0; i < Individuals.sizeTSP; i++) {
			list.add(this.getChromosome().get(i));
		}
		Collections.sort(list, new Comparator<Double>() {
			@Override
			public int compare(Double x, Double y) {
				if (x > y)
					return -1;
				else if (x == y)
					return 0;
				else
					return 1;
			}
		});
		ArrayList<Integer> cycle = new ArrayList<>();
		for (int i = 0; i < Individuals.sizeTSP; i++) {
			for (int j = 0; j < Individuals.sizeTSP; j++) {
				if (this.getChromosome().get(i) == list.get(j)) {
					cycle.add(j + 1);
					break;
				}

			}
		}
//		for(int i=0;i<Individuals.sizeTSP;i++) System.out.print(cycle.get(i)+" ");
//		System.out.println("====="+cycle.size());
		double sum = 0.0;
		for (int i = 0; i < Individuals.sizeTSP - 1; i++) {
			sum += point.distance(dataTSP.get(cycle.get(i)), dataTSP.get(cycle.get(i + 1)));
		}
		sum += point.distance(dataTSP.get(cycle.get(Individuals.sizeTSP - 1)), dataTSP.get(cycle.get(0)));
		// System.out.println(sum);
		return sum;

	}

	public double fitnessKP() {
		double sumValue = 0.0;
		double sumWeight = 0.0;
		for (int i = 0; i < Individuals.sizeKP; i++) {
			if (this.chromosome.get(i) >= 0.5) {
				sumValue += dataKP_V.get(i + 1);
				sumWeight += dataKP_W.get(i + 1);
				// System.out.println(i+1);
			}
		}
		// System.out.println("Value:"+sumValue+" Weight:"+sumWeight);
		if (sumWeight < Individuals.capacityKP)
			return sumValue;
		else
			return -sumValue;

	}

	public void printKP() {
		for (int i = 0; i < Individuals.sizeKP; i++)
			if (this.getChromosome().get(i) >= 0.5)
				System.out.print(i + " ");
	}

	public void print() {
		System.out.print(this.getFactCost().get(0) + " " + this.getFactCost().get(1));
		System.out.println("---------------------");
	}

	public static void main(String args[]) throws Exception {
		Individuals.size();
		Individuals.readData("D:/eil51.tsp.txt", "D:/p02_w.txt", "D:/p02_s.txt");
		Individuals gen1 = Individuals.create();
		Individuals gen2 = Individuals.create();
		System.out.println(gen1.fitnessTSP() + " " + gen1.fitnessKP());
		System.out.println(gen2.fitnessTSP() + " " + gen2.fitnessKP());
		gen1.crossover(gen2).getChromosome().forEach(value -> {
			System.out.print(value + " ");
		});
		System.out.println();
		System.out.println(gen1.mutation().fitnessTSP() + " " + gen1.mutation().fitnessKP());
		System.out.println(gen1.fitnessTSP() + " " + gen1.fitnessKP());
		System.out.println(gen2.fitnessTSP() + " " + gen2.fitnessKP());

	}

}
