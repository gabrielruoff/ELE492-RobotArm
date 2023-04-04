import java.time.Clock;

import lib.Arduino;
import lib.Packet;

public class VarSpeedTest {
	private static final int BAUD_RATE = 115200;

	public static void main(String[] args) throws Exception {
		boolean sim = false;
		Packet neutral = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
		Clock clock = Clock.systemDefaultZone();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}

		// send default
		neutral.setWristRotation(0);
		neutral.setElbow(130);
		a.writePacketVarSpeed(neutral, 20, 20, sim);

		System.out.println("press enter");
		System.in.read();
		
		neutral.setWristRotation(90);
		neutral.setShoulder(40);
		a.writePacketVarSpeed(neutral, 20, 20, sim);
		
		System.out.println("press enter");
		System.in.read();
		
		neutral.setWristRotation(180);
		a.writePacketVarSpeed(neutral, 20, 20, sim);

//		// rotate wrist
//		neutral.setWristRotation(0);
//		neutral.setShoulderRotation(145);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setWristRotation(180);
//		neutral.setShoulderRotation(90);
//		a.writePacketVarSpeed(neutral, 60, 20);
//
//		System.out.println("press enter");
//		System.in.read();
//
//		// move shoulder
//		neutral.setShoulder(160);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setShoulder(50);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setShoulder(97);
//		a.writePacketVarSpeed(neutral, 60, 20);
//
//		System.out.println("press enter");
//		System.in.read();
//
//		// move elbow
//		neutral.setElbow(180);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setElbow(90);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		// move wrist
//		neutral.setWrist(0);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setWrist(180);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		neutral.setWrist(135);
//		a.writePacketVarSpeed(neutral, 60, 20);
//		// move elbow back to 0
//		neutral.setElbow(0);
//		a.writePacketVarSpeed(neutral, 60, 20);
//
//		System.out.println("press enter");
//		System.in.read();
//		for (int i = 1; i < 6; i++)
//			neutral.setFinger(i, 0);
//		a.writePacketVarSpeed(neutral, 60, 25);
//		for (int i = 1; i < 6; i++)
//			neutral.setFinger(i, 180);
//		a.writePacketVarSpeed(neutral, 60, 25);
//		for (int i = 1; i < 6; i++)
//			neutral.setFinger(i, 0);
//		a.writePacketVarSpeed(neutral, 60, 25);
//		for (int i = 1; i < 6; i++)
//			neutral.setFinger(i, 180);
//		a.writePacketVarSpeed(neutral, 60, 25);
	}
}
