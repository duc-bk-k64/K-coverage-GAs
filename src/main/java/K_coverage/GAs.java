package K_coverage;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
public class GAs {
	public void solve(int maxIteration, double pc, double pm) {
		Population population = new Population();
		population.init();
		int k = 0;
		double[] result=new double[maxIteration];
		double[] generation=new double[maxIteration];
		while (k != maxIteration) {
			
			generation[k]= (double)k;
			population.caculateProbaly();
			population.crossover(pc);
			population.mutation(pm);
			population.select();
			result[k]=population.getPopulation().get(0).fitness();
			k++;
		}
		population.getPopulation().get(0).print();
		int M=0;
		for(int i=0;i<population.getPopulation().get(0).getChromosome().size();i++) {
			if(population.getPopulation().get(0).getChromosome().get(i)!=0)  M++;
		}
		System.out.println("Generation " + k + " Fitness value:" + population.getPopulation().get(0).fitness());
		System.out.println("Number of node selected:"+M);
		population.printCoverage();
		population.printConnected();
		System.out.println("Violate:"+ population.getPopulation().get(0).check());
		Plot2DPanel plot = new Plot2DPanel();
		plot.addLinePlot("Result",generation,result);
		JFrame frame = new JFrame("Result");
		frame.setSize(800,1000);
		  frame.setContentPane(plot);
		  frame.setVisible(true);
		  
	}

	public static void main(String args[]) {
		GAs gAs = new GAs();
		gAs.solve(1500, 0.8, 0.03);
	}
}
