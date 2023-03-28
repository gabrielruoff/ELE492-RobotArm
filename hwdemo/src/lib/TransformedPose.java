package lib;

public class TransformedPose {
	private int shoulderRotation = 0;
	private int shoulder = 1;
	private int elbow = 2;
	private int wristRotation = 4;
	private int wrist = 3;
	public float positions[] = {90,97,0,90,90,180,180,180,180,180};
	RangeMapping lWristToElbowJoint = new RangeMapping(40, 140);
	RangeMapping lElbowToShoulderJoint = new RangeMapping(0, 45);
	public TransformedPose(LRPose p) {
		if(p.left!=null) {
			// left elbow -> shoulder
			setShoulder(lElbowToShoulderJoint.fit(p.left.elbowAngle));
			// left wrist -> elbow
			setElbow(lWristToElbowJoint.fit(p.left.wristAngle));
		} else {
			setShoulder(Packet.defaultPositions[shoulder]);
			setElbow(Packet.defaultPositions[elbow]);
		}
		if(p.right!=null) {
			// right wrist -> wrist
			setWrist(lWristToElbowJoint.fit(p.right.wristAngle));
		} else {
			setWrist(Packet.defaultPositions[wrist]);
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
	
	@Override
	public String toString() {
		return "Shoulder: "+positions[shoulder]+"\nElbow: "+positions[elbow]+"\nWrist: "+positions[wrist];
	}

}
