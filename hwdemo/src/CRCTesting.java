
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
            /*
            String bits = "";
            for (int i=0; i<positions.length; i++) {
                bits += Integer.toBinaryString(positions[i]);
            }
            System.out.println("raw converted: " + bits);
            int check1 = crc8(bits);
            System.out.println("CRC: "+check1);
            String crc = Integer.toBinaryString(check1);
            System.out.println("CRC: "+crc);
            String message = bits + crc;
            System.out.println("Message: "+message);
            int check2 = crc8(message.substring(0, message.length() - 7));
            System.out.println("Checked value: "+check2);
            */
            /*
            byte[] packetArray = {0,25,50,75,110,125,0,0,120,70};
            CRCPacket p = new CRCPacket(packetArray);
            byte[] msg = p.compile();
            for(byte b : msg) {
                System.out.print((b & 0xFF)+", ");
            }
            */
            /*
            System.out.println((byte)250);
            byte[] packetArray = {0,25,50,75,110,125,0,0,120,70};
            CRCPacket p = new CRCPacket(packetArray);
            System.out.println("CRC: "+p.computeCRC());
            */
            //byte abyte = (byte)170;
            //int aint = Byte.toUnsignedInt(abyte);
       
            byte[] packetArray = {0,25,50,75,110,125,0,0,120,(byte)170};
            //byte[] packetArray = {(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240,(byte)240};
            CRCPacket p = new CRCPacket(packetArray);
            Clock clock = Clock.systemDefaultZone();
            Arduino a = new Arduino(Arduino.detectArduino());
            a.open(BAUD_RATE);
            int c = 0;
            
            long startTime = clock.millis();
            while (c < 5000) {
                a.writePacket(p);
                c++;
                System.out.println("wrote packet " + c + ". listening..");
                byte rpacket[] = a.listenForAndReadPacket();
                if (rpacket[0] == CRCPacket.BADCRC) {
                    System.out.println("Received Bad PAcket (CRC ERROS ON LINE)");
                }
                System.out.println("got packet back: ");
                for (byte b : rpacket) {
                    System.out.print((b & 0xFF) + ", ");
                }
                System.out.println();
                //Thread.sleep(1);

                }
            long endTime = clock.millis();
            long diff = (endTime - startTime)/5000;
            System.out.println("Packet ms: " + diff);

    }
}
