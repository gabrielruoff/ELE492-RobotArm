
import java.time.Clock;

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
            byte[] packetArray = {90,97,45,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180};
            byte[] packetArray1 = {90,97,0,90,0,(byte)0,(byte)0,(byte)0,(byte)0,(byte)0};
            //byte[] packetArray = {(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240};
            Packet p = new Packet(packetArray);
            Packet p1 = new Packet(packetArray1);
            Clock clock = Clock.systemDefaultZone();
            Arduino a = new Arduino(Arduino.detectArduino());
            a.open(BAUD_RATE);
            int c = 0;
            
            long startTime = clock.millis();
            /*
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
            
            a.writePacket(p1);
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
            
            a.writePacket(p);
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
            
            a.writePacket(p1);
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
            */
            
            while (true) {
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
                //Thread.sleep(1);

                }
                
            //long endTime = clock.millis();
            //long diff = (endTime - startTime)/5000;
            //System.out.println("Packet ms: " + diff);

    }
}
