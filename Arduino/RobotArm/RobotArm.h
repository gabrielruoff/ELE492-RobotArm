#ifndef RobotArm_h
#define RobotArm_h

#include "Arduino.h"
#include <Adafruit_PWMServoDriver.h>

class RobotArm
{
  public:
    RobotArm(void);
    void setShoulderRotation(int);
    void init(void);
    void setShoulder(int);
    void setElbow(int);
    void setWrist(int);
//    setWristRotation(int theta);
  private:
    Adafruit_PWMServoDriver pwm;
    void setServoPwm(int servoNum, int pwmVal);
    void setServoMicroseconds(int servoNum, int us);
    int clipAngle(int);
};

#endif
