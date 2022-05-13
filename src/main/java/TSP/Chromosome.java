package TSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Chromosome {
	private static int size = 48;
	private ArrayList<Integer> vertex;
	public static ArrayList<Point> data;

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

	public Chromosome mutation(Chromosome p) {
		ArrayList<Integer> z = new ArrayList<Integer>();
		for(int i=0;i<size;i++) z.add(p.getVertex().get(i));
		Random random = new Random();
		int g = random.nextInt(size - 1);
		int h = random.nextInt(size - 1);
		// System.out.println(h+" "+g);
		Collections.swap(z, g, h);
		Chromosome chromosome = new Chromosome(z);
		return chromosome;
	}

	public double fitness(Chromosome p) {
		double sum = 0.0;
		for (int i = 0; i < p.getVertex().size() - 1; i++) {
			sum += point.distance(data.get(p.vertex.get(i)), data.get(p.vertex.get(i + 1)));
		}
		sum += point.distance(data.get(p.vertex.get(0)), data.get(p.vertex.get(p.getVertex().size() - 1)));
		return -sum;
	}

	@SuppressWarnings("static-access")
	public void loadVertex() throws Exception {
		this.data = point.readData("D:/att48.tsp.txt");
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

//	public static void main(String[] args) throws Exception {
//		ArrayList<Integer> x = new ArrayList<>();
//		ArrayList<Integer> y = new ArrayList<>();
//		Chromosome zChromosome = new Chromosome();
//		Point point=new Point();
//		for (int i = 1; i <= 51; i++) {
//			x.add(i);
//			y.add(i);
//		}
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
