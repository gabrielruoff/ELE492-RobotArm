package lib;
import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Hand;

public class Pose {
	public static final double alpha = 0.8;
	public static final double beta = 0.1;
	public static final double dt = 0.1;
	double elbowAngle;
	double wristAngle;
	// for logging
	double rawElbowAngle;
	double rawWristAngle;
	int missedFrames = 0;
	AlphaBetaFilter elbowAngleFilter = null;
	AlphaBetaFilter wristAngleFilter = null;
	public Pose(Hand h) {
		Arm arm = h.arm();
    	elbowAngle = Math.toDegrees(arm.direction().pitch());
    	wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	setRawAngles();
    	elbowAngleFilter =  new AlphaBetaFilter(alpha, beta, elbowAngle);
    	wristAngleFilter =  new AlphaBetaFilter(alpha, beta, wristAngle);
	}
	
	public void update(Hand h) {
		Arm arm = h.arm();
    	elbowAngle = Math.toDegrees(arm.direction().pitch());
    	wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	setRawAngles();
    	elbowAngle = elbowAngleFilter.filter(elbowAngle, dt);
    	wristAngle = wristAngleFilter.filter(wristAngle, dt);
    	missedFrames = 0;
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
	
	@Override
	public String toString() {
		return "Elbow angle: "+elbowAngle+"\nWrist angle: "+wristAngle;
	}

	private void setRawAngles() {
		rawElbowAngle = elbowAngle;
		rawWristAngle = wristAngle;
	}
	
}
