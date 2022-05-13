package TSP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class GAs {
	public GAs() {
	}

	public void algorithms(int max_interation, double rc, double rm) throws Exception {
		 //for(int i=0;i<5;i++) {
		// Instant timeInstant=Instant.now();
		Population population = new Population();
		Chromosome chromosome = new Chromosome();
		population.inti();
		int k = 0;
		double[] result = new double[max_interation];
		double[] generation = new double[max_interation];
		while (k != max_interation) {
			generation[k] = (double) k;
			population.crossover(rc);
			population.mutation(rm);
			population.select();
			result[k] = -chromosome.fitness(population.getBest());
			k++;
			// System.out.print("Generation "+k+":");
//			population.getBest().printf();
//			System.out.println(chromosome.fitness(population.getBest()));
		}
		population.getBest().printf();
		System.out.println(-chromosome.fitness(population.getBest()));
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("Result", generation, result);
		// plot.addPlot(null);
		JFrame frame = new JFrame("Result");
		frame.setSize(800, 1000);
		frame.setContentPane(plot);
		frame.setVisible(true);

	//	 }

	}

	public void optimumResult(String url) throws FileNotFoundException { // n is number of node
		File file = new File(url);
		Scanner read = new Scanner(file);
		for (int i = 0; i < 5; i++) {
			System.out.println(read.nextLine());
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (read.hasNextLine()) {
			int xDouble = Integer.parseInt(read.nextLine());
			list.add(xDouble);
		}
		read.close();
		Chromosome chromosome = new Chromosome(list);
		System.out.println("Optimum result:"+-chromosome.fitness(chromosome));
	}

	public static void main(String args[]) throws Exception {
		GAs gAs = new GAs();
		gAs.algorithms(1000, 0.85, 0.15);
		gAs.optimumResult("D:/att48.opt.tour.txt");
	}

}
