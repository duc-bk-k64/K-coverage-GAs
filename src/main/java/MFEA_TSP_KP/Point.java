package MFEA_TSP_KP;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Point {
	private double x;
	private double y;

	public Point(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Point() {

	}

	public double distance(Point p, Point q) {
		double z = Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2);
		return Math.sqrt(z);
	}

	public void print() {
		System.out.println(this.x + "," + this.y);
	}

	public ArrayList<Point> readData(String url) throws Exception {
		try {
			String line = "";
			ArrayList<Point> data = new ArrayList<Point>();
			Point point = new Point(0.0, 0.0);
			data.add(point);
			File file = new File(url);
			Scanner read = new Scanner(file);
			for (int i = 0; i < 6; i++) {
				System.out.println(read.nextLine());
			}
			while (read.hasNextLine()) {
				line = read.nextLine();
				String[] part = line.split("\\s");
				Point p = new Point(Double.parseDouble(part[1]), Double.parseDouble(part[2]));
				data.add(p);
				// System.out.println(part[0]+","+part[1]+","+part[2]);
			}
			read.close();
			System.out.println("Read data successfully");

			return data;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
