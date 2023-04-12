package Old_Scripts;

import java.time.Clock;

import lib.*;

public class MidSemesterDemo {
	private static final int BAUD_RATE = 115200;

	public static void main(String[] args) throws Exception {
		Packet neutral = new Packet(new byte[] { 90, 97, 90, 90, 90, (byte) 180, (byte) 180, (byte) 180, (byte) 180, (byte) 180 });
		byte[] packetArray = { 90, 97, 45, 90, (byte) 180, (byte) 180, (byte) 180, (byte) 180, (byte) 180, (byte) 180 };
		byte[] packetArray1 = { 90, 97, 0, 90, 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
		// byte[] packetArray =
		// {(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240};
		Packet p = new Packet(packetArray);
		Packet p1 = new Packet(packetArray1);
		Clock clock = Clock.systemDefaultZone();
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		int c = 0;

		long startTime = clock.millis();

		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		byte rpacket[] = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(500);

		neutral.setWrist(0);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(500);

		neutral.setWrist(180);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(500);

		neutral.setWrist(0);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(500);
		
		neutral.setWrist(90);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(2000);
		
		neutral.setElbow(0);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(1000);
		
//		neutral.setElbow(180);
//		a.writePacket(neutral);
//		System.out.println("wrote packet " + c + ". listening..");
//		rpacket = a.listenForAndReadPacket();
//		if (rpacket[0] == Packet.BADCRC) {
//			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
//		}
//		System.out.println("got packet back: ");
//		for (byte b : rpacket) {
//			System.out.print((b & 0xFF) + ", ");
//		}
//		System.out.println();
//		Thread.sleep(1000);
//		
//		neutral.setElbow(90);
//		a.writePacket(neutral);
//		System.out.println("wrote packet " + c + ". listening..");
//		rpacket = a.listenForAndReadPacket();
//		if (rpacket[0] == Packet.BADCRC) {
//			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
//		}
//		System.out.println("got packet back: ");
//		for (byte b : rpacket) {
//			System.out.print((b & 0xFF) + ", ");
//		}
//		System.out.println();
//		Thread.sleep(2000);
		
		neutral.setElbow(45);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(2000);
		
//		neutral.setShoulder(0);
//		a.writePacket(neutral);
//		System.out.println("wrote packet " + c + ". listening..");
//		rpacket = a.listenForAndReadPacket();
//		if (rpacket[0] == Packet.BADCRC) {
//			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
//		}
//		System.out.println("got packet back: ");
//		for (byte b : rpacket) {
//			System.out.print((b & 0xFF) + ", ");
//		}
//		System.out.println();
//		Thread.sleep(1000);
//		
		neutral.setShoulder(180);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(1000);
		
		neutral.setShoulder(90);
		a.writePacket(neutral);
		System.out.println("wrote packet " + c + ". listening..");
		rpacket = a.listenForAndReadPacket();
		if (rpacket[0] == Packet.BADCRC) {
			System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
		}
		System.out.println("got packet back: ");
		for (byte b : rpacket) {
			System.out.print((b & 0xFF) + ", ");
		}
		System.out.println();
		Thread.sleep(500);

	}

}
