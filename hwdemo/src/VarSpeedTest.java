import java.time.Clock;

public class VarSpeedTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception
	{
		byte[] neutral = {(byte)90,(byte)90,(byte)90,(byte)90,(byte)90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180};
                Packet p = new Packet(neutral);
		Clock clock = Clock.systemDefaultZone();
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		System.out.println("Sending neutral");
                a.sendCommand(p, 10, 100);
                System.out.println("Sending Wrist and Shoulder");
                p.setWrist(0);
                p.setShoulder(97);
		a.sendCommand(p, 10, 100);
                
                p.setWrist(180);
		a.sendCommand(p, 10, 100);
                
                p.setWrist(90);
                p.setElbow(0);
		a.sendCommand(p, 1, 100);
                
                p.setElbow(160);
		a.sendCommand(p, 1, 100);
                
                p.setElbow(0);
                p.setShoulder(0);
		a.sendCommand(p, 1, 100);
                
                p.setShoulder(180);
		a.sendCommand(p, 1, 100);
                
                p.setShoulder(97);
		a.sendCommand(p, 1, 100);
	}
}
