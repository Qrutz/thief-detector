#include "rpcWiFi.h"
#include <Seeed_FS.h>
#include <Seeed_Arduino_FreeRTOS.h>
#include <WiFiClientSecure.h>
#include <PubSubClient.h>

const char* ssid = ""; // Replace this with WiFi network name
const char* password = ""; // Replace this with  WiFi network password

const char* mqttServer = "broker.hivemq.com"; // MQTT broker address
const int mqttPort = 1883; // MQTT port
const char* mqttTopic = "PIR_status"; // MQTT topic to publish

class MotionDetection {
private:
  const int PIR_PINNO = 2; //  pin used for PIR

  WiFiClient wifi;
  PubSubClient mqtt;

public:
  void setupMotionDetection() {
    Serial.begin(115200);
    while (!Serial); 

    WiFi.mode(WIFI_STA);
    WiFi.disconnect();

    Serial.println("Connecting to WiFi..");
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.println("Connecting to WiFi..");
      WiFi.begin(ssid, password);
    }

    Serial.println("Connected to the WiFi network");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP()); // Prints out the device's IP address

    mqtt.setClient(wifi);
    mqtt.setServer(mqttServer, mqttPort);
  }

  void loopMotionDetection() {
    if (!mqtt.connected()) {
      reconnectMQTT();
    }

    mqtt.loop();

    int motionDetected = digitalRead(PIR_PINNO);
    if (motionDetected == HIGH) {
      publishMotionDetected();
      

      delay(5000);
    }

    delay(100);
  }

private:
  void reconnectMQTT() {
    while (!mqtt.connected()) {
      Serial.println("Connecting to the MQTT broker");

      if (mqtt.connect("Wio_Terminal_Client")) {
        Serial.println("Connected to the MQTT broker");
      } else {
        Serial.print("MQTT connection failed, retrying in 5 seconds...");
        delay(5000);
      }
    }
  }

  void publishMotionDetected() {
    Serial.println("Publishing motion detection");

    if (mqtt.publish(mqttTopic, "Motion Detected")) {
      Serial.println("Motion detection published");
    } else {
      Serial.println("Motion detection publishing failed");
    }
  }
};

MotionDetection motionDetection;

void setup() {
  motionDetection.setupMotionDetection();
}

void loop() {
  motionDetection.loopMotionDetection();
}
