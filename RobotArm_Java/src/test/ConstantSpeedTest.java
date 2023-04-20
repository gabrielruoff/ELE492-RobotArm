package test;

import java.time.Clock;

import lib.Arduino;
import lib.Packet;

public class ConstantSpeedTest extends ArmTest {

	public static void main(String[] args) throws Exception {
		boolean sim = false;
		Packet neutral = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180, 90});
		Clock clock = Clock.systemDefaultZone();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}
		
//		neutral.setWristRotation(0);
//		neutral.setShoulder(80);
		neutral.setClaw(180);
		a.writePacketConstantSpeed(neutral, 90, sim);
//		
//		neutral.setWrist(180);
//		neutral.setShoulderRotation(0);
//		a.writePacketConstantSpeed(neutral, 90, sim);
//                System.in.read();
//                neutral.setShoulderRotation(90);
//		a.writePacketConstantSpeed(neutral, 90, sim);
//                System.in.read();
//                neutral.setShoulderRotation(180);
//		a.writePacketConstantSpeed(neutral, 90, sim);
	}
}
