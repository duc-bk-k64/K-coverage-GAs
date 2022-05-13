package K_coverage;

public class Sensor {
	private double x;
	private double y;
	private double rs; // radius sensing
	private double rc; // radius communication

	public Sensor(double x, double y, double rs, double rc) {
		this.x = x;
		this.y = y;
		this.rs = rs;
		this.rc = rc;
	}

	public Sensor() {

	}

	int Sensing(Target target) {
		double distance = Math.sqrt(Math.pow(this.x - target.getX(), 2) + Math.pow(this.y - target.getY(), 2));
		if (distance < this.rs)
			return 1;
		return 0;
	}

	int communication(Sensor sensor) {
		double distance = Math.sqrt(Math.pow(this.x - sensor.getX(), 2) + Math.pow(this.y - sensor.getY(), 2));
		if (distance < this.rc)
			return 1;
		return 0;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getRc() {
		return rc;
	}

	public void setRc(double rc) {
		this.rc = rc;
	}

}
