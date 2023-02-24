import java.io.InputStream;

public class SerialTest {
	private static final int BAUD_RATE = 115200;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String returnedPacket;
		Packet p = new Packet();
		p.setShoulderRotation(90);
		p.setFinger(1,90);
		p.setFinger(2, 0);
		
//		Arduino a = new Arduino(Arduino.detectArduino());
//		a.open(BAUD_RATE);
//		while(true)
//		{
//			a.writePacket(p);
//			System.out.println("wrote packet. listening..");
//			byte rpacket[] = a.listenForAndReadPacket();
//			System.out.println("got packet back: ");
//			for(byte b : rpacket)
//			{
//				System.out.print((int)b+", ");
//			}
//			System.out.println();
//		}
//		byte compiled[] = p.compile();
//		for(byte b:compiled)
//		{
//			System.out.print(b+",");
//		}
//		System.out.println();
		
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open(BAUD_RATE);
//		InputStream in = a.serialPort.getInputStream();

		int f1 = 30;
		while(true)
		{
//			if(f1!=180)
//			{
//				f1+=30;
//			} else {
//				f1 = 0;
//			}
			p.setFinger(1,f1);
			a.writePacket(p);
			System.out.println("wrote packet: "+p.toString());
//			Thread.sleep(1000);
			returnedPacket = "";
//			if(in.available()>0)
//			{
			while(a.inputStream.available()<10);
			a.inputStream.read();
			for(int i=0;i<Packet.PACKET_LENGTH;i++)
			{
				
						int r = a.inputStream.read();
						returnedPacket+=(int)r+", ";
			}
			a.inputStream.read();
			System.out.println("received packet: "+returnedPacket);
			Thread.sleep(1000);
		}
		}
	}
