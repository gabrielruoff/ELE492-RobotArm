#include <RobotArm.h>

RobotArm arm = RobotArm();

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  // arm.init();
}

void loop() {
  // put your main code here, to run repeatedly:
  arm.waitForPacket();
  Serial.println("begin pcket");
}
