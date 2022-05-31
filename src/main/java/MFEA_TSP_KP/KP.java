package MFEA_TSP_KP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class KP {
	public int size;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@SuppressWarnings("resource")
	public ArrayList<Double> readData(String url) throws FileNotFoundException {
		try {
			ArrayList<Double> list = new ArrayList<>();
			list.add(0.0);
			String line = "";
			File file = new File(url);
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				line = read.nextLine();
				double s = Double.parseDouble(line);
				list.add(s);
				
			}

			return list;
		} catch (Exception e) {
			throw new FileNotFoundException(e.getMessage());
		}

	}
	@SuppressWarnings("resource")
	public ArrayList<Double> readValue(String url) throws FileNotFoundException {
		try {
			ArrayList<Double> list = new ArrayList<>();
			list.add(0.0);
			String line = "";
			File file = new File(url);
			Scanner read = new Scanner(file);
			while (read.hasNextLine()) {
				line = read.nextLine();
				double s = Double.parseDouble(line);
				list.add(s);

			}

			return list;
		} catch (Exception e) {
			throw new FileNotFoundException(e.getMessage());
		}

	}
	


//	public static void main(String args[]) throws FileNotFoundException {
//		KP kp=new KP();
//		kp.readData("D:/p02_w.txt").forEach(data->{
//			System.out.print(data+" ");
//		});
//
//	}

}
