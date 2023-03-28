
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import lib.LRPose;
import lib.LogFile;
import lib.Pose;

public class UltraleapListener extends Listener {
	Hand leftHand, rightHand;
	Pose lPose = null;
    Pose rPose = null;
    BlockingQueue<LRPose> queue;
    
    LogFile log;
    
    public UltraleapListener(BlockingQueue<LRPose> q)
    {
    	queue = q;
    }
    
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        try {
			log = new LogFile("leap.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
//        System.out.println("Frame id: " + frame.id()
//                         + ", timestamp: " + frame.timestamp()
//                         + ", hands: " + frame.hands().count()
//                         + ", fingers: " + frame.fingers().count());

        //Get hands
        leftHand = null;
        rightHand = null;
        for(Hand hand : frame.hands()) {
            if(hand.isLeft())
            	leftHand = hand;
            else
            	rightHand = hand;
        }
        
        if(leftHand!=null) {
        	if(lPose==null)
        		lPose = new Pose(leftHand);
        	else {
        		lPose.update(leftHand);
        	}
//        	System.out.println("Left:\n"+lPose.toString());
        } else {
        	lPose = null;
        }
        if(rightHand!=null) {
        	if(rPose==null)
        		rPose = new Pose(rightHand);
        	else
        		rPose.update(rightHand);
//        	System.out.println("Right:\n"+rPose.toString());
        } else {
        	rPose = null;
        }
        
        try {
			if(queue.remainingCapacity()==0)
				queue.remove();
			queue.put(new LRPose(lPose, rPose));
			double rawWristAngle = Math.toDegrees(leftHand.arm().direction().angleTo(leftHand.palmNormal()));
			String line[] = {Long.toString(frame.id()), Double.toString(rawWristAngle), Double.toString(lPose.getWristAngle())};
			log.writeLine(line);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Sleep to allow for easier readability of output
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

//        if (!frame.hands().isEmpty()) {
//        	System.out.println();
//        }
        	
    }
    
    public void printHandInfo(Hand h) {
    	if(h.isLeft())
    		System.out.print("Left ");
    	else
    		System.out.print("Right ");
    	System.out.println("hand:");
    	Arm arm = h.arm();
    	Vector palmNormal = h.palmNormal();
//    	System.out.print("Palm normal "); System.out.println(palmNormal.toString());
//    	System.out.print("Palm direction "); System.out.println(h.direction().toString());
    	double elbowAngle = Math.toDegrees(arm.direction().pitch());
//    	System.out.println("  pitch: " + Math.toDegrees(arm.direction().pitch()) + " degrees, "
//+ "roll: " + Math.toDegrees(arm.direction().roll()) + " degrees, "
//+ "yaw: " + Math.toDegrees(arm.direction().yaw()) + " degrees");
    	System.out.print("Elbow Angle :"); System.out.println(elbowAngle);
    	double wristAngle = Math.toDegrees(arm.direction().angleTo(h.palmNormal()));
    	System.out.print("Angle vs Arm:"); System.out.println(wristAngle);
    }

public void writeToLog(double t, double unfiltered, double filtered)
{
	
}

}