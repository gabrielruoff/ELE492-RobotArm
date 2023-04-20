/*************************************************** 
  This is an example for our Adafruit 16-channel PWM & Servo driver
  Servo test - this will drive 8 servos, one after the other on the
  first 8 pins of the PCA9685

  Pick one up today in the adafruit shop!
  ------> http://www.adafruit.com/products/815
  
  These drivers use I2C to communicate, 2 pins are required to  
  interface.

  Adafruit invests time and resources providing this open source code, 
  please support Adafruit and open-source hardware by purchasing 
  products from Adafruit!

  Written by Limor Fried/Ladyada for Adafruit Industries.  
  BSD license, all text above must be included in any redistribution
 ****************************************************/

#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>
#include <RobotArm.h>

#define BAUD_RATE 115200

RobotArm arm = RobotArm(BAUD_RATE);

// called this way, it uses the default address 0x40
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x40);
// you can also call it with a different address you want
//Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x41);
// you can also call it with a different address and I2C interface
// Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x40, Wire);

// Depending on your servo make, the pulse width min and max may vary, you 
// want these to be as small/large as possible without hitting the hard stop
// for max range. You'll have to tweak them as necessary to match the servos you
// have!
#define FS5155M_SERVOMIN  85 // This is the 'minimum' pulse length count (out of 4096)
#define FS5115M_SERVOMAX  477 // This is the 'maximum' pulse length count (out of 4096)
#define USMIN  600 // This is the rounded 'minimum' microsecond length based on the minimum pulse of 150
#define USMAX  2400 // This is the rounded 'maximum' microsecond length based on the maximum pulse of 600
#define SERVO_FREQ 50 // Analog servos run at ~50 Hz updates

// our servo # counter
uint8_t servonum = 0;
int testElbow = 0;
int testHand = 1;
int testWrist = 0;

void setup() {
  Serial.begin(115200);
  arm.init();
 pwm.begin();
 pwm.setOscillatorFrequency(27000000);
 pwm.setPWMFreq(SERVO_FREQ);  // Analog servos run at ~50 Hz updates
//
  delay(1000);
}

void loop() {
  // Drive each servo one at a time using setPWM()
  
 int i = 0;
 if(Serial.available())
 {
   i = Serial.parseInt();
     Serial.println(i);
//      arm.setElbow(i);
//      arm.setShoulderRotation(i);
    //  arm.setElbow(i);
     pwm.writeMicroseconds(14, i);
 }

}
