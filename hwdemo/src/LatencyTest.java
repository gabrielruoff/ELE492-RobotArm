public class LatencyTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String returnedPacket;
		Packet p = new Packet();
		p.setShoulderRotation(90);
		p.setFinger(1,90);
		p.setFinger(2, 15);
		
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		int c = 0;
		while(true)
		{
			a.writePacket(p);
			c++;
			System.out.println("wrote packet "+c+". listening..");
			byte rpacket[] = a.listenForAndReadPacket();
			System.out.println("got packet back: ");
			for(byte b : rpacket)
			{
				System.out.print(b+", ");
			}
			if(!Packet.equals(p, new Packet(rpacket)))
			{
				System.out.println("Malformed packet");
				break;
			}
			System.out.println();
			Thread.sleep(1);
		}
	}
}