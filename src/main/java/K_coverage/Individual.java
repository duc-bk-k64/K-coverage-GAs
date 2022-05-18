package K_coverage;

import java.util.ArrayList;
import java.util.Random;

public class Individual {
	public static int K = 200; // number of potentiol position
	public static int N = 100; // number of targets
	private ArrayList<Integer> chromosome;
	public static ArrayList<Target> targets;
	public static ArrayList<Sensor> position;
	public static int k = 3; // k-coverage
	public static int m = 2; // m-connected
	public static int xmax = 300;
	public static int ymax = 300;

	public Individual() {

	}

	public ArrayList<Integer> getChromosome() {
		return chromosome;
	}

	public void setChromosome(ArrayList<Integer> chromosome) {
		this.chromosome = chromosome;
	}

	public Random random = new Random();

	public Individual(ArrayList<Integer> chromosome) {
		this.chromosome = chromosome;
	}

	public Individual create() {
		Double x;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < K; i++) {
			x = random.nextDouble();
			if (x > 0.3)
				list.add(1);
			else
				list.add(0);
		}
		Individual individual = new Individual(list);
		return individual;

	}

	public Individual crossover(Individual m) {
		int h = random.nextInt(K - 1);
		// System.out.println(h);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <= h; i++)
			list.add(this.chromosome.get(i));
		for (int i = h + 1; i < K; i++)
			list.add(m.getChromosome().get(i));
		Individual individual = new Individual(list);
		return individual;
	}

	public Individual mutation() {
		int h = random.nextInt(K - 1);
		// System.out.println(h);
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < K; i++)
			list.add(this.getChromosome().get(i));
		list.set(h, 0);
		Individual individual = new Individual(list);
		return individual;
	}

	public void print() {
		System.out.print("Best individual:");
		for (int i = 0; i < K; i++) {
			System.out.print(this.chromosome.get(i) + " ");
		}
		System.out.println();
	}

	public void readData(String url) {
		// read data from file txt
		Random random = new Random();
		ArrayList<Target> listTargets = new ArrayList<Target>();
		for (int i = 0; i < N; i++) {
			Target target = new Target();
			target.setX(Individual.xmax * random.nextDouble());
			target.setY(Individual.ymax * random.nextDouble());
			listTargets.add(target);
		}
		Individual.setTargets(listTargets);
		ArrayList<Sensor> list = new ArrayList<Sensor>();
		for (int i = 0; i < K; i++) {
			Sensor sensor = new Sensor(Individual.xmax * random.nextDouble(), Individual.ymax * random.nextDouble(),
					50.0, 100.0);
			list.add(sensor);
		}
		Individual.setPosition(list);
	}

	public int CovCost(int j) {
		int cost = 0;
		for (int i = 0; i < K; i++)
			if (Individual.position.get(i).Sensing(Individual.targets.get(j)) == 1 && this.chromosome.get(i) != 0) {
				cost++;
			}
		if (cost >= Individual.k)
			return Individual.k;
		else
			return Individual.k - cost;
	}

	public int ComCost(int j) {
		int cost = 0;
		for (int i = 0; i < K; i++)
			if (Individual.position.get(j).communication(Individual.position.get(i)) == 1 && this.chromosome.get(i) != 0
					&& i != j) {
				cost++;
			}
		if (cost >= Individual.m)
			return Individual.m;
		else
			return Individual.m - cost;

	}

	public int Cov(int j) {
		int cost = 0;
		for (int i = 0; i < K; i++)
			if (Individual.position.get(i).Sensing(Individual.targets.get(j)) == 1 && this.chromosome.get(i) != 0) {
				cost++;
			}
		return cost;
	}

	public int Com(int j) {
		int cost = 0;
		for (int i = 0; i < K; i++)
			if (Individual.position.get(j).communication(Individual.position.get(i)) == 1 && this.chromosome.get(i) != 0
					&& i != j) {
				cost++;
			}
		return cost;
	}

	public static ArrayList<Target> getTargets() {
		return targets;
	}

	public static void setTargets(ArrayList<Target> targets) {
		Individual.targets = targets;
	}

	public static ArrayList<Sensor> getPosition() {
		return position;
	}

	public static void setPosition(ArrayList<Sensor> position) {
		Individual.position = position;
	}

	public double fitness() {
		double w1 = 0.4, w2 = 0.3, w3 = 0.3;
		int M = 0;
		for (int i = 0; i < K; i++) { // object 1: minimmum selcted node
			if (this.chromosome.get(i) != 0)
				M++;
		}
		if (M == 0)
			return -1;

		double result = w1 * (1 - (double) M / K);
		int sumCov = 0;
		for (int i = 0; i < N; i++) { // object 2: k-coverage
			sumCov += this.CovCost(i);
		}
		result += w2 * (double) sumCov / (N * k);
		int sumCom = 0;
		for (int i = 0; i < K; i++) {
			if (this.chromosome.get(i) != 0)
				sumCom += this.ComCost(i);
		}
		result += w3 * (double) sumCom / (M * m);
		double a1 = 1.0 / (2 * Individual.N);
		double a2 = 1.0 / (Individual.K * Individual.m + Individual.N * Individual.k);
		if (this.check() == 0)
			return result;
		else {
			result = result - (this.check() * a1 + this.amount() * a2);
			return result;
		}
	}

	public int check() { // count number of violate constraint
		int cov = 0;
		for (int i = 0; i < Individual.N; i++) {
			if (this.Cov(i) < Individual.k)
				cov++;
		}
		int com = 0;
		for (int i = 0; i < Individual.K; i++) {
			if (this.chromosome.get(i) != 0 && this.Com(i) < Individual.m)
				com++;
		}
		return com + cov;
	}

	public int amount() { // caculate amount of violate constraint
		int count = 0;
		for (int i = 0; i < Individual.N; i++) {
			if (this.Cov(i) < Individual.k)
				count += Individual.k - this.Cov(i);
		}
		for (int i = 0; i < Individual.K; i++) {
			if (this.chromosome.get(i) != 0 && this.Com(i) < Individual.m)
				count += Individual.m - this.Com(i);
		}
		return count;

	}

}
