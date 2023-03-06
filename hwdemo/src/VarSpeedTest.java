import java.time.Clock;

public class VarSpeedTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception
	{
		Packet neutral = new Packet(new byte[] { 90, 97, 90, 90, 90, (byte) 180, (byte) 180, (byte) 180, (byte) 180, (byte) 180 });
		Clock clock = Clock.systemDefaultZone();
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		
		
		a.writePacketVarSpeed(neutral, 10, 100);
		
		// move wrist
		neutral.setWrist(0);
		a.writePacketVarSpeed(neutral, 10, 100);
		neutral.setWrist(180);
		a.writePacketVarSpeed(neutral, 10, 100);
//		neutral.setWrist(90);
//		a.writePacketVarSpeed(neutral, 10,100);
		
		

	}
}
