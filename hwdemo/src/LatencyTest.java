import java.time.*;

public class LatencyTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String returnedPacket;
		Packet p = new Packet();
		p.setShoulderRotation(90);
                p.setShoulder(90);
                p.setWrist(180);
		p.setFinger(1,180);
		p.setFinger(2, 0);
                p.setFinger(3, 0);
                p.setFinger(4, 0);
                p.setFinger(5, 0);
		
                Clock clock = Clock.systemDefaultZone();
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		int c = 0;
		//long startTime = clock.millis();
                while(true)
		{
                        a.writePacket(p);
			c++;
			System.out.println("wrote packet "+c+". listening..");
			byte rpacket[] = a.listenForAndReadPacket();
                        System.out.println("got packet back: ");
			
                        for(byte b : rpacket)
			{
				System.out.print((b & 0xFF)+", ");
			}
			if(!p.equals(new Packet(rpacket)))
			{
				System.out.println("Malformed packet");
				break;
			}
			System.out.println();
			//Thread.sleep(1);
                        
		}
                //long endTime = clock.millis();
                //long diff = (endTime - startTime)/5000;
                //System.out.println("Packet ms: " + diff);
                
	}
}