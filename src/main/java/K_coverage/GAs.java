package K_coverage;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class GAs {
	public void solve(int maxIteration, double pc, double pm) throws IOException {
		Population population = new Population();
		population.init();
		int k = 0;
		double[] result = new double[maxIteration];
		double[] generation = new double[maxIteration];
		while (k != maxIteration) {

			generation[k] = (double) k;
			population.caculateProbaly();
			population.crossover(pc);
			population.mutation(pm);
			population.select();
			result[k] = population.getPopulation().get(0).fitness();
			if (k > 100 && (Math.abs(result[k] - result[k - 100])) < 0.0001)
				break;
			k++;
			if (k % 20 == 0)
				System.out.println("Generation " + k + " Fitness value:" + population.getPopulation().get(0).fitness());
		}
		population.getPopulation().get(0).print();
		int M = 0;
		for (int i = 0; i < population.getPopulation().get(0).getChromosome().size(); i++) {
			if (population.getPopulation().get(0).getChromosome().get(i) != 0)
				M++;
		}
		System.out.println("Generation " + k + " Fitness value:" + population.getPopulation().get(0).fitness());
		System.out.println("Number of node selected:" + M);
		population.printCoverage();
		population.printConnected();
		System.out.println("Violate:" + population.getPopulation().get(0).check());
		population.getPopulation().get(0).write();
		System.out.println(Individual.count);
		Plot2DPanel plot = new Plot2DPanel();
		if (k == maxIteration)
			plot.addLinePlot("Result", generation, result);
		else {
			double[] y = new double[k];
			double[] x = new double[k];
			for (int i = 0; i < k; i++) {
				x[i] = generation[i];
				y[i] = result[i];
			}
			plot.addLinePlot("Result", Color.RED, x, y);
		}
		JFrame frame = new JFrame("Result");
		frame.setSize(800, 1000);
		frame.setContentPane(plot);
		//frame.setVisible(true);

	}

	public static void main(String args[]) throws IOException {
		 for (int i = 0; i < 1; i++) {
		GAs gAs = new GAs();
		gAs.solve(5000, 0.8, 0.03);
		 }
		// 552
	}
}
