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
//Servo Speed
#define Servo_Speed 100
#define Servo_Angle_Steps 10

//CRC Hash Table
int CRC8_TABLE [] = { 0x00, 0x5e, 0xbc, 0xe2, 0x61, 0x3f, 0xdd, 0x83, 0xc2, 0x9c, 0x7e,
			0x20, 0xa3, 0xfd, 0x1f, 0x41, 0x9d, 0xc3, 0x21, 0x7f, 0xfc, 0xa2, 0x40, 0x1e, 0x5f, 0x01, 0xe3, 0xbd,
			0x3e, 0x60, 0x82, 0xdc, 0x23, 0x7d, 0x9f, 0xc1, 0x42, 0x1c, 0xfe, 0xa0, 0xe1, 0xbf, 0x5d, 0x03, 0x80,
			0xde, 0x3c, 0x62, 0xbe, 0xe0, 0x02, 0x5c, 0xdf, 0x81, 0x63, 0x3d, 0x7c, 0x22, 0xc0, 0x9e, 0x1d, 0x43,
			0xa1, 0xff, 0x46, 0x18, 0xfa, 0xa4, 0x27, 0x79, 0x9b, 0xc5, 0x84, 0xda, 0x38, 0x66, 0xe5, 0xbb, 0x59,
			0x07, 0xdb, 0x85, 0x67, 0x39, 0xba, 0xe4, 0x06, 0x58, 0x19, 0x47, 0xa5, 0xfb, 0x78, 0x26, 0xc4, 0x9a,
			0x65, 0x3b, 0xd9, 0x87, 0x04, 0x5a, 0xb8, 0xe6, 0xa7, 0xf9, 0x1b, 0x45, 0xc6, 0x98, 0x7a, 0x24, 0xf8,
			0xa6, 0x44, 0x1a, 0x99, 0xc7, 0x25, 0x7b, 0x3a, 0x64, 0x86, 0xd8, 0x5b, 0x05, 0xe7, 0xb9, 0x8c, 0xd2,
			0x30, 0x6e, 0xed, 0xb3, 0x51, 0x0f, 0x4e, 0x10, 0xf2, 0xac, 0x2f, 0x71, 0x93, 0xcd, 0x11, 0x4f, 0xad,
			0xf3, 0x70, 0x2e, 0xcc, 0x92, 0xd3, 0x8d, 0x6f, 0x31, 0xb2, 0xec, 0x0e, 0x50, 0xaf, 0xf1, 0x13, 0x4d,
			0xce, 0x90, 0x72, 0x2c, 0x6d, 0x33, 0xd1, 0x8f, 0x0c, 0x52, 0xb0, 0xee, 0x32, 0x6c, 0x8e, 0xd0, 0x53,
			0x0d, 0xef, 0xb1, 0xf0, 0xae, 0x4c, 0x12, 0x91, 0xcf, 0x2d, 0x73, 0xca, 0x94, 0x76, 0x28, 0xab, 0xf5,
			0x17, 0x49, 0x08, 0x56, 0xb4, 0xea, 0x69, 0x37, 0xd5, 0x8b, 0x57, 0x09, 0xeb, 0xb5, 0x36, 0x68, 0x8a,
			0xd4, 0x95, 0xcb, 0x29, 0x77, 0xf4, 0xaa, 0x48, 0x16, 0xe9, 0xb7, 0x55, 0x0b, 0x88, 0xd6, 0x34, 0x6a,
			0x2b, 0x75, 0x97, 0xc9, 0x4a, 0x14, 0xf6, 0xa8, 0x74, 0x2a, 0xc8, 0x96, 0x15, 0x4b, 0xa9, 0xf7, 0xb6,
			0xe8, 0x0a, 0x54, 0xd7, 0x89, 0x6b, 0x35 };

//Current Value and Previous Value Holders
int values[11];
int prevvalues[11];

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

    //Initialize Angles
    values[0] = 90;
    values[1] = 90;
    values[2] = 90;
    values[3] = 90;
    values[4] = 90;
    values[5] = 180;
    values[6] = 180;
    values[7] = 180;
    values[8] = 180;
    values[9] = 180;
    //CRC Bit
    values[10] = 202;
    //Initialize previous values to intial angles
    for (int i = 0; i < 11; i++) {
        prevvalues[i] = values[i];
    }

    //Move Servos to intial angles
    updateArm();

    //Flush Serial Buffer
	Serial.flush();
}



// Servo-Specific Functions
// Servo Angle-Sets
void RobotArm::setShoulderRotation(int theta)
{
    int pwmVal = (theta/180.0)*(FS5115M_SERVOMAX-FS5115M_SERVOMIN)+FS5115M_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_rotation_servonum, pwmVal);
}

void RobotArm::setShoulder(int theta)
{
    int servo2Offset = 4;
    int pwmVal1 = (clipAngleShoulder(theta)/180.0)*(BB_SERVOMAX-BB_SERVOMIN)+BB_SERVOMIN;
    int pwmVal2 = (clipAngleShoulder(180 - theta - servo2Offset) / 180.0) * (BB_SERVOMAX - BB_SERVOMIN) + BB_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(shoulder_servonum, pwmVal1);
    this->setServoMicroseconds(shoulder_servonum+1, pwmVal2);
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
//    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX_F1 - LFD_01_SERVOMIN_F1) + LFD_01_SERVOMIN_F1;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger1_servonum, pwmVal);
}

void RobotArm::setFinger2(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger2_servonum, pwmVal);
}

void RobotArm::setFinger3(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger3_servonum, pwmVal);
}

void RobotArm::setFinger4(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger4_servonum, pwmVal);
}

void RobotArm::setFinger5(int theta)
{
    int pwmVal = (theta / 180.0) * (LFD_01_SERVOMAX - LFD_01_SERVOMIN) + LFD_01_SERVOMIN;
    // Serial.print("usVal: "); // Serial.println(pwmVal);
    this->setServoMicroseconds(Finger5_servonum, pwmVal);
}

// Servo Driver Functions
void RobotArm::setServoPwm(int servoNum, int pwmVal)
{
    this->pwm.setPWM(servoNum, 0, pwmVal);
}

void RobotArm::setServoMicroseconds(int servoNum, int us)
{
    this->pwm.writeMicroseconds(servoNum, us);
}

//Angle Clipping Functions
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

int RobotArm::clipAngleShoulder(int theta)
{
    if (theta >= 160)
    {
        return 160;
    }
    else if (theta <= 20)
    {
        return 20;
    }
    return theta;
}


void RobotArm::jointHandler(int index, int i, void (RobotArm::*func)(int))
{
    if (prevvalues[index] > values[index]) {
        (this->*func)(prevvalues[index] - i);
        prevvalues[index] = prevvalues[index] - i;
    }
    else if (prevvalues[index] < values[index]) {
        (this->*func)(prevvalues[index] + i);
        prevvalues[index] = prevvalues[index] - i;
    }
    else if (prevvalues[index] == values[index]) {
        (this->*func)(values[index]);
    }
}


// Packet Functions
// Packet Debugging Functions
void RobotArm::updateValues(int* packet)
{
    for (int i = 0; i < PACKET_LENGTH; i++)
    {
        values[i] = packet[i];
    }
}
// Regular Packet Functions
void RobotArm::waitForPacketStart()
{
    while(true)
    {
        if(Serial.available()>0 && Serial.read() == PACKET_START)
        {
            return;
        }
    }
}

void RobotArm::digestPacket()
{
	for (int i = 0; i < PACKET_LENGTH + 1; i++)
	{
		while (true)
		{
			if (Serial.available() > 0)
			{
				values[i] = Serial.read();
				break;
			}
		}
	}
	return;
}

void RobotArm::waitForPacketEnd()
{
	while (true)
	{
		if (Serial.available() > 0 && Serial.read() == PACKET_STOP)
		{
			return;
		}
	}
}

bool RobotArm::verifyPacketCRC()
{
    int readCRC = values[10];
    int crc = this->calculateCRC();
    if (crc == readCRC) {
        return true;
    }
    else {
        return false;
    }
}

int RobotArm::calculateCRC()
{
    int crc = 0;
    for (int i = 0; i < PACKET_LENGTH; i++) {
        crc = CRC8_TABLE[values[i] ^ (crc & 0xFF)];
    }
    return crc;
}

int RobotArm::calculateCRC(int* packet)
{
    int crc = 0;
    for (int i = 0; i < PACKET_LENGTH; i++) {
        crc = CRC8_TABLE[packet[i] ^ (crc & 0xFF)];
    }
    return crc;
}

int* RobotArm::readPacket()
{
	//Wait for Packet Start
	this->waitForPacketStart();
	//Digest Packet
	this->digestPacket();
	//Wait for Packet End
	this->waitForPacketEnd();
	//Verify CRC
	bool checkCRC = this->verifyPacketCRC();
	//If CRC Bad set packet to BADCRC value;
	if (checkCRC == false) {
		for (int i = 0; i < PACKET_LENGTH; i++)
		{
			values[i] = PACKET_BADCRC;
		}
		//CRC for BAD_PACKET
		values[10] = (byte)169;
	}
	//Else CRC is GOOD, return values
    return values;
}

void RobotArm::updateFromPacket(int* packet)
{
    for (int i = 0; i < 10; i++) {
        prevvalues[i] = values[i];
    }
        //Update Angle Values from packet
        for (int i = 0; i < 10; i++) {
            values[i] = packet[i];
        }
    updateArm();
}

void RobotArm::updateArm()
{
    int deltas[10];
    void (RobotArm::*functions[])(int) = {
        &setShoulderRotation,
        &setShoulder,
        &setElbow,
        &setWrist,
        &setWristRotation,
        &setFinger1,
        &setFinger2,
        &setFinger3,
        &setFinger4,
        &setFinger5
    };
    for(int j=0;j<10;j++)
    {
        (this->*functions[j])(values[j]);
    }
//    for(int i=0;i<10;i++)
//    {
//        deltas[i] = (values[i]-prevvalues[i])/Servo_Angle_Steps;
//    }
//    for(int i=1;i<=Servo_Angle_Steps;i++)
//    {
//        for(int j=0;j<10;j++)
//        {
//            (this->*functions[j])(prevvalues[j]+(i*deltas[j]));
//        }
//        delay(1000);
//    }
}

void RobotArm::sendPacket(int* packetData)
{
    Serial.write(PACKET_START);
    for(int i=0;i<PACKET_LENGTH+1;i++)
    {
      Serial.write(packetData[i]);
    }
    Serial.write(PACKET_STOP);
    Serial.flush();
}



