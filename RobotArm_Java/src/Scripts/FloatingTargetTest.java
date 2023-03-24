package Scripts;

import OperatingClasses.*;
import java.time.Clock;

public class FloatingTargetTest extends ArmTest {

    public static void main(String[] args) throws Exception {
        boolean sim = true;
        Packet neutral = new Packet(new byte[]{90, 97, 0, 90, 90, (byte) 180, (byte) 180, (byte) 180, (byte) 180, (byte) 180});
        Clock clock = Clock.systemDefaultZone();
        Arduino a;
        if (sim) {
            a = new Arduino(null);
        } 
        else {
            a = new Arduino(Arduino.detectArduino());
            a.open(BAUD_RATE);
        }

        String input;
        while (true) {
            if (System.in.available() > 0) {
                input = new String(System.in.readNBytes(System.in.available() - 1));
                System.in.read();
                if (input.equals("x")) {
                    break;
                } else {
                    neutral.setWrist(Integer.parseInt(input));
                }
            }
            a.setFloatingTarget(neutral);
            a.moveToFloatingTarget(1, true);			
            //Thread.sleep(1500);
        }
    }
}
