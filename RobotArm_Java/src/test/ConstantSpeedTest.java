package test;

import lib.Arduino;
import lib.Packet;

public class ConstantSpeedTest extends ArmTest {

	public static void main(String[] args) throws Exception {
		boolean sim = false;
		Packet neutral = Packet.getDefaultPacket();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}
		
//		neutral.setShoulder(180);
		neutral.setClaw(0);
		a.writePacketConstantSpeed(neutral, 200, sim);
		System.in.read();
		
//		neutral.setShoulder(0);
		neutral.setClaw(90);
		a.writePacketConstantSpeed(neutral, 200, sim);
		
//		System.in.read();
////		neutral.setShoulder(180);
//		neutral.setClaw(0);
//		a.writePacketConstantSpeed(neutral, 200, sim);
	}
}
