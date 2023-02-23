import java.io.InputStream;

public class SerialTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String returnedPacket;
		Packet p = new Packet();
		p.setShoulderRotation(90);
//		byte compiled[] = p.compile();
//		for(byte b:compiled)
//		{
//			System.out.print(b+",");
//		}
//		System.out.println();
		
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
		InputStream in = a.getInputStream();

		while(true)
		{
			a.writePacket(p);
			System.out.println("wrote packet: "+p.toString());
//			Thread.sleep(1000);
			returnedPacket = "";
			if(in.available()>0)
			{
			for(int i=0;i<Packet.PACKET_LENGTH;i++)
			{
				while(in.available()>0)
					{
						int r = in.read();
						returnedPacket+=(int)r+", ";
					}
			}
			System.out.println("received packet: "+returnedPacket);
			Thread.sleep(1000);
		}
		}
	}
}
