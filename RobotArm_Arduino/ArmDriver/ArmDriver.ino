#include <RobotArm.h>

#define BAUD_RATE 115200

RobotArm arm = RobotArm(BAUD_RATE);
int* packet;

void setup() {
  Serial.begin(BAUD_RATE);
  arm.init();
}

void loop() {
  //Read Next Packet
  packet = arm.readPacket();
  //Update Servo Positions Internally
  arm.updateFromPacket();
  //Send Packet Back
  arm.sendPacket(packet);
}
