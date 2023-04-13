package lib;
import java.util.HashMap;
import java.util.Map;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;

public class Pose {
	public static final double alpha = 0.8;
	public static final double beta = 0.1;
	public static final double dt = 0.1;
	double elbowAngle;
	double wristAngle;
	double x, z;
	// for logging
	double rawElbowAngle;
	double rawWristAngle;
	double grabStrength;
	int missedFrames = 0;
	AlphaBetaFilter elbowAngleFilter;
	AlphaBetaFilter wristAngleFilter;
	AlphaBetaFilter xFilter, zFilter;
	AlphaBetaFilter grabStrengthFilter;
	public Map<Finger.Type, Double> fingerAngles = new HashMap<Finger.Type, Double>();
	Map<Finger.Type, AlphaBetaFilter> fingerFilters = new HashMap<Finger.Type, AlphaBetaFilter>();
	public Pose(Hand h) {
		getData(h);
    	setRawData();
    	// create filters
    	initFilters();
//    	System.out.println(h.palmPosition().toString());
	}
	
	public void update(Hand h) {
		getData(h);
    	setRawData();
    	// apply filters
    	filterData();
    	missedFrames = 0;
//    	System.out.println(h.palmPosition().toString());
	}
	
	private void getData(Hand h) {
		Arm arm = h.arm();
    	elbowAngle = Math.toDegrees(arm.direction().pitch());
    	wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	x = h.palmPosition().getX();
    	z = h.palmPosition().getZ();
    	grabStrength = h.grabStrength();
    	// fingers
    	for(Finger f : h.fingers()) {
    		fingerAngles.put(f.type(), getFingerAngle(h, f));
//    		System.out.println(f.type());
    	}
	}
	
	private void initFilters() {
		elbowAngleFilter =  new AlphaBetaFilter(alpha, beta, elbowAngle);
    	wristAngleFilter =  new AlphaBetaFilter(alpha, beta, wristAngle);
    	xFilter = new AlphaBetaFilter(alpha, beta, x);
    	zFilter = new AlphaBetaFilter(alpha, beta, z);
    	grabStrengthFilter = new AlphaBetaFilter(alpha, beta, grabStrength);
    	for(Finger.Type t : Finger.Type.values()) {
    		fingerFilters.put(t, new AlphaBetaFilter(alpha, beta, fingerAngles.get(t)));
    	}
	}
	
	private void filterData() {
		elbowAngle = elbowAngleFilter.filter(elbowAngle, dt);
    	wristAngle = wristAngleFilter.filter(wristAngle, dt);
    	x = xFilter.filter(x, dt);
    	z = zFilter.filter(z, dt);
    	grabStrength = grabStrengthFilter.filter(grabStrength, dt);
    	// fingers
    	for(Finger.Type t : Finger.Type.values()) {
    		double value = fingerAngles.get(t);
    		fingerAngles.put(t, fingerFilters.get(t).filter(value, dt));
    	}
	}
	
	private double getFingerAngle(Hand h, Finger f) {
		Bone distal = f.bone(Bone.Type.TYPE_DISTAL);
		if(f.type().equals(Finger.Type.TYPE_THUMB)) {
			Bone proximal = f.bone(Bone.Type.TYPE_PROXIMAL);
			return proximal.direction().angleTo(distal.direction());
//			return h.palmNormal().angleTo(intermediate.direction());
		}
		Bone metacarpal = f.bone(Bone.Type.TYPE_METACARPAL);
		
		return metacarpal.direction().angleTo(distal.direction());
	}
	
	private void setRawData() {
		rawElbowAngle = elbowAngle;
		rawWristAngle = wristAngle;
	}
	
	public double getElbowAngle() {
		return elbowAngle;
	}
	public void setElbowAngle(double elbowAngle) {
		this.elbowAngle = elbowAngle;
	}
	public double getWristAngle() {
		return wristAngle;
	}
	public void setWristAngle(double wristAngle) {
		this.wristAngle = wristAngle;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public String toString() {
		String s = "Elbow angle: "+elbowAngle+"\nWrist angle: "+wristAngle+"\nThumb: "+fingerAngles.get(Finger.Type.TYPE_THUMB)+"\nIndex: "+fingerAngles.get(Finger.Type.TYPE_INDEX)+"\nMiddle: "+fingerAngles.get(Finger.Type.TYPE_MIDDLE)+"\nRing: "
				+fingerAngles.get(Finger.Type.TYPE_RING)
				+"\nPinky: "+fingerAngles.get(Finger.Type.TYPE_PINKY);
		return s;
	}
	
}
