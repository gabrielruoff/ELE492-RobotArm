#include "Arduino.h"
#include "RobotArm.h"
#include <Adafruit_PWMServoDriver.h>

#define SERVO_FREQ 50
// shoulder rotation and elbow servo
#define FS5115M_SERVOMIN  422
#define FS5115M_SERVOMAX  2333
#define shoulder_rotation_servonum 0
#define elbow_servonum 3
// shoulder servos (us)
#define BB_SERVOMIN 525
#define BB_SERVOMAX 2250
#define shoulder_servonum 1
// shoulder rotation and wrist servo
#define FS5103B_SERVOMIN  500
#define FS5103B_SERVOMAX  2231
#define wrist_servonum 4
// Hand Servos
#define LFD_01_SERVOMIN  500
#define LFD_01_SERVOMAX  2500
#define Finger1_servonum 5
#define Finger2_servonum 6
#define Finger3_servonum 7
#define Finger4_servonum 8
#define Finger5_servonum 9

RobotArm::RobotArm()
{
    Serial.begin(9600);
}

void RobotArm::init()
{
    pwm = Adafruit_PWMServoDriver(0x40);
    pwm.begin();
    pwm.setOscillatorFrequency(27000000);
    pwm.setPWMFreq(SERVO_FREQ);
    delay(10);
}

void RobotArm::setShoulderRotation(int theta)
{
    int pwmVal = (theta/180.0)*(FS5115M_SERVOMAX-FS5115M_SERVOMIN)+FS5115M_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_rotation_servonum, pwmVal);
}

void RobotArm::setShoulder(int theta)
{
    int pwmVal = (clipAngle(theta)/180.0)*(BB_SERVOMAX-BB_SERVOMIN)+BB_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_servonum, pwmVal);
}

void RobotArm::setElbow(int theta)
{
    int pwmVal = (theta/180.0)*(FS5115M_SERVOMAX-FS5115M_SERVOMIN)+FS5115M_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(elbow_servonum, pwmVal);
}

void RobotArm::setWrist(int theta)
{
    int pwmVal = (theta/180.0)*(FS5103B_SERVOMAX-FS5103B_SERVOMIN)+FS5103B_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(wrist_servonum, pwmVal);
}

void RobotArm::setFinger1(int theta)
{
    int theta = 180 - theta;
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger1_servonum, pwmVal);
}

void RobotArm::setFinger2(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger2_servonum, pwmVal);
}

void RobotArm::setFinger3(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger3_servonum, pwmVal);
}

void RobotArm::setFinger4(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger4_servonum, pwmVal);
}

void RobotArm::setFinger5(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger5_servonum, pwmVal);
}

void RobotArm::setServoPwm(int servoNum, int pwmVal)
{
    this->pwm.setPWM(servoNum, 0, pwmVal);
}

void RobotArm::setServoMicroseconds(int servoNum, int us)
{
    this->pwm.writeMicroseconds(servoNum, us);
}

int RobotArm::clipAngle(int theta)
{
    if(theta>=180)
    {
        return 180;
    } else if(theta<=0)
    {
        return 0;
    }
    return theta;
}
