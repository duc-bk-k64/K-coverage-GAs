package MFEA_TSP_KP;

import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class MFEA {
	public void solve(int max_iteration, double pc) throws Exception {
		Population population = new Population();
		population.init();
		population.validate();
		int k = 0;
		double[] tsp = new double[max_iteration];
		double[] trp = new double[max_iteration];
		double[] generation = new double[max_iteration];
		while (k != max_iteration) {
			generation[k] = k;
			population.calRouteWheel();
			population.crossoverAmutation(pc);
			population.validate();
			population.select();
			Individuals individuals = population.getbestTSP();
			Individuals individuals2 = population.getnestKP();
			System.out.println("Generation:" + k + " " + individuals.fitnessTSP() + " " + individuals2.fitnessKP());
			tsp[k] = individuals.fitnessTSP();
			trp[k] = individuals2.fitnessKP();
			k++;
		}
		population.getnestKP().printKP();
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("TSP", Color.RED, generation, tsp);
	    plot.addLinePlot("TRP", Color.BLUE, generation, trp);
//		Plot2DPanel panel=new Plot2DPanel();
//		panel.addLinePlot("TRP", Color.BLUE, generation, trp);
		JFrame frame = new JFrame("Result");
		frame.setSize(800, 1000);
		frame.setContentPane(plot);
		frame.setVisible(true);
//		JFrame frame2=new JFrame();
//		frame2.setSize(800,1000);
//		frame2.setContentPane(panel);
//		frame2.setVisible(true);

	}

	public static void main(String args[]) throws Exception {
		MFEA mfea = new MFEA();
		mfea.solve(3000, 0.1);
	}
}
