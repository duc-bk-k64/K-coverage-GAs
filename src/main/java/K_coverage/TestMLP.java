package K_coverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TestMLP {
	public static int m = 2;
	public static int k = 3;

	public void test(String urlTarget, String urlSensor) throws FileNotFoundException {
		ArrayList<Sensor> sensors = new ArrayList<>();
		ArrayList<Target> targets = new ArrayList<>();
		File file = new File(urlSensor);
		Scanner read = new Scanner(file);
		String line = "";
		while (read.hasNextLine()) {
			line = read.nextLine();
			String[] part = line.split("\\s");
			double x = Double.parseDouble(part[0]) / 2;
			double y = Double.parseDouble(part[1]) / 2;
			System.out.println(x + " " + y);
			if (x > 0.0 || y > 0.0) {
				Sensor sensor = new Sensor();
				sensor.setX(x);
				sensor.setY(y);
				sensor.setRs(50.0);
				sensor.setRc(100.0);
				sensors.add(sensor);
			}
		}
		read.close();
		file = new File(urlTarget);
		read = new Scanner(file);
		while (read.hasNextLine()) {
			line = read.nextLine();
			String[] part = line.split("\\s");
			double x = Double.parseDouble(part[0]);
			double y = Double.parseDouble(part[1]);
			// System.out.println(x+" "+y);
			Target target = new Target(x, y);
			targets.add(target);
		}
		read.close();
		System.out.println(sensors.size());
		System.out.println(targets.size());
		int cov = 0;
		for (int i = 0; i < targets.size(); i++) {
			int count = 0;
			for (int j = 0; j < sensors.size(); j++) {
				if (sensors.get(j).Sensing(targets.get(i)) == 1)
					count++;
			}
			if (count >= TestMLP.k)
				cov++;
		}
		int com = 0;
		for (int i = 0; i < sensors.size(); i++) {
			int count = 0;
			for (int j = 0; j < sensors.size(); j++) {
				if (sensors.get(j).communication(sensors.get(i)) == 1 && i != j)
					count++;
			}
			if (count >= TestMLP.m)
				com++;
		}
		System.out.println("Coverage:" + 100.0 * cov / targets.size()+"%");
		System.out.println("Communication::" + 100.0 * com / sensors.size()+"%");
	}
	public static void main(String args[]) throws FileNotFoundException {
		 TestMLP testMLP = new TestMLP();
		 testMLP.test("D://targettest.txt", "D://Downloads/mlp (2).txt");

	}

}
