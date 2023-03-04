#ifndef RobotArm_h
#define RobotArm_h

#include "Arduino.h"
#include <Adafruit_PWMServoDriver.h>

class RobotArm
{
  public:
  int PACKET_LENGTH = 10;
    byte PACKET_START = (byte)200;
    byte PACKET_STOP = (byte)300;
    RobotArm(int);
    void setShoulderRotation(int);
    void init(void);
    void setShoulder(int);
    void setElbow(int);
    void setWrist(int);
    void setWristRotation(int);
    void setFinger1(int);
    void setFinger2(int);
    void setFinger3(int);
    void setFinger4(int);
    void setFinger5(int);

    void waitForPacket(void);
    int* readPacket(void);
    void waitForPacketEnd(void);
    void updateFromPacket(int*);
    void sendPacket(int*);
  private:
    Adafruit_PWMServoDriver pwm;
    void setServoPwm(int servoNum, int pwmVal);
    void setServoMicroseconds(int servoNum, int us);
    int clipAngle(int);
};
#endif
