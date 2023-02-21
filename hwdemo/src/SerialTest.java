import java.io.InputStream;

public class SerialTest {
	private static final int BAUD_RATE = 9600;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Packet p = new Packet();
		p.setShoulderRotation(90);
		byte compiled[] = p.compile();
		for(byte b:compiled)
		{
			System.out.print(b+",");
		}
		System.out.println();
		
		Arduino a = new Arduino(Arduino.detectArduino());
		a.open();
		
		InputStream in = a.getInputStream();
		System.out.println("wrote");
		while(true)
		{
			a.writePacket(p);
		Thread.sleep(2000);
			System.out.println("Available bytes "+in.available());
		while(in.available()>0)
		{
			int r = in.read();
			System.out.print((char)r);
		}
		}
	}
}
