package test;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Listener;

import lib.*;


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
        		LRPose newPose = queue.take();
//        		System.out.println("latest value: ");
//        		if(item.left!=null)
//        			System.out.println(item.left.toString());
//        		if(item.right!=null)
//        			System.out.println(item.right.toString());
        		TransformedPose newTPose = new TransformedPose(newPose);
        		System.out.println(newTPose.toString());
        		if(CollisionAvoidance.validatePosition(newTPose)) {
        			target = new Packet(newTPose);
        		} else {
        			System.out.println("invalid pose");
        		}
        	}
        	else {
//        		System.out.println("no new value");
        	}
        	// log
        	String line[] = {Long.toString(System.nanoTime()-start), Integer.toString(a.floatingTarget.positions[Packet.elbow] & 0xFF),
        			Integer.toString(a.oldPacket.positions[Packet.elbow] & 0xFF)};
        	log.writeLine(line);
        	a.setFloatingTarget(target);
			a.moveToFloatingTarget(90, sim);
//			System.out.println("wrote "+a.oldPacket.toString());
//        	Thread.sleep(100);
        }

        // Remove the listener when done
        controller.removeListener(listener);
    }
}
