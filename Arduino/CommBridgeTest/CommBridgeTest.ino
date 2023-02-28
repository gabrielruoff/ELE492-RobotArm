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
  /*
  arm.waitForPacket();
  packet = arm.readPacket();
  if (arm.verifyPacketCRC() == true) {
    arm.waitForPacketEnd();
    arm.updateFromPacket(packet);
    arm.sendPacket(packet);
  }
  else {
    arm.waitForPacketEnd();
    int badPacket [] = {230, 230, 230, 230, 230, 230, 230, 230, 230, 230};
    arm.sendPacket(packet);
  }
  */
  
  packet = arm.readPacket();
  arm.updateFromPacket(packet);
  //packet[10] = 12;
  arm.sendPacket(packet);
  
  /*
  Serial.print("Received packet: ");
  for(int i=0;i<arm.PACKET_LENGTH;i++)
  {
    Serial.write(packet[i]);
  }
  Serial.println();
  arm.updateFromPacket(packet);
  arm.sendPacket(packet);
  delay(100);
  */
  /*
  int val [] = {0,25,50,75,110,125,0,0,120,70};
  arm.updateValues(val);
  Serial.println("Values Updated");
  if (arm.verifyPacketCRC() == true) {
    Serial.println("CRC Verfied");
  }
  else {
    Serial.println("CRC BAD");
  }
  */
}
