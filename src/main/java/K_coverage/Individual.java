package K_coverage;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.plots.ScatterPlot;

public class Individual {
	public static int K = 200; // number of potential position
	public static int N = 100; // number of targets
	private ArrayList<Integer> chromosome;
	public static ArrayList<Target> targets;
	public static ArrayList<Sensor> position;
	public static int k = 3; // k-coverage
	public static int m = 2; // m-connected
	public static int xmax = 300;
	public static int ymax = 300;
	public static double[][] xy = new double[Individual.K][2];
	public static double[][] ss = new double[Individual.K][2];
	public static int count = 0;

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

	public void readData(String url) throws IOException {
		// read data from file txt
		FileWriter writer = new FileWriter("D:/targetgauss.txt", true);
		FileWriter writers = new FileWriter("D:/positiongauss.txt", true);
		Random random = new Random();
		ArrayList<Target> listTargets = new ArrayList<Target>();
		for (int i = 0; i < N; i++) {
			Target target = new Target();
			double x = -2.5, y = -2.5;
			while (x <= -2.0 || x >= 2.0) {
				x = random.nextGaussian();

			}
			while (y <= -2.0 || y >= 2.0) {
				y = random.nextGaussian();

			}
			x=Math.abs(x);
			y=Math.abs(y);
			target.setX(Individual.xmax * x / 2);
			target.setY(Individual.ymax * y / 2);
			xy[i][0] = target.getX();
			xy[i][1] = target.getY();
			writer.write(xy[i][0] + " " + xy[i][1] + "\n");
			listTargets.add(target);
		}
		writer.write("-1\n");
		writer.close();
		Plot2DPanel plot = new Plot2DPanel();
		ScatterPlot scatterPlot = new ScatterPlot("Targte", Color.BLUE, xy);
		plot.plotCanvas.addPlot(scatterPlot);
		Individual.setTargets(listTargets);
		ArrayList<Sensor> list = new ArrayList<Sensor>();
		for (int i = 0; i < K; i++) {
			Sensor sensor = new Sensor(Individual.xmax * random.nextDouble(), Individual.ymax * random.nextDouble(),
					50.0, 100.0);
			ss[i][0] = sensor.getX();
			ss[i][1] = sensor.getY();
			writers.write(ss[i][0] + " " + ss[i][1] + "\n");
			list.add(sensor);
		}
		writers.write("-1\n");
		writers.close();
		// Plot2DPanel plot = new Plot2DPanel();
		ScatterPlot scatterPlot2 = new ScatterPlot("Targte", Color.RED, ss);
		plot.plotCanvas.addPlot(scatterPlot2);
		JFrame frame = new JFrame("Result");
		frame.setSize(800, 1000);
		frame.setContentPane(plot);
		//frame.setVisible(true);
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
			return cost;
		// return Individual.k - cost;
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
			return cost;
		// return Individual.m - cost;

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
		double a1 = 1.0 / (Individual.N + Individual.K);
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

	public void write() throws IOException {
		int[][] sparseMatrix = new int[Individual.xmax * 2][Individual.ymax * 2];
		FileWriter writer = new FileWriter("D:/resultgauss.txt", true);
		for (int i = 0; i < Individual.xmax * 2; i++) {
			for (int j = 0; j < Individual.ymax * 2; j++) {
				int k = 0;
				for (int q = 0; q < Individual.K; q++) {
					if (this.getChromosome().get(q) != 0) {
						if (Individual.position.get(q).getX() >= j * 0.5
								&& Individual.position.get(q).getX() < (j + 1) * 0.5) {
							if (Individual.position.get(q).getY() >= i * 0.5
									&& Individual.position.get(q).getY() < (i + 1) * 0.5) {
								// writer.write("1 ");
								sparseMatrix[j][i] = 1;
								k++;
								count++;
								break;
							}

						}
					}
				}
				if (k == 0)
					sparseMatrix[j][i] = 0;
				// writer.write("0 ");
			}
			// writer.write("\n");
		}
		// convert matrix to sparse
		int size = 0;
		for (int i = 0; i < Individual.xmax * 2; i++) {
			for (int j = 0; j < Individual.ymax * 2; j++) {
				if (sparseMatrix[i][j] != 0) {
					size++;
				}
			}
		}

		// number of columns in compactMatrix (size) must be
		// equal to number of non - zero elements in
		// sparseMatrix
		int compactMatrix[][] = new int[2][size];
		// Making of new matrix
		int z = 0;
		for (int i = 0; i < Individual.xmax * 2; i++) {
			for (int j = 0; j < Individual.ymax * 2; j++) {
				if (sparseMatrix[i][j] != 0) {
					compactMatrix[0][z] = i;
					compactMatrix[1][z] = j;
					// compactMatrix[2][z] = sparseMatrix[i][j];
					z++;
				}
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 70; j++) {
				// System.out.printf("%d ", compactMatrix[i][j]);
				if (j < size)
					writer.write(compactMatrix[i][j] + " ");
				else
					writer.write("0 ");
			}
			writer.write("\n");
			// System.out.printf("\n");
		}
//		for (int i = 0; i < size; i++) {
//			writer.write(compactMatrix[0][i] + " " + compactMatrix[1][i] + "\n");
//		}

		writer.write("-1 \n");
		writer.close();

	}

}
