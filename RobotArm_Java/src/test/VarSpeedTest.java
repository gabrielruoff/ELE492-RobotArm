package test;

import lib.Arduino;
import lib.Packet;

public class VarSpeedTest extends ArmTest {

	public static void main(String[] args) throws Exception {
		boolean sim = true;
		Packet neutral = Packet.getDefaultPacket();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}

		// send default
		a.writePacketVarSpeed(neutral, 40, 20, sim);

		System.out.println("press enter");
		System.in.read();
		
		neutral.setWristRotation(90);
		neutral.setShoulder(120);
                neutral.setElbow(45);
		a.writePacketVarSpeed(neutral, 40, 20, sim);
		
		System.out.println("press enter");
		System.in.read();
		
		neutral.setWristRotation(180);
                neutral.setElbow(0);
		a.writePacketVarSpeed(neutral, 40, 20, sim);

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
