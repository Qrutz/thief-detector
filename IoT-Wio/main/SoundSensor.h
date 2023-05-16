#ifndef SOUND_SENSOR_H
#define SOUND_SENSOR_H

#include <Arduino.h>

class SoundSensor {
private:
  const int SOUND_SENSOR_PORT = D0; // Use D0 on Wio
  const int BUZZER_PORT = D6; // Use D6 on Wio
  const int SOUND_THRESHOLD = 500; // Adjust the threshold as needed
  bool isActive = true;

public:
  void setup() {
    pinMode(SOUND_SENSOR_PORT, INPUT);
    pinMode(BUZZER_PORT, OUTPUT);
    Serial.begin(115200);
    Serial.println("Sound Sensor Activated");
  }

  void loop() {
    if (isActive) {
      int soundLevel = analogRead(SOUND_SENSOR_PORT);
      Serial.print("Sound Level: ");
      Serial.println(soundLevel);

      if (soundLevel >= SOUND_THRESHOLD) {
        digitalWrite(BUZZER_PORT, HIGH);
        Serial.println("Warning: Sound Level Exceeded Threshold");
      } else {
        digitalWrite(BUZZER_PORT, LOW);
      }
    } else {
      digitalWrite(BUZZER_PORT, LOW);
    }

    if (Serial.available()) {
      char inputChar = Serial.read();
      if (inputChar == '0') {
        isActive = false;
        Serial.println("Sound Sensor Deactivated");
      } else if (inputChar == '1') {
        isActive = true;
        Serial.println("Sound Sensor Activated");
      }
    }
  }
};

#endif
