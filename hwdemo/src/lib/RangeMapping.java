package lib;

public class RangeMapping {
	int l1, h1;
	int l2 = 0;
	int h2 = 180;
	public RangeMapping(int l1, int h1) {
		this.l1 = l1;
//		this.l2 = l2;
		this.h1 = h1;
//		this.h2 = h2;
	}
	
	public float fit(double val) {
		float v = clip((float)val);
//		System.out.println("after clip: "+v);
		return (v - l1) * (h2 - l2) / (h1 - l1) + l2;
	}
	
	private float clip(float val) {
		return Math.max(l1, Math.min(h1, val));
	}

}
