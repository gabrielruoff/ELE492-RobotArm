package Test_Scripts;

import java.time.Clock;

import Operating_Classes.*;

public class ConstantSpeedTest extends ArmTest {

	public static void main(String[] args) throws Exception {
		boolean sim = true;
		Packet neutral = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
		Clock clock = Clock.systemDefaultZone();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}
		
		neutral.setWristRotation(0);
//		neutral.setShoulder(80);
		a.writePacketConstantSpeed(neutral, 90, sim);
		
		neutral.setWrist(180);
		neutral.setShoulder(120);
		a.writePacketConstantSpeed(neutral, 90, sim);
	}
}