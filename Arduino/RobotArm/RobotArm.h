#ifndef RobotArm_h
#define RobotArm_h

#include "Arduino.h"
#include <Adafruit_PWMServoDriver.h>


class RobotArm
{
  public:
	int PACKET_LENGTH = 10;
    byte PACKET_START = (byte)200;
    byte PACKET_STOP = (byte)250;
	byte PACKET_BADCRC = (byte)240;
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
    void updateArm();

    void waitForPacketStart(void);
	void digestPacket(void);
	void waitForPacketEnd(void);
    int* readPacket(void);
	bool verifyPacketCRC(void);
	int calculateCRC(void);
	int calculateCRC(int*);
	void updateValues(int*);
    void updateFromPacket(int*);
    void sendPacket(int*);
  private:
    Adafruit_PWMServoDriver pwm;
    void setServoPwm(int servoNum, int pwmVal);
    void setServoMicroseconds(int servoNum, int us);
    int clipAngle(int);
    int clipAngleShoulder(int);
    int clipAngleElbow(int);

    void jointHandler(int, int, void (RobotArm::*)(int));
};

#endif
