package lib;

import org.ejml.simple.SimpleMatrix;

public class CollisionAvoidance {
	public static final int PADDING = 0;
	public static final int bicepR = 30;
	public static final int bicepL = 300;
	public static final int forearmL = 250;
	public static boolean validatePosision(TransformedPose p) {
		// normalize
		p.positions[TransformedPose.elbow]-=90;
		
		SimpleMatrix shoulderR = createRMatrix(p.positions[TransformedPose.shoulder]);
		SimpleMatrix shoulderT = createTMatrix(bicepL, 0);
		SimpleMatrix elbowR = createRMatrix(p.positions[TransformedPose.elbow]);
		SimpleMatrix elbowT = createTMatrix(forearmL, 0);
//		System.out.println(shoulderR.toString());
//		System.out.println(shoulderT.toString());
		SimpleMatrix wristPosition = shoulderR.mult(shoulderT).mult(elbowR).mult(elbowT);
//		System.out.println(wristPosition.toString());
		return wristPosition.get(1,2)>PADDING;
	}
	
	public static boolean validatePosision(byte positions[]) {
		// normalize
		positions[TransformedPose.elbow]-=90;
		
		SimpleMatrix shoulderR = createRMatrix(positions[TransformedPose.shoulder]);
		SimpleMatrix shoulderT = createTMatrix(bicepL, 0);
		SimpleMatrix elbowR = createRMatrix(positions[TransformedPose.elbow]);
		SimpleMatrix elbowT = createTMatrix(forearmL, 0);
//		System.out.println(shoulderR.toString());
//		System.out.println(shoulderT.toString());
		SimpleMatrix wristPosition = shoulderR.mult(shoulderT).mult(elbowR).mult(elbowT);
		System.out.println(wristPosition.toString());
		return wristPosition.get(1,2)>PADDING;
	}
	
	private static SimpleMatrix createRMatrix(double theta) {
		theta = Math.toRadians(theta);
		SimpleMatrix rM = new SimpleMatrix(3,3);
		rM.set(0, 0, Math.cos(theta));
		rM.set(0, 1, -Math.sin(theta));
		rM.set(1, 0, Math.sin(theta));
		rM.set(1, 1, Math.cos(theta));
		rM.set(2,2,1);
		return rM;
	}
	
	private static SimpleMatrix createTMatrix(double dx, double dy) {
		SimpleMatrix m = new SimpleMatrix(3,3);
		for(int i=0;i<3;i++)
			m.set(i, i, 1);
		m.set(0, 2, dx);
		m.set(1, 2, dy);
		return m;
	}
}
