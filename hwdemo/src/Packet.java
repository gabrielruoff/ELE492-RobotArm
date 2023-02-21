
public class Packet {
	private static final byte START = 's';
	private static final byte STOP = 'e';
	private static final byte DELIMITER = '!';
	private int shoulderRotation;
	private int shoulder;
	private int elbow;
	private int wristRotation;
	private int wrist;
	private int[] fingers;
	
	public Packet()
	{
		fingers = new int[5];
	}

	public void setShoulderRotation(int shoulderRotation) {
		this.shoulderRotation = shoulderRotation;
	}

	public void setShoulder(int shoulder) {
		this.shoulder = shoulder;
	}

	public void setElbow(int elbow) {
		this.elbow = elbow;
	}

	public void setWristRotation(int wristRotation) {
		this.wristRotation = wristRotation;
	}

	public void setWrist(int wrist) {
		this.wrist = wrist;
	}

	public void setFingers(int[] fingers) {
		this.fingers = fingers;
	}
	
	public void setFinger(int index, int val)
	{
		fingers[index] = val;
	}
	
	public byte[] compile()
	{
		byte bytes[] = new byte[21];
		for(int i=2;i<21;i+=2)
		{
			bytes[i] = DELIMITER;
		}
		bytes[0] = START;
		bytes[1] = (byte) shoulderRotation;
		bytes[3] = (byte) shoulder;
		bytes[5] = (byte) elbow;
		bytes[7] = (byte) wrist;
		bytes[9] = (byte) wristRotation;
		for(int i=0;i<fingers.length;i+=2)
		{
			bytes[11+i] = (byte) fingers[i];
		}
		bytes[20] = STOP;
		return bytes;
	}
	
}
