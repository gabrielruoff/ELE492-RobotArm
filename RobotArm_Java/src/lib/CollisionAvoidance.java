package lib;

import java.util.Vector;

import org.ejml.simple.SimpleMatrix;

public class CollisionAvoidance {
	// millimeters
	public static final int PADDING = -100;
	public static final int bicepR = 30;
	public static final int bicepL = 300;
	public static final int forearmL = 250;
	public static final int wristL = 80;
	public static final int clawL = 120;
	public static boolean validatePosition(TransformedPose p) {
		byte pos[] = new byte[p.positions.length];
		for(int i=0;i<p.positions.length;i++) {
			pos[i] = (byte)p.positions[i];
		}
		return validatePosition(pos);
	}
	
	public static boolean validatePosition(byte positions[]) {
		// normalize
		float elbowNorm = Arduino.byteToInt(positions[TransformedPose.elbow])-150;
		float wristNorm = Arduino.byteToInt(positions[TransformedPose.wrist])-90;
		System.out.println(elbowNorm);
		SimpleMatrix shoulderR = createRMatrix(positions[TransformedPose.shoulder]);
		SimpleMatrix shoulderT = createTMatrix(bicepL, 0);
		SimpleMatrix elbowR = createRMatrix(elbowNorm);
		SimpleMatrix elbowT = createTMatrix(forearmL, 0);
		SimpleMatrix wristR = createRMatrix(wristNorm);
		SimpleMatrix wristT = createTMatrix(wristL+clawL, 0);
//		System.out.println(shoulderR.toString());
//		System.out.println(shoulderT.toString());
//		System.out.println(wristR.toString());
//		System.out.println(shoulderR.mult(shoulderT).toString());
		SimpleMatrix elbowTipPosition = shoulderR.mult(shoulderT).mult(elbowR).mult(elbowT);
//		System.out.println(elbowTipPosition.toString());
		SimpleMatrix clawPosition = elbowTipPosition.mult(wristR).mult(wristT);
//		System.out.println(clawPosition.toString());
		return tableCollisionCheck(elbowTipPosition) && tableCollisionCheck(clawPosition) && limbCollisionCheck(clawPosition, shoulderR);
	}
	
	private static boolean limbCollisionCheck(SimpleMatrix clawPosition, SimpleMatrix shoulderR) {
//		SimpleMatrix rotateMinus90 = createRMatrix(-90);
//		SimpleMatrix bicepW = createTMatrix(bicepR, 0);
//		// point A
//		SimpleMatrix edgePoint = shoulderR.mult(rotateMinus90).mult(bicepW);
//		double edgeLine[] = new double[] {edgePoint.get(0, 2), edgePoint.get(1,2), Math.tan(Math.acos(shoulderR.get(0, 0)))};
//		System.out.println("Parallel line at (Y-"+edgeLine[1]+") = "+edgeLine[2]+"*(X-"+edgeLine[0]);
//		// point B
//		double XAtYIsZero = xPointFromLineEq(edgeLine, 0);
//		// determinant of vectors AB (between both line points) and AQ (between point A and query point)
//		double determinant = (XAtYIsZero - edgeLine[0]) * (clawPosition.get(1, 2) - edgeLine[1]) - (0 - edgeLine[1]) * (clawPosition.get(0, 2) - edgeLine[0]);
//		System.out.println("Point A ("+edgePoint.get(0, 2)+","+edgePoint.get(1,2)+") Point B ("+XAtYIsZero+",0)");
//		System.out.println("determinant "+determinant);
		Boolean valid = true;
//		if(determinant > 0) {
//			valid = clawPosition.get(1,2)>bicepL*Math.sin(Math.acos(shoulderR.get(0, 0)));
//		}
		return valid;
	}
	
	private static double xPointFromLineEq(double line[], double y) {
		return (y-line[1])/(line[2])+line[0];
	}
	
	private static boolean tableCollisionCheck(SimpleMatrix clawPosition) {
		return clawPosition.get(1,2)>PADDING;
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
