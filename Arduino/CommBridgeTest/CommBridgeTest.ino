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
  // put your main code here, to run repeatedly:
  arm.waitForPacket();
  packet = arm.readPacket();
  arm.waitForPacketEnd();
//  Serial.print("Received packet: ");
//  for(int i=0;i<arm.PACKET_LENGTH;i++)
//  {
//    Serial.write(packet[i]);
//  }
//  Serial.println();
  arm.updateFromPacket(packet);
  arm.sendPacket(packet);
//   delay(100);
}
