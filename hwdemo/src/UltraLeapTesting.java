import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

import lib.Arduino;
import lib.LRPose;
import lib.LogFile;
import lib.Packet;
import lib.TransformedPose;
import lib.UltraleapListener;


class UltraLeapTesting extends ArmTest {
	static boolean sim = true;
	static BlockingQueue<LRPose> queue = new ArrayBlockingQueue<>(1);
	
	static Packet idle = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
	static Packet target = new Packet(new byte[] {90,97,0,90,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180});
	static Arduino a;
	
	static LogFile log;

	
    public static void main(String[] args) throws Exception {
    	log = new LogFile("arm.csv");
    	long start = System.nanoTime();
    	if(sim)
    		a = new Arduino(null);
    	else {
    		a = new Arduino(Arduino.detectArduino());
    		a.open(BAUD_RATE);
    	}
    	
        // Create a sample listener and controller
        Listener listener = new UltraleapListener(queue);
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until a key is pressed
       while(true)
        {
    	   if(System.in.available()>0)
			{
				break;
			}
        	if(queue.peek()!=null)
        	{
        		LRPose item = queue.take();
//        		System.out.println("latest value: ");
//        		if(item.left!=null)
//        			System.out.println(item.left.toString());
//        		if(item.right!=null)
//        			System.out.println(item.right.toString());
        		TransformedPose newPose = new TransformedPose(item);
//        		System.out.println(newPose.toString());
        		target = new Packet(newPose);
        	}
        	else {
//        		System.out.println("no new value");
        	}
        	String line[] = {Long.toString(System.nanoTime()-start), Integer.toString(a.floatingTarget.positions[Packet.elbow]& 0xFF),
        			Integer.toString(a.oldPacket.positions[Packet.elbow]& 0xFF)};
        	log.writeLine(line);
        	a.setFloatingTarget(target);
			a.moveToFloatingTarget(60, true);
			System.out.println("wrote "+a.oldPacket.toString());
//        	Thread.sleep(10);
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
