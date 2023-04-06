package Operating_Classes;
import com.leapmotion.leap.Arm;
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
	int missedFrames = 0;
	AlphaBetaFilter elbowAngleFilter;
	AlphaBetaFilter wristAngleFilter;
	AlphaBetaFilter xFilter, zFilter;
	public Pose(Hand h) {
		Arm arm = h.arm();
    	elbowAngle = Math.toDegrees(arm.direction().pitch());
    	wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	x = h.palmPosition().getX();
    	z = h.palmPosition().getZ();
    	setRawAngles();
    	elbowAngleFilter =  new AlphaBetaFilter(alpha, beta, elbowAngle);
    	wristAngleFilter =  new AlphaBetaFilter(alpha, beta, wristAngle);
    	xFilter = new AlphaBetaFilter(alpha, beta, x);
    	zFilter = new AlphaBetaFilter(alpha, beta, z);
//    	System.out.println(h.palmPosition().toString());
	}
	
	public void update(Hand h) {
		Arm arm = h.arm();
    	elbowAngle = Math.toDegrees(arm.direction().pitch());
    	wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	x = h.palmPosition().getX();
    	z = h.palmPosition().getZ();
    	setRawAngles();
    	elbowAngle = elbowAngleFilter.filter(elbowAngle, dt);
    	wristAngle = wristAngleFilter.filter(wristAngle, dt);
    	x = xFilter.filter(x, dt);
    	z = zFilter.filter(z, dt);
    	missedFrames = 0;
//    	System.out.println(h.palmPosition().toString());
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
		return "Elbow angle: "+elbowAngle+"\nWrist angle: "+wristAngle;
	}

	private void setRawAngles() {
		rawElbowAngle = elbowAngle;
		rawWristAngle = wristAngle;
	}
	
}
