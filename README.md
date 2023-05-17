# SeeedSentinel

![alt text](https://i.imgur.com/mYtfjEU.png)

## ![alt text](https://i.imgur.com/nnc05xy.png) What is SeeedSentinel?

The SeeedSentinel is a security system designed and created by group 4 of DIT113-2023 at the University of Gothenburg.
The project will provide an added layer of protection for homes or businesses by detecting potential intrusions and 
sending alerts to the homeowner or business owner. The system uses a variety of sensors to be able to warn the security
system owners of an intrusion, and an android app to be able to control the system.


## What is the purpose of the project?

Our project aims to create a security system using various sensors including an infrared motion sensor, a buzzer, an LED, 
and a joystick. 
The system is designed to provide a layer of security for homes or businesses, allowing 
individuals to feel more secure and protected from burglaries or break-ins. 
When the security system is activated, the person that activated it has a certain amount of time to deactivate the alarm 
on the WIO terminal device before "Intruder mode" is initiated. The alarm can be deactivated by entering a joystick pattern.
During "Intruder mode" the system will send an automated message to the homeowner, alerting them of the intrusion.
The homeowner can then react accordingly, such as calling the police or turning the alarm off from the mobile phone
app in case of a false alarm.

## Key functionalities

- Activate or deactivate alarm through an android app
- Deactivate alarm using WIO terminal sensors by entering a joystick pattern
- Change the joystick pattern password in android app
- Change notification settings in android app
- Stores password safely in a database

## Diagrams and documents

- [Wiki Diagrams](https://git.chalmers.se/courses/dit113/2023/group-4/thief-detector/-/wikis/Diagrams)

## How is the system supposed to work?

To handle communication between the UI and the SeedSentinel device, we will use MQTT.
The SeedSentinel gadget is made up of a Wio terminal and external sensors such as the PIR (Passive Infrared) sensor, 
sound sensor, buzzer, and LED. 

The PIR sensor is the main component responsible for detecting an object that emits heat, such as a human body, 
within a range of 1-5 meters. 
When the PIR sensor detects an object, it sends a signal to the Wio terminal to activate the alarm.

In addition to the PIR sensor, the sound sensor is integrated into the system. 
It detects sound intensity and outputs an analog value to the microcontroller. When the sound intensity reaches a 
certain threshold, the Wio terminal triggers the buzzer to produce a loud buzzing noise, indicating that the alarm 
is activated.
Once an object is detected by the PIR the Wio sends a signal to turn on the LED and the buzzer, which start making a loud 
buzzing noise to signal that the alarm is activated.
When an alarm is activated, the owner will be notified through an android notification. When entering a password, the database will be used 
to store user credentials for convenience.
This system provides an effective way to detect motion and sound, ensuring the security of the monitored area and providing timely alerts to the owner.



## ![alt text](https://i.imgur.com/GBdgh4z.png) Technologies used

- C++
- Java
- Arduino IDE
- Android Studio
- MQTT 
- Wio Seed Terminal with built-in sensors
- External sensors such as a PIR motion sensor,Sound sensor, RGB LED Stick, and a buzzer
- MongoDB


## ![alt text](https://i.imgur.com/S0Q1MxJ.png) Team Members

- [ ] Johan Sandgren (gussanjoba@student.gu.se)
- [ ] Stefan Ingimarsson (gusingist@student.gu.se)
- [ ] Feride Hansson (gusferiha@student.gu.se)
- [ ] Alexander Helsing (gushelsial@student.gu.se)
- [ ] Deba Arif Mohammed (gusmohamde@student.gu.se)
- [ ] Henrik Andren (gusandrhe@student.gu.se)
