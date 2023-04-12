package lib;

public class TransformedPose {
	public static final int shoulderRotation = 0;
	public static final int shoulder = 1;
	public static final int elbow = 2;
	public static final int wristRotation = 4;
	public static final int wrist = 3;
	public static final int FINGERS_OFFSET = 4;
	public static final int xMax = 500, xMin = 150;
	public static final int zMax = 200, zMin = -150;
	public static final int clawMin = 0, clawMax = 90;
	public float positions[] = {90,97,0,90,90,180,180,180,180,180};
	RangeMapping rWristAngleToWrist = new RangeMapping(40, 140);
	RangeMapping rightZToElbowJoint = new RangeMapping(zMin, zMax, RangeMapping.elbowMinAngle, RangeMapping.elbowMaxAngle);
	RangeMapping leftZToShoulderJoint = new RangeMapping(zMin, zMax, RangeMapping.shoulderMinAngle, RangeMapping.shoulderMaxAngle);
	RangeMapping rightXToWristRotation = new RangeMapping(xMin, xMax);
	RangeMapping leftXToShoulderRotation = new RangeMapping(-xMax, -xMin);
	RangeMapping RightGrabStrengthToClaw = new RangeMapping(0, 1, clawMin, clawMax);
	public TransformedPose(LRPose p) {
		if(p.left!=null) {
			// left x -> shoulder rotation
//			setShoulderRotation(leftXToShoulderRotation.fit(p.left.x));
			setShoulderRotation(Packet.defaultPositions[shoulderRotation]);
			// left z -> shoulder
			setShoulder(leftZToShoulderJoint.fit(p.left.z));
		} else {
			setShoulderRotation(Packet.defaultPositions[shoulderRotation]);
			setShoulder(Packet.defaultPositions[shoulder]);
			setElbow(Packet.defaultPositions[elbow]);
		}
		if(p.right!=null) {
			// right X -> elbow
//			System.out.println(p.right.x);
			setElbow(rightZToElbowJoint.fit(p.right.z));
			// right wrist -> wrist
			setWrist(rWristAngleToWrist.fit(p.right.wristAngle));
			// right z -> wrist rotation
			setWristRotation(rightXToWristRotation.fit(p.right.x));
			// grab strength -> finger1
			setFinger(1, RightGrabStrengthToClaw.fit(p.right.grabStrength));
		} else {
			setElbow(Packet.defaultPositions[elbow]);
			setWrist(Packet.defaultPositions[wrist]);
			setWristRotation(Packet.defaultPositions[wristRotation]);
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
	
	public void setFinger(int index, float val)
	{
		this.positions[FINGERS_OFFSET+index] = val;
	}
	
	@Override
	public String toString() {
		return "ShoulderRotation: "+positions[shoulderRotation]+"\nShoulder: "+positions[shoulder]+"\nElbow: "+positions[elbow]+"\nWristRotation: "+positions[wristRotation]+"\nWrist: "+positions[wrist]+"\nClaw: "+positions[FINGERS_OFFSET+1];
	}

}
