import java.time.Clock;

public class FloatingTargetTest extends ArmTest {
	
	
	public static void main(String[] args) throws Exception {
		boolean sim = true;
		Packet neutral = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
		Clock clock = Clock.systemDefaultZone();
		Arduino a;
		if(sim)
			a = new Arduino(null);
		else {
			a = new Arduino(Arduino.detectArduino());
			a.open(BAUD_RATE);
		}
		
		neutral.setWrist(0);
		a.setFloatingTarget(neutral);
//		a.moveToFloatingTarget(9, true);
		for(int i =0;i<10;i++) {
			a.moveToFloatingTarget(20, true);
		}
		
		System.in.read();
		neutral.setWrist(180);
		a.setFloatingTarget(neutral);
		for(int i =0;i<10;i++) {
			a.moveToFloatingTarget(10, true);
		}
	}
}
