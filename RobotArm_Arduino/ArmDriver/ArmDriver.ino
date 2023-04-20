#include <RobotArm.h>

#define BAUD_RATE 115200

RobotArm arm = RobotArm(BAUD_RATE);
int* packet;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(BAUD_RATE);
  arm.init();
}

void loop() {
  packet = arm.readPacket();
  arm.updateFromPacket(packet);
  arm.sendPacket(packet);
}
