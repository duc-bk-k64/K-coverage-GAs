package MFEA_TSP;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class MFEA {
	private Individuals zIndividuals = new Individuals();

	public void solve(int max_iteration, double pm, double pc, int x) throws Exception { // x is firt city in TRP
		Population population = new Population();
		population.init();
		population.validate(x);
		int k = 0;
		double[] tsp = new double[max_iteration];
		double[] trp = new double[max_iteration];
		double[] generation = new double[max_iteration];
		System.out.println();
		while (k < max_iteration) {

			generation[k] = k;
			//System.out.println(k);
			population.calRouteWheel();
			population.crossoverAmutation(pm, pc);
			population.RVND(x);
			population.validate(x);
			population.select();
			Individuals individuals = population.getbest();
			System.out.println("Generation:"+k+" "+ zIndividuals.fitnessTSP(individuals) + " " + zIndividuals.fitnessTRP(individuals, x)); 
			tsp[k] = zIndividuals.fitnessTSP(individuals);
			trp[k] = zIndividuals.fitnessTRP(individuals, x);
			k++;
		}
		Individuals individuals = population.getbest();
		System.out.print(zIndividuals.fitnessTSP(individuals) + " " + zIndividuals.fitnessTRP(individuals, x));
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("TSP",Color.RED, generation,tsp);
		plot.addLinePlot("TRP",Color.BLUE, generation,trp);
		JFrame frame = new JFrame("Result");
		frame.setSize(800, 1000);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

	public static void main(String args[]) throws Exception {
		MFEA mfea = new MFEA();
		mfea.solve(2000, 0.8, 0.05, 1);
	}
}
