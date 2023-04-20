package lib;

import com.leapmotion.leap.Finger;

public class TransformedPose {
	public static final int shoulderRotation = 0;
	public static final int shoulder = 1;
	public static final int elbow = 2;
	public static final int wristRotation = 4;
	public static final int wrist = 3;
	public static final int FINGERS_OFFSET = 4;
	public static final int claw = 10;
	public static final int xMax = 500, xMin = 150;
	public static final int zMax = 200, zMin = -150;
	public static final int clawMin = 0, clawMax = 90;
	public float positions[] = { 90, 97, 0, 90, 90, 180, 180, 180, 180, 180, 90 };
	RangeMapping rWristAngleToWrist = new RangeMapping(140, 40);
	RangeMapping rightZToElbowJoint = new RangeMapping(zMax, zMin, RangeMapping.elbowMinAngle,
			RangeMapping.elbowMaxAngle);
	RangeMapping leftZToShoulderJoint = new RangeMapping(zMin, zMax, RangeMapping.shoulderMinAngle,
			RangeMapping.shoulderMaxAngle);
	RangeMapping rightXToWristRotation = new RangeMapping(xMin, xMax);
	RangeMapping leftXToShoulderRotation = new RangeMapping(-xMax, -xMin, 90, 180);
	RangeMapping RightGrabStrengthToClaw = new RangeMapping(1, 0, clawMin, clawMax);
	RangeMapping fingers = new RangeMapping(3, 0);
	RangeMapping thumb = new RangeMapping(1, 0);
	public static int MODE_ARM = 0, MODE_HAND = 1;

	public TransformedPose(LRPose p, int mode) {
		if (p.left != null && mode == MODE_ARM) {
			// left x -> shoulder rotation
			setShoulderRotation(leftXToShoulderRotation.fit(p.left.x));
			// setShoulderRotation(Packet.defaultPositions[shoulderRotation]);
			// left z -> shoulder
//			setShoulder(leftZToShoulderJoint.fit(p.left.z));
			setShoulder(Packet.defaultPositions[shoulder]);
			// left grab strength -> claw
			setClaw(RightGrabStrengthToClaw.fit(p.left.grabStrength));
		} else {
			setShoulderRotation(Packet.defaultPositions[shoulderRotation]);
			setShoulder(Packet.defaultPositions[shoulder]);
			setElbow(Packet.defaultPositions[elbow]);
			setClaw(Packet.defaultPositions[claw]);
		}
		if (p.right != null && mode == MODE_ARM) {
			// right X -> elbow
//			System.out.println(p.right.x);
			setElbow(rightZToElbowJoint.fit(p.right.z));
			// right wrist -> wrist
			setWrist(rWristAngleToWrist.fit(p.right.wristAngle));
			// right z -> wrist rotation
			setWristRotation(rightXToWristRotation.fit(p.right.x));
//			// right grab strength -> claw
//			setClaw(RightGrabStrengthToClaw.fit(p.right.grabStrength));
		} else {
			setElbow(Packet.defaultPositions[elbow]);
			setWrist(Packet.defaultPositions[wrist]);
			setWristRotation(Packet.defaultPositions[wristRotation]);
			// right fingers -> fingers
                        if(p.right != null) {
			setFinger(1, thumb.fit(p.right.fingerAngles.get(Finger.Type.TYPE_THUMB)));
			setFinger(2, fingers.fit(p.right.fingerAngles.get(Finger.Type.TYPE_INDEX)));
			setFinger(3, fingers.fit(p.right.fingerAngles.get(Finger.Type.TYPE_MIDDLE)));
			setFinger(4, fingers.fit(p.right.fingerAngles.get(Finger.Type.TYPE_RING)));
			setFinger(5, fingers.fit(p.right.fingerAngles.get(Finger.Type.TYPE_PINKY)));
                        }
		}
	}

	public void setShoulderRotation(float val) {
		this.positions[shoulderRotation] = val;
	}

	public void setShoulder(float val) {
		this.positions[shoulder] = val;
	}

	public void setElbow(float val) {
		this.positions[elbow] = val;
	}

	public void setWristRotation(float val) {
		this.positions[wristRotation] = val;
	}

	public void setWrist(float val) {
		this.positions[wrist] = val;
	}

	public void setFinger(int index, float val) {
		this.positions[FINGERS_OFFSET + index] = val;
	}
	
	public void setClaw(float val)
	{
		this.positions[claw] = val;
	}

	@Override
	public String toString() {
		return "ShoulderRotation: " + positions[shoulderRotation] + "\nShoulder: " + positions[shoulder] + "\nElbow: "
				+ positions[elbow] + "\nWristRotation: " + positions[wristRotation] + "\nWrist: " + positions[wrist]
				+ "\nClaw: " + positions[claw]
				+"\nThumb: "+positions[FINGERS_OFFSET+1]
				+ "\nIndex: " + positions[FINGERS_OFFSET + 2] + "\nMiddle: " + positions[FINGERS_OFFSET + 3]
				+ "\nRing: " + positions[FINGERS_OFFSET + 4] + "\nPinky: " + positions[FINGERS_OFFSET + 5];
	}

}
