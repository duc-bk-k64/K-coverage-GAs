package TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class Chromosome {
	private static int size = 51;
	private ArrayList<Integer> vertex;
	public static ArrayList<Point> data;
	Random random = new Random();
	private Point point = new Point();

	public ArrayList<Integer> getVertex() {
		return vertex;
	}

	public void setVertex(ArrayList<Integer> vertex) {
		this.vertex = vertex;
	}

	public Chromosome(ArrayList<Integer> vertex) {
		this.setVertex(vertex);
	}

	public Chromosome() {

	}

	public Chromosome create() {
		ArrayList<Integer> litsArrayList = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++)
			litsArrayList.add(i);
		Collections.shuffle(litsArrayList);
		Chromosome newchChromosome = new Chromosome(litsArrayList);
		return newchChromosome;
	}

	public Chromosome crossover(Chromosome p, Chromosome q) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		Random random = new Random();
		int cr = random.nextInt(size - 1); // generate random 0<cr<n
		// System.out.println(cr);
		for (int i = 0; i < cr; i++) {
			a.add(p.vertex.get(i));
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < cr; j++) {
				if (a.get(j) == q.vertex.get(i))
					break;
				else if (j == cr - 1) {
					a.add(q.vertex.get(i));

				}
			}
		}
		Chromosome chromosome = new Chromosome();
		chromosome.setVertex(a);
		return chromosome;

	}

	public List<Chromosome> crossoverCycle(Chromosome p, Chromosome q) { // cycle crossover
		List<ArrayList<Integer>> list = new ArrayList<>();
		list = this.findCycle(p, q);
		ArrayList<Integer> child1 = new ArrayList<>();
		ArrayList<Integer> child2 = new ArrayList<>();
		for (int i = 0; i < Chromosome.size; i++) {
			child1.add(0);
			child2.add(0);
		}
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Integer> data = list.get(i);
			if (i % 2 == 0) {
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Chromosome.size; j++) {
						if (data.get(k) == p.getVertex().get(j)) {
							child1.set(j, data.get(k));
						}
					}
				}
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Chromosome.size; j++) {
						if (data.get(k) == q.getVertex().get(j)) {
							child2.set(j, data.get(k));
						}
					}
				}
			} else {
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Chromosome.size; j++) {
						if (data.get(k) == q.getVertex().get(j)) {
							child1.set(j, data.get(k));
						}
					}
				}
				for (int k = 0; k < data.size(); k++) {
					for (int j = 0; j < Chromosome.size; j++) {
						if (data.get(k) == p.getVertex().get(j)) {
							child2.set(j, data.get(k));
						}
					}
				}
			}
		}
		Chromosome individuals = new Chromosome();
		individuals.setVertex(child1);
		Chromosome individuals2 = new Chromosome();
		individuals2.setVertex(child2);
		List<Chromosome> listIndividuals = new ArrayList<>();
		listIndividuals.add(individuals);
		listIndividuals.add(individuals2);
		return listIndividuals;
	}

	public Chromosome mutation(Chromosome p) {
		ArrayList<Integer> z = new ArrayList<Integer>();
		for (int i = 0; i < size; i++)
			z.add(p.getVertex().get(i));
		Random random = new Random();
		int g = random.nextInt(size - 1);
		int h = random.nextInt(size - 1);
		// System.out.println(h+" "+g);
		Collections.swap(z, g, h);
		Chromosome chromosome = new Chromosome(z);
		return chromosome;
	}

	public List<ArrayList<Integer>> findCycle(Chromosome p, Chromosome q) {
		List<ArrayList<Integer>> cycle = new ArrayList<ArrayList<Integer>>();
		int[] inq = new int[Chromosome.size + 1];
		for (int i = 0; i < Chromosome.size + 1; i++)
			inq[i] = 0;
		int a = -999;
		int index = 0;
		for (int i = 0; i < Chromosome.size; i++) {
			if (inq[p.getVertex().get(i)] == 0) {
				ArrayList<Integer> list = new ArrayList<Integer>();
				index = i;
				inq[p.getVertex().get(i)] = 1;
				while (true) {
					a = p.getVertex().get(index);
					inq[p.getVertex().get(index)] = 1;
					list.add(a);
					for (int j = 0; j < Chromosome.size; j++) {
						if (q.getVertex().get(j) == a) {
							index = j;
							break;
						}
					}

					if (inq[p.getVertex().get(index)] == 1)
						break;
				}
				cycle.add(list);
			}
		}
		return cycle;
	}

	public double fitness(Chromosome p) {
		double sum = 0.0;
		for (int i = 0; i < p.getVertex().size() - 1; i++) {
			sum += point.distance(data.get(p.vertex.get(i)), data.get(p.vertex.get(i + 1)));
		}
		sum += point.distance(data.get(p.vertex.get(0)), data.get(p.vertex.get(p.getVertex().size() - 1)));
		return -sum;
	}
	public double fitnessTRP(Chromosome p, int x) { // x is first city
		double sum = 0.0;
		int t = 0;
		int position = 0;
		for (int i = 0; i <Chromosome.size; i++) { // find index of city x
			if (p.getVertex().get(i) == x) {
				position = i;
				break;
			}
		}
		double distance = 0.0;
		while (t != (Chromosome.size - 1)) {
			t++;
			distance += point.distance(data.get(p.getVertex().get(position)),
					data.get(p.getVertex().get(increCycle(position + 1))));
			sum += distance;
			position = increCycle(position + 1);
		}
		return sum / Chromosome.size;

	}
	public int increCycle(int x) {
		if (x < Chromosome.size)
			return x;
		else
			return x - Chromosome.size;
	}

	@SuppressWarnings("static-access")
	public void loadVertex() throws Exception {
		this.data = point.readData("D:/eil51.tsp.txt");
	}

	public double compare(Chromosome a, Chromosome b) {
		return a.fitness(a) - a.fitness(b);
	}

	public void printf() {
		// System.out.println(this.vertex.length);
		for (int i = 0; i < this.vertex.size(); i++) {
			System.out.print(this.vertex.get(i) + " ");
		}
		System.out.println();
	}
//
//	public static void main(String[] args) throws Exception {
//		ArrayList<Integer> x = new ArrayList<Integer>();
//		ArrayList<Integer> y = new ArrayList<Integer>();
//		Chromosome zChromosome = new Chromosome();
//		for (int i = 1; i <= Chromosome.size; i++) {
//			x.add(i);
//			y.add(i);
//		}
//		Collections.shuffle(x);
//		Collections.shuffle(y);
//		Chromosome chromosome = new Chromosome(x);
//		Chromosome chromosome2 = new Chromosome(y);
//		chromosome.printf();
//		chromosome2.printf();
//		zChromosome.findCycle(chromosome, chromosome2).forEach(list -> {
//			for (int i = 0; i < list.size(); i++)
//				System.out.print(" " + list.get(i));
//			System.out.println();
//		});
//		zChromosome.crossoverCycle(chromosome, chromosome2).printf();
//	}
//
//		Collections.shuffle(x);
//		Chromosome chromosome1 = new Chromosome(x);
//		Collections.shuffle(y);
//		Chromosome chromosome2 = new Chromosome(y);
//		chromosome1.printf();
//		chromosome2.printf();
//		Chromosome chromosome = chromosome1.crossover(chromosome1, chromosome2);
//		chromosome.printf();
//		chromosome1.mutation(chromosome1);
//		chromosome1.printf();
//		zChromosome.loadVertex();
//		System.out.println(point.distance(data.get(1), data.get(2)));
//		System.out.println(zChromosome.fitness(chromosome1)+" "+zChromosome.fitness(chromosome2));
//
//	}

}
