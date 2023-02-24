
public class Packet {
	public static final byte START = (byte) 200;
	public static final byte STOP = (byte) 300;
	public static final int PACKET_LENGTH = 10;
	private static final int FINGERS_OFFSET = 4;
	private byte positions[];
	private int shoulderRotation = 0;
	private int shoulder = 1;
	private int elbow = 2;
	private int wristRotation = 4;
	private int wrist = 3;
	
	public Packet()
	{
		positions = new byte[PACKET_LENGTH];
		for(int i=0;i<positions.length;i++)
		{
			positions[i] = 0;
		}
	}
	
	public Packet(byte packetData[])
	{
		this.positions = packetData;
	}
	
	public byte[] compile()
	{
		byte bytes[] = new byte[PACKET_LENGTH+2];
		bytes[0] = START;
		for(int i=0;i<positions.length;i++)
		{
			bytes[i+1] = (byte) positions[i];
		}
		bytes[11] = STOP;
		return bytes;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for(byte b:this.compile())
		{
			s+=b+",";
		}
		return s;
	}
	
	public static boolean equals(Packet p1, Packet p2)
	{
		for(int i=0;i<PACKET_LENGTH;i++)
		{
			if(p1.positions[i] != p2.positions[i])
				return false;
		}
		return true;
	}
	
	public void setShoulderRotation(int val) {
		this.positions[shoulderRotation] = (byte) (val);
	}

	public void setShoulder(int val) {
		this.positions[shoulder] = (byte) (val);
	}

	public void setElbow(int val) {
		this.positions[elbow] = (byte) (val);
	}

	public void setWristRotation(int val) {
		this.positions[wristRotation] = (byte) (val);
	}

	public void setWrist(int val) {
		this.positions[wrist] = (byte) (val);
	}
	
	public void setFinger(int index, int val)
	{
		positions[FINGERS_OFFSET+index] = (byte) (val);
	}
	
}
