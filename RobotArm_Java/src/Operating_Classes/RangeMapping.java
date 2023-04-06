package Operating_Classes;

public class RangeMapping {
	public static final int shoulderMinAngle = 20;
	public static final int shoulderMaxAngle = 97;
	public static final int elbowMinAngle = 0;
	public static final int elbowMaxAngle = 100;
	int l1, h1;
	int l2 = 0;
	int h2 = 180;
	
	public RangeMapping(int l1, int h1) {
		this.l1 = l1;
		this.h1 = h1;
	}
	
	public RangeMapping(int l1, int h1, int l2, int h2) {
		this.l1 = l1;
		this.l2 = l2;
		this.h1 = h1;
		this.h2 = h2;
	}
	
	public float fit(double val) {
		float v = clip((float)val);
		return (v - l1) * (h2 - l2) / (h1 - l1) + l2;
	}
	
	private float clip(float val) {
		return Math.max(l1, Math.min(h1, val));
	}

}
