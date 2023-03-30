package Scripts_Old;

import java.io.IOException;

import com.leapmotion.leap.Arm;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import lib.AlphaBetaFilter;

class SampleListener1 extends Listener {
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
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            double wristAngle = Math.toDegrees(arm.direction().angleTo(hand.direction()));

            // Create filter for wrist angle
            AlphaBetaFilter wristFilter = new AlphaBetaFilter(0.85, 0.05, wristAngle);
            double filteredWristAngle = wristFilter.filter(wristAngle, 0.1);
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition()
                             + ", wrist angle: " + wristAngle
                             + ", filtered wrist angle: " + filteredWristAngle);

            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                // Get Bones
                double fingerJointAngle = 0;
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);

                    // Label bones for visibility
                    Bone metacarpal = finger.bone(boneType.TYPE_METACARPAL);
                    Bone proximal = finger.bone(boneType.TYPE_PROXIMAL);
                    Bone intermediate = finger.bone(boneType.TYPE_INTERMEDIATE);
                    Bone distal = finger.bone(boneType.TYPE_DISTAL);

                    // Determine joint angle based on finger bone
                    if (bone.type() == Bone.Type.TYPE_METACARPAL) {
                        fingerJointAngle = Math.toDegrees(metacarpal.direction().angleTo(proximal.direction()));
                    }
                    else if (bone.type() == Bone.Type.TYPE_PROXIMAL ) {
                        fingerJointAngle = Math.toDegrees(proximal.direction().angleTo(intermediate.direction()));
                    }
                    else if (bone.type() == Bone.Type.TYPE_INTERMEDIATE) {
                        fingerJointAngle = Math.toDegrees(intermediate.direction().angleTo(distal.direction()));
                    }
                    else if (bone.type() == Bone.Type.TYPE_DISTAL) {
                        fingerJointAngle = 0;
                    }

                    // Create filter for finger joint angles
                    AlphaBetaFilter fingerJointFilter = new AlphaBetaFilter(0.85, 0.05, fingerJointAngle);
                    double filteredFingerJointAngle = fingerJointFilter.filter(fingerJointAngle, 0.1);
                    System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction()
                                     + ", angle: " + fingerJointAngle);
                }
            }
        }
        // Sleep to allow for easier readability of output
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
    }
}


class PrintAll {
    public static void main(String[] args) {
        // Create a sample listener and controller
        SampleListener1 listener = new SampleListener1();
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
