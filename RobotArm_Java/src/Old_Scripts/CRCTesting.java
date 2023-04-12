package Old_Scripts;

import java.time.Clock;

import lib.Arduino;
import lib.Packet;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author denja
 */
public class CRCTesting {
    private static final int BAUD_RATE = 115200;
    
    public static void main(String[] args) throws Exception {
    		Packet neutral = new Packet(new byte[] {90,97,90,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
//            byte[] packetArray = {90,97,45,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180};
//            byte[] packetArray1 = {90,97,0,90,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0};
            //byte[] packetArray = {(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240};
//            Packet p = new Packet(packetArray);
//            Packet p1 = new Packet(packetArray1);
            Clock clock = Clock.systemDefaultZone();
            Arduino a = new Arduino(Arduino.detectArduino());
            a.open(BAUD_RATE);
            int c = 0;
            
            long startTime = clock.millis();
            
            a.writePacket(neutral);
            
            for(int i=90;i>0;i-=1)
            {
	            neutral.setWrist(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            Thread.sleep(1000);
            for(int i=90;i<180;i++)
            {
	            neutral.setElbow(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            Thread.sleep(1000);
            for(int i=180;i>0;i-=1)
            {
	            neutral.setElbow(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            Thread.sleep(1000);
            for(int i=97;i<180;i++)
            {
	            neutral.setShoulder(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            Thread.sleep(1000);
            for(int i=180;i>45;i--)
            {
	            neutral.setShoulder(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            Thread.sleep(1000);
            for(int i=45;i<97;i++)
            {
	            neutral.setShoulder(i);
	            a.writePacket(neutral);
	            Thread.sleep(50);
            }
            
//            neutral.setWrist(180);
//            a.writePacket(neutral);
//            
//            neutral.setWrist(0);
//            a.writePacket(neutral);
            
//            neutral.setWrist((byte)180);
//            a.writePacket(neutral);
//            System.out.println("wrote packet " + c + ". listening..");
//            rpacket = a.listenForAndReadPacket();
//            if (rpacket[0] == Packet.BADCRC) {
//                System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
//            }
//            System.out.println("got packet back: ");
//            for (byte b : rpacket) {
//                System.out.print((b & 0xFF) + ", ");
//            }
//            System.out.println();
//            Thread.sleep(1);
            
//            neutral.setWrist(90);
//            a.writePacket(neutral);
//            System.out.println("wrote packet " + c + ". listening..");
//            rpacket = a.listenForAndReadPacket();
//            if (rpacket[0] == Packet.BADCRC) {
//                System.out.println("Received Bad PAcket (CRC ERRORS ON LINE)");
//            }
//            System.out.println("got packet back: ");
//            for (byte b : rpacket) {
//                System.out.print((b & 0xFF) + ", ");
//            }
//            System.out.println();
//            Thread.sleep(1);

            /*while (true) {
                c++;
                if ((c % 2) == 0) {
                    a.writePacket(p);
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
                }
                else {
                    a.writePacket(p1);
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
                    }
                Thread.sleep(1);

                }*/
                
            //long endTime = clock.millis();
            //long diff = (endTime - startTime)/5000;
            //System.out.println("Packet ms: " + diff);

    }
}
