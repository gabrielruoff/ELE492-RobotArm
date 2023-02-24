#include "Arduino.h"
#include "RobotArm.h"
#include <Adafruit_PWMServoDriver.h>

#define SERVO_FREQ 50
// shoulder rotation and elbow servo
#define FS5115M_SERVOMIN  422
#define FS5115M_SERVOMAX  2333
#define shoulder_rotation_servonum 0
#define elbow_servonum 3
#define wrist_rotation_servonum 5
// shoulder servos (us)
#define BB_SERVOMIN 525
#define BB_SERVOMAX 2250
#define shoulder_servonum 1
// shoulder rotation and wrist servo
#define FS5103B_SERVOMIN  500
#define FS5103B_SERVOMAX  2400
#define wrist_servonum 4
// Hand Servos
#define LFD_01_SERVOMIN  500
#define LFD_01_SERVOMAX  2388
#define LFD_01_SERVOMIN_F1  500
#define LFD_01_SERVOMAX_F1  2100
#define Finger1_servonum 6
#define Finger2_servonum 7
#define Finger3_servonum 8
#define Finger4_servonum 9
#define Finger5_servonum 10

int values[10];

// int shoulderRotation = 0, shoulder = 0, elbow = 0, wirstRotation=0, wrist=0;
// int finger1 = 0, int finger2 = 0; int finger3 = 0; int finger4 = 0; int finger5 = 0;

RobotArm::RobotArm(int baudRate)
{
//    values = new int[PACKET_LENGTH];
    Serial.begin(baudRate);
}

void RobotArm::init()
{
    pwm = Adafruit_PWMServoDriver(0x40);
    pwm.begin();
    pwm.setOscillatorFrequency(27000000);
    pwm.setPWMFreq(SERVO_FREQ);
    delay(10);
    
    this->setShoulderRotation(90);
    this->setShoulder(90);
    this->setElbow(90);
    this->setWrist(90);
    this->setWristRotation(90);
    
    this->setFinger1(180);
    this->setFinger2(180);
    this->setFinger3(180);
    this->setFinger4(180);
    this->setFinger5(180);
}

void RobotArm::setShoulderRotation(int theta)
{
    int pwmVal = (theta/180.0)*(FS5115M_SERVOMAX-FS5115M_SERVOMIN)+FS5115M_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_rotation_servonum, pwmVal);
}

void RobotArm::setShoulder(int theta)
{
    int pwmVal = (clipAngle(theta)/180.0)*(BB_SERVOMAX-BB_SERVOMIN)+BB_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_servonum, pwmVal);
}

void RobotArm::setElbow(int theta)
{
    int pwmVal = (theta/180.0)*(FS5115M_SERVOMAX-FS5115M_SERVOMIN)+FS5115M_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(elbow_servonum, pwmVal);
}

void RobotArm::setWrist(int theta)
{
    int pwmVal = (theta/180.0)*(FS5103B_SERVOMAX-FS5103B_SERVOMIN)+FS5103B_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(wrist_servonum, pwmVal);
}

void RobotArm::setWristRotation(int theta)
{
    int pwmVal = (theta / 180.0) * (FS5115M_SERVOMAX - FS5115M_SERVOMIN) + FS5115M_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(wrist_rotation_servonum, pwmVal);
}

void RobotArm::setFinger1(int theta)
{
    theta = 180 - theta;
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX_F1 - LFD_01_SERVOMIN_F1) + LFD_01_SERVOMIN_F1;
//     Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger1_servonum, pwmVal);
}

void RobotArm::setFinger2(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
//    Serial.print("usVal: "); Serial.println(pwmVal);
    this->setServoMicroseconds(Finger2_servonum, pwmVal);
}

void RobotArm::setFinger3(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    //// Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger3_servonum, pwmVal);
}

void RobotArm::setFinger4(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    //// Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger4_servonum, pwmVal);
}

void RobotArm::setFinger5(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    //// Serial.print("usVal: "); // Serial.println(pwmVal);
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

void RobotArm::waitForPacket()
{
    while(true)
    {
        if(Serial.available()>0 && Serial.read() == PACKET_START)
        {
            return;
        }
    }
}

int* RobotArm::readPacket()
{
    for(int i=0;i<PACKET_LENGTH;i++)
    {
        while(true)
        {
            if(Serial.available()>0)
            {
                // int val =  Serial.read();
                // Serial.write(val);
                values[i] = Serial.read();
                break;
            }
        }
    };
    return values;
}

void RobotArm::waitForPacketEnd()
{
    while(true)
    {
        if(Serial.available()>0 && Serial.read() == PACKET_STOP)
        {
            return;
        }
    }
}

void RobotArm::updateFromPacket(int* packet)
{
//    for(int i=0;i<PACKET_LENGTH;i++)
//    {
//      Serial.write(packet[i]);
//    }
        this->setShoulderRotation(packet[0]);
        this->setShoulder(packet[1]);
        this->setElbow(packet[2]);
        this->setWrist(packet[3]);
        this->setWristRotation(packet[4]);
        this->setFinger1(packet[5]);
        this->setFinger2(packet[6]);
        this->setFinger3(packet[7]);
        this->setFinger4(packet[8]);
        this->setFinger5(packet[9]);
}

void RobotArm::sendPacket(int* packet)
{
    Serial.write(PACKET_START);
    for(int i=0;i<PACKET_LENGTH;i++)
    {
      Serial.write(packet[i]);
    }
    Serial.write(PACKET_STOP);
    Serial.flush();
}
