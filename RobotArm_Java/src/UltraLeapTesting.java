import java.io.File;
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap.*;

class SampleListener2 extends Listener {
	Hand leftHand, rightHand;
	Pose lPose = null;
    Pose rPose = null;
    
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
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
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count());

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
        	else
        		lPose.update(leftHand);
        	System.out.println("Left:\n"+lPose.toString());
        }
        if(rightHand!=null) {
        	if(rPose==null)
        		rPose = new Pose(rightHand);
        	else
        		rPose.update(rightHand);
        	System.out.println("Right:\n"+rPose.toString());
        }
        
        // Sleep to allow for easier readability of output
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!frame.hands().isEmpty()) {
        	System.out.println();
        }
        	
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
}

class UltraLeapTesting {
    public static void main(String[] args) throws IOException {
    	File log = new File("log.csv");
    	if(log.exists())
    		log.delete();
    	log.createNewFile();
        // Create a sample listener and controller
        Listener listener = new SampleListener2();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
